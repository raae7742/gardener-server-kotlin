package gdscsookmyung.gardener.entity.attendance.dto

data class AttendanceAttendeeDto (
    //Todo: 이름과 프로필 이미지를 직접 줄 것인지, 아니면 github id만 주고 조회하게 할 것인지?
    val github: String,
    var attendances: MutableList<AttendanceResponseDto> = mutableListOf(),
)