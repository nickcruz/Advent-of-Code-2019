package me.nickcruz

import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day5Test {

    private val day5 = Day5()

    @Test
    fun thermalEnvironmentSupervisionTerminal_diagnosticProgram_correctDiagnosticCode() {
        expectThat(day5.runTestDiagnosticProgram()) isEqualTo 3122865
    }
}