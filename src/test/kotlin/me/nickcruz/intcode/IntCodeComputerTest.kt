package me.nickcruz.intcode

import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class IntCodeComputerTest {

    companion object {
        private const val TEST_VALUE = 123456789
        private const val END = 99
        private const val NON_ZERO = 123

        private const val JUMP_IF_TRUE_POSITION = 5
        private const val JUMP_IF_TRUE_IMMEDIATE = 1105
        private const val JUMP_IF_FALSE_POSITION = 6
        private const val JUMP_IF_FALSE_IMMEDIATE = 1106
        private const val LESS_THAN_POSITION = 7
        private const val LESS_THAN_IMMEDIATE = 1107
        private const val EQUALS_POSITION = 8
        private const val EQUALS_IMMEDIATE = 1108

        // Both of these take an input, then output 0 if 0 and 1 if non-zero.
        private val DAY_5_PART_2_JUMP_POSITION = listOf(
            3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9
        )
        private val DAY_5_PART_2_JUMP_IMMEDIATE = listOf(
            3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1
        )

        private val COMPARE_TO_8 = listOf(
            3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
            1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
            999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99
        )
    }

    @Test
    fun opcode_store_savedInParameterLocation() {
        val computer = IntCodeComputer(listOf(3, 3, END, 0))

        computer.run(listOf(TEST_VALUE))

        expectThat(computer.program) isEqualTo listOf(3, 3, END, TEST_VALUE)
    }

    @Test
    fun opcode_store_noPreparedInput_0_saved() {
        val computer = IntCodeComputer(listOf(3, 3, END, TEST_VALUE))

        computer.run()

        expectThat(computer.program) isEqualTo listOf(3, 3, END, 0)
    }

    @Test
    fun opcode_print_inReturnList() {
        val computer = IntCodeComputer(listOf(4, 3, END, TEST_VALUE))

        val output = computer.run()

        expectThat(output) isEqualTo listOf(TEST_VALUE)
    }

    @Test
    fun opcode_print_immediate_day5_example() {
        val computer = IntCodeComputer(listOf(3, 0, 4, 0, END))

        val output = computer.run(listOf(TEST_VALUE))

        expectThat(output) isEqualTo listOf(TEST_VALUE)
    }

    @Test
    fun opcode_multiply_immediate_day5_example() {
        val computer = IntCodeComputer(listOf(1002, 4, 3, 4, 33))

        computer.run()

        expectThat(computer.program) isEqualTo listOf(1002, 4, 3, 4, END)
    }

    @Test
    fun opcode_add_immediate_day5_example() {
        val computer = IntCodeComputer(listOf(1101, 100, -1, 4, 0))

        computer.run()

        expectThat(computer.program) isEqualTo listOf(1101, 100, -1, 4, END)
    }

    @Test
    fun opcode_lessThan_position_day5_part2_example_lessThan() {
        val computer = IntCodeComputer(listOf(3, 9, LESS_THAN_POSITION, 9, 10, 9, 4, 9, END, -1, 8))

        val output = computer.run(listOf(7))

        expectThat(output[0]) isEqualTo 1
    }

    @Test
    fun opcode_lessThan_position_day5_part2_example_greaterThan() {
        val computer = IntCodeComputer(listOf(3, 9, LESS_THAN_POSITION, 9, 10, 9, 4, 9, END, -1, 8))

        val output = computer.run(listOf(123))

        expectThat(output[0]) isEqualTo 0
    }

    @Test
    fun opcode_lessThan_immediate_day5_part2_example_lessThan() {
        val computer = IntCodeComputer(listOf(3, 3, LESS_THAN_IMMEDIATE, -1, 8, 3, 4, 3, END))

        val output = computer.run(listOf(7))

        expectThat(output[0]) isEqualTo 1
    }

    @Test
    fun opcode_lessThan_immediate_day5_part2_example_greaterThan() {
        val computer = IntCodeComputer(listOf(3, 3, LESS_THAN_IMMEDIATE, -1, 8, 3, 4, 3, END))

        val output = computer.run(listOf(123))

        expectThat(output[0]) isEqualTo 0
    }

    @Test
    fun opcode_jumpIfTrue_position_nonZero_jumped() {
        val computer = IntCodeComputer(listOf(JUMP_IF_TRUE_POSITION, 5, 6, -1, END, NON_ZERO, 4))

        computer.run()
    }

    @Test
    fun opcode_jumpIfTrue_position_0_noJump() {
        val computer = IntCodeComputer(listOf(JUMP_IF_TRUE_POSITION, 3, END, 0))

        computer.run()
    }

    @Test
    fun opcode_jumpIfTrue_immediate_nonZero_jump() {
        val computer = IntCodeComputer(listOf(JUMP_IF_TRUE_IMMEDIATE, NON_ZERO, 4, -1, END))

        computer.run()
    }

    @Test
    fun opcode_jumpIfTrue_immediate_0_noJump() {
        val computer = IntCodeComputer(listOf(JUMP_IF_TRUE_IMMEDIATE, 0, 99))

        computer.run()
    }

    @Test
    fun opcode_jumpIfFalse_position_nonZero_noJump() {
        val computer = IntCodeComputer(listOf(JUMP_IF_FALSE_POSITION, 3, END, NON_ZERO))

        computer.run()
    }

    @Test
    fun opcode_jumpIfFalse_position_0_jump() {
        val computer = IntCodeComputer(listOf(JUMP_IF_FALSE_POSITION, 5, 6, -1, END, 0, 4))

        computer.run()
    }

    @Test
    fun opcode_jumpIfFalse_immediate_nonZero_noJump() {
        val computer = IntCodeComputer(listOf(JUMP_IF_FALSE_IMMEDIATE, NON_ZERO, END))

        computer.run()
    }

    @Test
    fun opcode_jumpIfFalse_immediate_0_jump() {
        val computer = IntCodeComputer(listOf(JUMP_IF_FALSE_IMMEDIATE, 0, 4, -1, END))

        computer.run()
    }

    @Test
    fun opcode_jumpIfFalse_day5_part2_position_example_0_0() {
        val computer = IntCodeComputer(DAY_5_PART_2_JUMP_POSITION)

        val output = computer.run(listOf(0))

        expectThat(output[0]) isEqualTo 0
    }

    @Test
    fun opcode_jumpIfFalse_day5_part2_position_example_nonZero_1() {
        val computer = IntCodeComputer(DAY_5_PART_2_JUMP_POSITION)

        val output = computer.run(listOf(NON_ZERO))

        expectThat(output[0]) isEqualTo 1
    }

    @Test
    fun opcode_jumpIfTrue_day5_part2_immediate_example_0_0() {
        val computer = IntCodeComputer(DAY_5_PART_2_JUMP_IMMEDIATE)

        val output = computer.run(listOf(0))

        expectThat(output[0]) isEqualTo 0
    }

    @Test
    fun opcode_jumpIfTrue_day5_part2_immediate_example_nonZero_1() {
        val computer = IntCodeComputer(DAY_5_PART_2_JUMP_IMMEDIATE)

        val output = computer.run(listOf(NON_ZERO))

        expectThat(output[0]) isEqualTo 1
    }

    @Test
    fun opcode_equals_position_day5_part2_example_equal() {
        val computer = IntCodeComputer(listOf(3, 9, EQUALS_POSITION, 9, 10, 9, 4, 9, END, -1, 8))

        val output = computer.run(listOf(8))

        expectThat(output[0]) isEqualTo 1
    }

    @Test
    fun opcode_equals_position_day5_part2_example_unEqual() {
        val computer = IntCodeComputer(listOf(3, 9, EQUALS_POSITION, 9, 10, 9, 4, 9, END, -1, 8))

        val output = computer.run(listOf(123))

        expectThat(output[0]) isEqualTo 0
    }

    @Test
    fun opcode_equals_immediate_day5_part2_example_equal() {
        val computer = IntCodeComputer(listOf(3, 3, EQUALS_IMMEDIATE, -1, 8, 3, 4, 3, END))

        val output = computer.run(listOf(8))

        expectThat(output[0]) isEqualTo 1
    }

    @Test
    fun opcode_equals_immediate_day5_part2_example_unEqual() {
        val computer = IntCodeComputer(listOf(3, 3, EQUALS_IMMEDIATE, -1, 8, 3, 4, 3, END))

        val output = computer.run(listOf(123))

        expectThat(output[0]) isEqualTo 0
    }

    @Test
    fun day5_part2_example_INT_MIN_999() {
        val computer = IntCodeComputer(COMPARE_TO_8)

        val output = computer.run(listOf(Int.MIN_VALUE))

        expectThat(output[0]) isEqualTo 999
    }

    @Test
    fun day5_part2_example_7_999() {
        val computer = IntCodeComputer(COMPARE_TO_8)

        val output = computer.run(listOf(7))

        expectThat(output[0]) isEqualTo 999
    }

    @Test
    fun day5_part2_example_8_1000() {
        val computer = IntCodeComputer(COMPARE_TO_8)

        val output = computer.run(listOf(8))

        expectThat(output[0]) isEqualTo 1000
    }

    @Test
    fun day5_part2_example_9_1001() {
        val computer = IntCodeComputer(COMPARE_TO_8)

        val output = computer.run(listOf(9))

        expectThat(output[0]) isEqualTo 1001
    }

    @Test
    fun day5_part2_example_INT_MAX_1001() {
        val computer = IntCodeComputer(COMPARE_TO_8)

        val output = computer.run(listOf(Int.MAX_VALUE))

        expectThat(output[0]) isEqualTo 1001
    }
}
