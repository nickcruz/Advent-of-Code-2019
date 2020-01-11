package me.nickcruz

import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day2Test {

    private val day2 = Day2()

    @Test
    fun example1() {
        val program = listOf(
            1, 9, 10, 3,
            2, 3, 11, 0,
            99,
            30, 40, 50
        )

        expectThat(
            day2.run(program)
        ) isEqualTo listOf(
            3500, 9, 10, 70,
            2, 3, 11, 0,
            99,
            30, 40, 50
        )
    }

    @Test
    fun example2() {
        val program = listOf(1, 0, 0, 0, 99)

        expectThat(day2.run(program)) isEqualTo listOf(2, 0, 0, 0, 99)
    }

    @Test
    fun example3() {
        val program = listOf(2, 3, 0, 3, 99)

        expectThat(day2.run(program)) isEqualTo listOf(2, 3, 0, 6, 99)
    }

    @Test
    fun example4() {
        val program = listOf(2, 4, 4, 5, 99, 0)

        expectThat(day2.run(program)) isEqualTo listOf(2, 4, 4, 5, 99, 9801)
    }

    @Test
    fun example5() {
        val program = listOf(1, 1, 1, 4, 99, 5, 6, 0, 99)

        expectThat(day2.run(program)) isEqualTo listOf(30, 1, 1, 4, 2, 5, 6, 0, 99)
    }

    @Test
    fun solution_part1() {
        expectThat(day2.run(PUZZLE_INPUT)[0]) isEqualTo 2692315
    }

    companion object {
        private val PUZZLE_INPUT = listOf(
            1,
            12, // position 1 (0), replaced with 12
            2, // position 2 (0), replaced with 2
            3,
            1,
            1,
            2,
            3,
            1,
            3,
            4,
            3,
            1,
            5,
            0,
            3,
            2,
            1,
            6,
            19,
            1,
            19,
            5,
            23,
            2,
            13,
            23,
            27,
            1,
            10,
            27,
            31,
            2,
            6,
            31,
            35,
            1,
            9,
            35,
            39,
            2,
            10,
            39,
            43,
            1,
            43,
            9,
            47,
            1,
            47,
            9,
            51,
            2,
            10,
            51,
            55,
            1,
            55,
            9,
            59,
            1,
            59,
            5,
            63,
            1,
            63,
            6,
            67,
            2,
            6,
            67,
            71,
            2,
            10,
            71,
            75,
            1,
            75,
            5,
            79,
            1,
            9,
            79,
            83,
            2,
            83,
            10,
            87,
            1,
            87,
            6,
            91,
            1,
            13,
            91,
            95,
            2,
            10,
            95,
            99,
            1,
            99,
            6,
            103,
            2,
            13,
            103,
            107,
            1,
            107,
            2,
            111,
            1,
            111,
            9,
            0,
            99,
            2,
            14,
            0,
            0
        )
    }
}
