package me.nickcruz

import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day5Test {

    private val day5 = Day5()

    @Test
    fun part1_thermalEnvironmentSupervisionTerminal_diagnosticProgram_airConditioner() {
        expectThat(day5.turnOnAirConditioner()) isEqualTo 3122865
    }

    @Test
    fun part2_thermalEnvironmentSupervisionTerminal_diagnosticProgram_thermalRadiatorController() {
        println(day5.turnOnThermalRadiatorController().toString())
    }
}