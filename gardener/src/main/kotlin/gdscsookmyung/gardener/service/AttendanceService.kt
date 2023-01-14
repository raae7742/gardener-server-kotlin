package gdscsookmyung.gardener.service

import gdscsookmyung.gardener.entity.attendance.Attendance
import gdscsookmyung.gardener.entity.attendance.dto.AttendanceDateDto
import gdscsookmyung.gardener.entity.attendance.dto.AttendanceAttendeeDto
import gdscsookmyung.gardener.entity.attendance.dto.AttendanceResponseDto
import gdscsookmyung.gardener.entity.attendee.Attendee
import gdscsookmyung.gardener.entity.event.Event
import gdscsookmyung.gardener.repository.AttendanceRepository
import gdscsookmyung.gardener.repository.AttendeeRepository
import gdscsookmyung.gardener.repository.EventRepository
import gdscsookmyung.gardener.service.util.GithubUtil
import gdscsookmyung.gardener.service.util.SlackUtil
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
    private val eventRepository: EventRepository,
    private val attendeeRepository: AttendeeRepository,
    private val githubUtil: GithubUtil,
    private val slackUtil: SlackUtil,
) {

    @Transactional
    fun create(attendee: Attendee, date: LocalDate?): Attendance {
        return if (date == null)
            attendanceRepository.save(Attendance(attendee = attendee, date = LocalDate.now()))
        else
            attendanceRepository.save(Attendance(attendee = attendee, date = date))
    }

    fun readAllByEventId(eventId: Long): MutableList<AttendanceAttendeeDto> {
        val event = eventRepository.findById(eventId).orElseThrow { CustomException(ErrorCode.NOT_FOUND) }
        val attendees = attendeeRepository.findAllByEvent(event)
        val response: MutableList<AttendanceAttendeeDto> = mutableListOf()

        updateAllCommit(event, attendees)

        attendees.stream().forEach { attendee ->
            val dto = AttendanceAttendeeDto(attendee.github!!)

            val queue = ArrayDeque<Attendance>()
            for (a in attendanceRepository.findAllByAttendee(attendee)) {
                queue.add(a)
            }

            var date = event.startedAt!!
            while (!date.isAfter(event.endedAt)) {
                if (queue.isNotEmpty() && queue.first().date.isEqual(date)) {
                    dto.attendances.add(AttendanceResponseDto(date, queue.first().commit))
                    queue.removeFirst()
                }
                else {
                    dto.attendances.add(AttendanceResponseDto(date, false))
                }

                date = date.plusDays(1)
            }
            response.add(dto)
        }
        return response
    }

    fun readAllByEventIdAndDate(eventId: Long, date: LocalDate): MutableList<AttendanceDateDto> {
        val event = eventRepository.findById(eventId).orElseThrow { CustomException(ErrorCode.NOT_FOUND) }

        if (date.isBefore(event.startedAt)) {
            throw CustomException(ErrorCode.EVENT_NOT_STARTED)
        }

        if (date.isAfter(event.endedAt)) {
            throw CustomException(ErrorCode.EVENT_EXPIRED)
        }

        val attendees = attendeeRepository.findAllByEvent(event)

        updateAllCommit(event, attendees)

        val response = mutableListOf<AttendanceDateDto>()
        attendanceRepository.findByEventAndDate(event.id!!, date).stream().forEach {
            response.add(AttendanceDateDto(github = it.attendee?.github!!, isChecked = it.commit))
        }

        return response
    }

    @Transactional
    private fun updateAllCommit(event: Event, attendees: List<Attendee>) {
        val startedAt = event.startedAt
        val endedAt = event.endedAt

        for (attendee in attendees) {
            val iterator = githubUtil.getCommits(attendee.github!!)
            try {
                // 최근 순부터 반복
                while (iterator.hasNext()) {
                    val commit = iterator.next()
                    val date = commit.commitDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

                    if (date.isBefore(startedAt)) {
                        return
                    }

                    if (date.isBefore(endedAt) || date.isEqual(endedAt)) {
                        val attendance = attendanceRepository.findByAttendeeAndDate(attendee, date)
                            ?: Attendance(attendee = attendee, date = date)
                        attendance.commit = true
                        attendanceRepository.save(attendance)
                    }

                    if (date.isEqual(endedAt) || date.isAfter(endedAt)) {
                        return
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                throw CustomException(ErrorCode.COMMIT_ERROR)
            }
        }
    }

    @Transactional
    private fun updateAllTil(attendee: Attendee) {

        slackUtil.fetchHistory()

        //Todo
    }

    // 임시 메소드
    @Transactional
    fun updateTil(attendance: Attendance) {
        attendance.til = true
        attendanceRepository.save(attendance)
    }
}