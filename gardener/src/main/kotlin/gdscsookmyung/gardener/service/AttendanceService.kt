package gdscsookmyung.gardener.service

import gdscsookmyung.gardener.entity.attendance.Attendance
import gdscsookmyung.gardener.entity.attendance.dto.AttendanceDateDto
import gdscsookmyung.gardener.entity.attendance.dto.AttendanceAttendeeDto
import gdscsookmyung.gardener.entity.attendance.dto.AttendanceResponseDto
import gdscsookmyung.gardener.entity.attendee.Attendee
import gdscsookmyung.gardener.entity.event.Event
import gdscsookmyung.gardener.repository.AttendanceRepository
import gdscsookmyung.gardener.service.util.GithubUtil
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import java.time.LocalDate
import javax.transaction.Transactional

@Service
@RequiredArgsConstructor
class AttendanceService(
    private val attendanceRepository: AttendanceRepository,
    private val githubUtil: GithubUtil
) {

    @Transactional
    fun create(attendee: Attendee, date: LocalDate?): Attendance {
        return if (date == null) attendanceRepository.save(Attendance(attendee = attendee))
        else attendanceRepository.save(Attendance(attendee = attendee, date = date))
    }

    fun readAllByAttendee(attendee: Attendee): AttendanceAttendeeDto {
        var response = AttendanceAttendeeDto(github = attendee.github!!)
        val attendances = attendanceRepository.findByAttendee(attendee)

        for (a in attendances) {
            response.attendances.add(
                AttendanceResponseDto(
                    date = a.date,
                    isChecked = a.isChecked
                ))
        }
        return response
    }

    fun readAllByEventAndDate(event_id: Long, date: LocalDate): MutableList<AttendanceDateDto> {
        val response = mutableListOf<AttendanceDateDto>()
        val attendances = attendanceRepository.findByEventAndDate(event_id, date)

        for (a in attendances) {
            response.add(AttendanceDateDto(
                github = a.attendee?.github!!,
                isChecked = a.isChecked
            ))
        }
        return response
    }

    @Transactional
    fun updateAllCommit(attendee: Attendee) {
        //Todo
    }

    @Transactional
    fun updateAllTil(attendee: Attendee) {
        //Todo
    }

    // 임시 메소드
    @Transactional
    fun updateCommit(attendance: Attendance) {
        attendance.commit = true
        attendanceRepository.save(attendance)
    }

    // 임시 메소드
    @Transactional
    fun updateTil(attendance: Attendance) {
        attendance.til = true
        attendanceRepository.save(attendance)
    }
}