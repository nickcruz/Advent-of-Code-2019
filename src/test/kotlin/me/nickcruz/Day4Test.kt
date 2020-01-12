package me.nickcruz

import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day4Test {

    private val day4 = Day4()

    @Test
    fun solution_part1() {
        expectThat(day4.run()) isEqualTo 1716
    }
}