package me.nickcruz

/**
 * --- Day 4: Secure Container ---
 * You arrive at the Venus fuel depot only to discover it's protected by a password. The Elves had written the password
 * on a sticky note, but someone threw it out.
 *
 * However, they do remember a few key facts about the password:
 *
 * - It is a six-digit number.
 * - The value is within the range given in your puzzle input.
 * - Two adjacent digits are the same (like 22 in 122345).
 * - Going from left to right, the digits never decrease; they only ever increase or stay the same (like 111123 or
 * 135679).
 *
 * Other than the range rule, the following are true:
 *
 * 111111 meets these criteria (double 11, never decreases).
 * 223450 does not meet these criteria (decreasing pair of digits 50).
 * 123789 does not meet these criteria (no double).
 * How many different passwords within the range given in your puzzle input meet these criteria?
 *
 * Your puzzle input is 165432-707912.
 *
 * --- Part Two ---
 * An Elf just remembered one more important detail: the two adjacent matching digits are not part of a larger group of
 * matching digits.
 *
 * Given this additional criterion, but still ignoring the range rule, the following are now true:
 *
 * 112233 meets these criteria because the digits never decrease and all repeated digits are exactly two digits long.
 * 123444 no longer meets the criteria (the repeated 44 is part of a larger group of 444).
 * 111122 meets the criteria (even though 1 is repeated more than twice, it still contains a double 22).
 * How many different passwords within the range given in your puzzle input meet all of the criteria?
 *
 * Your puzzle input is still 165432-707912.
 */
class Day4 {

    companion object {
        private const val FROM = 165432
        private const val TO = 707912
    }

    fun part1() = run { hasTwoAdjacentDigits() && areDigitsNeverDecreasing() }

    fun part2() = run {
        hasTwoAdjacentDigits()
                && areDigitsNeverDecreasing()
                && hasTwoAdjacentMatchingDigitsNotPartOfLargerMatchingDigit()
    }

    private fun run(criteria: Int.() -> Boolean): Int {
        val possiblePasswords = mutableSetOf<Int>()
        for (n in FROM..TO) {
            if (n.criteria()) {
                possiblePasswords.add(n)
            }
        }
        return possiblePasswords.size
    }


    // Two adjacent digits are the same (like 22 in 122345)
    private fun Int.hasTwoAdjacentDigits(): Boolean {
        val digits = digits()
        var hasTwoAdjacentDigits = false
        for (i in 0..(digits.size - 2)) {
            hasTwoAdjacentDigits = hasTwoAdjacentDigits || digits[i] == digits[i + 1]
        }
        return hasTwoAdjacentDigits
    }

    // Going from left to right, the digits never decrease
    private fun Int.areDigitsNeverDecreasing(): Boolean {
        val digits = digits()
        var isNeverDecreasing = true
        for (i in 0..(digits.size - 2)) {
            isNeverDecreasing = isNeverDecreasing && digits[i] <= digits[i + 1]
        }
        return isNeverDecreasing
    }

    // Used for testing
    fun hasTwoAdjacentMatchingDigitsLargerMatchingDigit(int: Int) = with(int) {
        hasTwoAdjacentMatchingDigitsNotPartOfLargerMatchingDigit()
    }

    // The two adjacent matching digits are not part of a larger group of matching digits
    private fun Int.hasTwoAdjacentMatchingDigitsNotPartOfLargerMatchingDigit(): Boolean {
        val digits = digits()
        val matchingDigitGroups = mutableListOf<MatchingDigitGroup>()
        var currentGroup: MatchingDigitGroup? = null
        var i = 0
        while (i < digits.size) {
            if (currentGroup == null) {
                // If no current group, make one
                currentGroup = MatchingDigitGroup(digits[i], listOf(i))

            } else if (currentGroup.digit == digits[i] && currentGroup.indices.last() == i - 1) {
                // If current digit is part of previous current group, add it to that group
                currentGroup = currentGroup.copy(
                    indices = currentGroup.indices.toMutableList().apply { add(i) }.toList()
                )
            } else {
                // Otherwise, we have a new number and a potential new matching digit grouping
                if (currentGroup.size > 1) {
                    // If it's greater than 1 (an actual group), add it to a collection. Otherwise do nothing about it
                    matchingDigitGroups.add(currentGroup)
                }
                currentGroup = MatchingDigitGroup(digits[i], listOf(i))
            }
            ++i
        }
        currentGroup?.let {
            if (it.size > 1) {
                matchingDigitGroups.add(currentGroup)
            }
        }
        return matchingDigitGroups.any { it.size == 2 }
    }

    /**
     * Returns the digits of this number in a list, from most significant digit to least significant.
     */
    private fun Int.digits(): List<Int> {
        var n = this
        val digits = mutableListOf<Int>()
        while (n > 0) {
            digits.add(n % 10)
            n /= 10
        }
        return digits.reversed()
    }

    data class MatchingDigitGroup(
        val digit: Int,
        val indices: List<Int>
    ) {
        val size: Int
            get() = indices.size
    }
}