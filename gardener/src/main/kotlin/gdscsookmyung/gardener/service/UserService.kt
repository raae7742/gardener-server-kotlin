package gdscsookmyung.gardener.service

import gdscsookmyung.gardener.auth.JwtTokenProvider
import gdscsookmyung.gardener.auth.RoleType
import gdscsookmyung.gardener.entity.user.User
import gdscsookmyung.gardener.entity.user.dto.LoginResponseDto
import gdscsookmyung.gardener.entity.user.dto.LoginRequestDto
import gdscsookmyung.gardener.entity.user.dto.UserRequestDto
import gdscsookmyung.gardener.repository.UserRepository
import gdscsookmyung.gardener.util.exception.CustomException
import gdscsookmyung.gardener.util.exception.ErrorCode
import lombok.RequiredArgsConstructor
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import javax.transaction.Transactional

@Service
@RequiredArgsConstructor
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Transactional
    fun join(requestDto: UserRequestDto): LoginResponseDto {
        if (validateDuplicatedGithub(requestDto.github)) throw CustomException(ErrorCode.USER_GITHUB_DUPLICATED)
        if (validateDuplicatedUsername(requestDto.username)) throw CustomException(ErrorCode.USER_NAME_DUPLICATED)

        val user = User(
            username = requestDto.username,
            password = passwordEncoder.encode(requestDto.password),
            github = requestDto.github,
            role = RoleType.USER
        )
        userRepository.save(user)

        return login(LoginRequestDto(requestDto.username, requestDto.password))
    }

    @Transactional
    fun login(loginDto: LoginRequestDto): LoginResponseDto {
        val user : User = userRepository.findByUsername(loginDto.username).orElseThrow { throw CustomException(ErrorCode.USER_LOGIN_FAIL) }

        if (!passwordEncoder.matches(loginDto.password, user.password))
            throw CustomException(ErrorCode.USER_LOGIN_FAIL)

        return LoginResponseDto(
            id = user.id,
            username = user.username,
            github = user.github,
            jwtToken = jwtTokenProvider.createToken(user.username, user.role),
            refreshToken = jwtTokenProvider.createToken(user.username, user.role)
        )
    }

    private fun validateDuplicatedGithub(github: String): Boolean {
        return userRepository.existsByGithub(github)
    }

    private fun validateDuplicatedUsername(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }

    fun updateUsername(id: Long, username: String): User {
        val user : User = userRepository.findById(id).orElseThrow { throw CustomException(ErrorCode.USER_NOT_FOUND) }

        user.update(username)
        return userRepository.save(user)
    }
}