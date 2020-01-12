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
 */
class Day4 {

    companion object {
        private const val FROM = 165432
        private const val TO = 707912
    }

    fun run(): Int {
        val possiblePasswords = mutableSetOf<Int>()
        for (n in FROM..TO) {
            val digits = n.digits()

            // Two adjacent digits are the same (like 22 in 122345)
            var hasTwoAdjacentDigits = false
            for (i in 0..(digits.size - 2)) {
                hasTwoAdjacentDigits = hasTwoAdjacentDigits || digits[i] == digits[i + 1]
            }

            // Going from left to right, the digits never decrease
            var isNeverDecreasing = true
            for (i in 0..(digits.size - 2)) {
                isNeverDecreasing = isNeverDecreasing && digits[i] <= digits[i + 1]
            }
            if (hasTwoAdjacentDigits && isNeverDecreasing) {
                possiblePasswords.add(n)
            }
        }
        return possiblePasswords.size
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
}