package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userService: UserService,
) {

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("유저 저장")
    fun saveUserTest() {
        // given
        val request = UserCreateRequest("주현진", null)

        // when
        userService.saveUser(request)

        // then
        val result = userRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("주현진")
        assertThat(result[0].age).isNull()
    }

    @Test
    @DisplayName("유저 조회")
    fun getUserTest() {
        // given
        userRepository.saveAll(listOf(
            User("현진", 32),
            User("주넌", 33)
        ))

        // when
        val result = userService.getUsers()

        // then
        assertThat(result).hasSize(2)
        assertThat(result).extracting("name").containsExactlyInAnyOrder("현진", "주넌")
        assertThat(result).extracting("age").containsExactlyInAnyOrder(32, 33)
    }

    @Test
    @DisplayName("유저 수정")
    fun updateUserNameTest() {
        // given
        val savedUser = userRepository.save(User("현진", null))
        val request = UserUpdateRequest(savedUser.id, "B")

        // when
        userService.updateUserName(request)

        // then
        val result = userRepository.findAll()[0]
        assertThat(result.name).isEqualTo("B")
    }

    @Test
    @DisplayName("유저 삭제")
    fun deleteUserTest() {
        // given
        userRepository.save(User("현진", null))

        // when
        userService.deleteUser("현진")

        // then
        assertThat(userRepository.findAll()).isEmpty()
    }
}