package me.nickcruz

import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isGreaterThan
import strikt.assertions.isLessThan

class Day4Test {

    private val day4 = Day4()

    @Test
    fun solution_part1() {
        expectThat(day4.part1()) isEqualTo 1716
    }

    @Test
    fun solution_part2() {
        expectThat(day4.part2()) isLessThan 1188 // first incorrect guess, too high
        expectThat(day4.part2()) isGreaterThan 904 // second incorrect guess, too low
        expectThat(day4.part2()) isEqualTo 1163
    }

    @Test
    fun largerMatchingGroup_112233_true() {
        expectThat(day4.hasTwoAdjacentMatchingDigitsLargerMatchingDigit(112233)) isEqualTo true
    }

    @Test
    fun largerMatchingGroup_112211_true() {
        expectThat(day4.hasTwoAdjacentMatchingDigitsLargerMatchingDigit(112211)) isEqualTo true
    }

    @Test
    fun largerMatchingGroup_123444_false() {
        expectThat(day4.hasTwoAdjacentMatchingDigitsLargerMatchingDigit(123444)) isEqualTo false
    }

    // Inverted previous given example to test out code more. This seems good
    @Test
    fun largerMatchingGroup_111234_false() {
        expectThat(day4.hasTwoAdjacentMatchingDigitsLargerMatchingDigit(111234)) isEqualTo false
    }

    @Test
    fun largerMatchingGroup_111122_true() {
        expectThat(day4.hasTwoAdjacentMatchingDigitsLargerMatchingDigit(111122)) isEqualTo true
    }

    @Test
    fun largerMatchingGroup_111111_false() {
        expectThat(day4.hasTwoAdjacentMatchingDigitsLargerMatchingDigit(111111)) isEqualTo false
    }

    @Test
    fun largerMatchingGroup_111222_false() {
        expectThat(day4.hasTwoAdjacentMatchingDigitsLargerMatchingDigit(111222)) isEqualTo false
    }

    @Test
    fun largerMatchingGroup_123455_true() {
        expectThat(day4.hasTwoAdjacentMatchingDigitsLargerMatchingDigit(123455)) isEqualTo true
    }
}