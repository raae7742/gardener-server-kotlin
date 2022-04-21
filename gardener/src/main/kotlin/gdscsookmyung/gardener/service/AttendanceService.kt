package gdscsookmyung.gardener.service

import gdscsookmyung.gardener.entity.attendance.Attendance
import gdscsookmyung.gardener.entity.attendance.dto.AttendanceDateDto
import gdscsookmyung.gardener.entity.attendance.dto.AttendanceAttendeeDto
import gdscsookmyung.gardener.entity.attendance.dto.AttendanceResponseDto
import gdscsookmyung.gardener.entity.attendee.Attendee
import gdscsookmyung.gardener.entity.event.Event
import gdscsookmyung.gardener.repository.AttendanceRepository
import gdscsookmyung.gardener.service.util.GithubUtil
import gdscsookmyung.gardener.util.exception.CustomException
import gdscsookmyung.gardener.util.exception.ErrorCode
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import java.io.IOException
import java.time.LocalDate
import java.time.ZoneId
import javax.transaction.Transactional

@Service
@RequiredArgsConstructor
class AttendanceService(
    private val attendanceRepository: AttendanceRepository,
    private val githubUtil: GithubUtil
) {

    @Transactional
    fun create(attendee: Attendee, date: LocalDate?): Attendance {
        return if (date == null) attendanceRepository.save(Attendance(attendee = attendee, date = LocalDate.now()))
        else attendanceRepository.save(Attendance(attendee = attendee, date = date))
    }

    fun readAllByAttendee(event: Event, attendees: List<Attendee>): MutableList<AttendanceAttendeeDto> {
        updateAllCommit(event, attendees)

        val response: MutableList<AttendanceAttendeeDto> = mutableListOf()
        for (attendee in attendees) {
            val attendeeDto = AttendanceAttendeeDto(github = attendee.github!!)
            val attendances = attendanceRepository.findByAttendee(attendee)

            for (a in attendances) {
                attendeeDto.attendances.add(
                    AttendanceResponseDto(
                        date = a.date,
                        isChecked = a.commit
                    ))
            }
            response.add(attendeeDto)
        }
        return response
    }

    fun readAllByEventAndDate(event: Event, attendees: List<Attendee>, date: LocalDate): MutableList<AttendanceDateDto> {
        updateAllCommit(event, attendees)

        val response = mutableListOf<AttendanceDateDto>()
        val attendances = attendanceRepository.findByEventAndDate(event.id!!, date)

        for (a in attendances) {
            response.add(AttendanceDateDto(
                github = a.attendee?.github!!,
                isChecked = a.commit
            ))
        }
        return response
    }

    @Transactional
    fun updateAllCommit(event: Event, attendees: List<Attendee>) {
        val startedAt = event.startedAt
        val endedAt = event.endedAt

        for (attendee in attendees) {
            val iterator = githubUtil.getCommits(attendee.github!!)
            try {
                while (iterator.hasNext()) {
                    val commit = iterator.next()
                    val date = commit.commitDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

                    if (date.isBefore(startedAt)) return

                    if (date.isBefore(endedAt) || date.isEqual(endedAt)) {
                        val attendance = attendanceRepository.findByAttendeeAndDate(attendee, date)
                        attendance.commit = true
                        attendanceRepository.save(attendance)

                        if (date.isEqual(endedAt)) return
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                throw CustomException(ErrorCode.COMMIT_ERROR)
            }
        }
    }

    @Transactional
    fun updateAllTil(attendee: Attendee) {
        //Todo
    }

    // 임시 메소드
    @Transactional
    fun updateTil(attendance: Attendance) {
        attendance.til = true
        attendanceRepository.save(attendance)
    }
}