package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
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
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
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
        val request = UserUpdateRequest(savedUser.id!!, "B")

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

    @Test
    @DisplayName("대출 기록이 없는 유저도 응답에 포함된다")
    fun getUserLoanHistoriesTest1() {
        // given
        userRepository.save(User("현진주", null))

        // when
        val results = userService.getUserLoanHistories()

        // then
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("현진주")
        assertThat(results[0].books).isEmpty()
    }

    @Test
    @DisplayName("대출 기록이 많은 유저의 응답이 정상 동작한다")
    fun getUserLoanHistoriesTest2() {
        // given
        val savedUser = userRepository.save(User("현진주", null))
        userLoanHistoryRepository.saveAll(listOf(
                UserLoanHistory.fixture(savedUser, "책1", UserLoanStatus.LOANED),
                UserLoanHistory.fixture(savedUser, "책2", UserLoanStatus.LOANED),
                UserLoanHistory.fixture(savedUser, "책3", UserLoanStatus.RETURNED),
        ))

        // when
        val results = userService.getUserLoanHistories()

        // then
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("현진주")
        assertThat(results[0].books).hasSize(3)
        assertThat(results[0].books).extracting("name")
                .containsExactlyInAnyOrder("책1", "책2", "책3")
        assertThat(results[0].books).extracting("isReturn")
                .containsExactlyInAnyOrder(false, false, true)

    }

//    @Test
//    @DisplayName("위에 두 경우가 합쳐진 테스트")    // 하지만 작은 테스트코드 2개가 유지보수하는데 더 낫다
//    fun getUserLoanHistoriesTest3() {
//        // given
//        val savedUsers = userRepository.saveAll(listOf(
//                User("현진주", null),
//                User("주넌초이", null),
//        ))
//
//        userLoanHistoryRepository.saveAll(listOf(
//                UserLoanHistory.fixture(savedUsers[0], "책1", UserLoanStatus.LOANED),
//                UserLoanHistory.fixture(savedUsers[0], "책2", UserLoanStatus.LOANED),
//                UserLoanHistory.fixture(savedUsers[0], "책3", UserLoanStatus.RETURNED),
//        ))
//
//        // when
//        val results = userService.getUserLoanHistories()
//
//        // then
//        assertThat(results).hasSize(2)
//        val userResult = results.first { it.name == "현진주" }
//        assertThat(userResult.books).hasSize(3)
//        assertThat(userResult.books).extracting("name")
//                .containsExactlyInAnyOrder("책1", "책2", "책3")
//        assertThat(userResult.books).extracting("isReturn")
//                .containsExactlyInAnyOrder(false, false, true)
//
//    }
}