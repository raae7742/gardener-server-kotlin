package gdscsookmyung.gardener.service

import gdscsookmyung.gardener.entity.user.User
import gdscsookmyung.gardener.entity.user.dto.LoginResponseDto
import gdscsookmyung.gardener.entity.user.dto.LoginRequestDto
import gdscsookmyung.gardener.entity.user.dto.UserRequestDto
import gdscsookmyung.gardener.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import javax.transaction.Transactional

@Service
@RequiredArgsConstructor
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional
    fun join(requestDto: UserRequestDto): LoginResponseDto {
        if (validateDuplicatedGithub(requestDto.github)) throw IllegalArgumentException()
        if (validateDuplicatedUsername(requestDto.username)) throw IllegalArgumentException()

        val user = User(
            username = requestDto.username,
            password = requestDto.password,
            github = requestDto.github,
            role = "ROLE_USER"
        )
        userRepository.save(user)

        return login(LoginRequestDto(user.username!!, user.password!!))
    }

    @Transactional
    fun login(loginDto: LoginRequestDto): LoginResponseDto {
        val user = userRepository.findByUsername(loginDto.username)
            ?: throw IllegalArgumentException("해당 회원이 존재하지 않습니다.")

        if (loginDto.password.contentEquals(user.password))
            return LoginResponseDto(
                username = user.username!!,
                github = user.github!!,
                jwtToken = "1",
                refreshToken = "1"
            )
        else throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
    }

    private fun validateDuplicatedGithub(github: String): Boolean {
        return userRepository.existsByGithub(github)
    }

    private fun validateDuplicatedUsername(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }
}