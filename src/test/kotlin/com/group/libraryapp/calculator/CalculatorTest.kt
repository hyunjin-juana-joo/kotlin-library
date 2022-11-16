package com.group.libraryapp.calculator

fun main() {
    val calculatorTest = CalculatorTest()
    calculatorTest.addTest()
    calculatorTest.minusTest()
    calculatorTest.multiplyTest()
    calculatorTest.divideTest()
    calculatorTest.divideExceptionTest()
}

class CalculatorTest {

    // given-when-then pattern
    fun addTest() {
        // 1. given: 테스트를 하고 싶은 대상을 만든다
        val calculator = Calculator(5)
        // 2. when: 실제 테스트하고 싶은 기능을 호출한다.
        calculator.add(3)

        // 3. then: 의도한대로 결과가 나왔는지 확인한다.
        if (calculator.number != 8) {
            throw  IllegalStateException()
        }
    }

    fun minusTest() {
        val calculator = Calculator(5)
        calculator.minus(3)

        if (calculator.number != 2) {
            throw  IllegalStateException()
        }
    }

    fun multiplyTest() {
        val calculator = Calculator(5)
        calculator.multiply(3)

        if (calculator.number != 15) {
            throw  IllegalStateException()
        }
    }

    fun divideTest() {
        val calculator = Calculator(5)
        calculator.divide(2)

        if (calculator.number != 2) {
            throw  IllegalStateException()
        }
    }

    fun divideExceptionTest() {
        val calculator = Calculator(5)

        try {
            calculator.divide(0)
        } catch (e: IllegalArgumentException) {
            // 테스트 성공
            if (e.message != "0으로 나눌 수 없습니다.") {
                throw  IllegalStateException("메시지가 다릅니다.")
            }
            return
        } catch (e: Exception) {
            throw  IllegalStateException("기대한 에러가 아닙니다.")
        }
    }
}