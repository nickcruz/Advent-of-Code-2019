package me.nickcruz.intcode

import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class IntCodeComputerTest {

    companion object {
        private const val TEST_VALUE = 123456789
    }

    @Test
    fun opcode_store_savedInParameterLocation() {
        val computer = IntCodeComputer(listOf(3, 3, 99, 0))

        computer.run(listOf(TEST_VALUE))

        expectThat(computer.program) isEqualTo listOf(3, 3, 99, TEST_VALUE)
    }

    @Test
    fun opcode_store_noPreparedInput_0_saved() {
        val computer = IntCodeComputer(listOf(3, 3, 99, TEST_VALUE))

        computer.run()

        expectThat(computer.program) isEqualTo listOf(3, 3, 99, 0)
    }

    @Test
    fun opcode_print_inReturnList() {
        val computer = IntCodeComputer(listOf(4, 3, 99, TEST_VALUE))

        val results = computer.run()

        expectThat(results) isEqualTo listOf(TEST_VALUE)
    }

    @Test
    fun opcode_day5_example_printWhatYouStored() {
        val computer = IntCodeComputer(listOf(3, 0, 4, 0, 99))

        val results = computer.run(listOf(TEST_VALUE))

        expectThat(results) isEqualTo listOf(TEST_VALUE)
    }
}