import java.util.*

object SmartCalculator {
    private val NUMBERS = mutableMapOf<String, Int>()

    fun run() {
        val scanner = Scanner(System.`in`)
        var input = scanner.nextLine()

        while (input != "/exit") {
            if (input != "") {
                if (input[0] != '/') {
                    if (input.contains('=')) memoryAdd(input) else doMath(input)
                } else command(input)
            }
            input = scanner.nextLine()
        }
        println("Bye!")
    }

    private fun doMath(expression: String) {
        val strings = expression.split(" ").toTypedArray()
        var sum = 0
        var shouldBeNumber = true
        var subtract = false
        var print = true

        for (num in strings) {
            var char = num[0]
            if (num.length > 1 && (char == '-' || char == '+') && char != num[1]) char = ' '

            if (shouldBeNumber) {
                val num1: Int? = if (isNumber(num)) num.toInt() else memoryGet(num)
                if (num1 == null) {
                    print = false
                    unknownVar()
                    continue
                }
                sum += if (subtract) -num1 else num1
                if (subtract) subtract = false
                shouldBeNumber = false
            } else {
                if (char == '-' || char == '+') {
                    if (char == '-') for (char2 in num) if (char2 == '-') subtract = !subtract
                    shouldBeNumber = true
                } else {
                    print = false
                    invalidExp()
                    continue
                }
            }
        }
        if (print) println(sum)
    }

    private fun memoryAdd(value: String) {
        val values = value.replace(" ", "").split('=').toTypedArray()
        val sequence: CharRange = 'a'..'z'
        if (values.size > 2) invalidAssign() else {
            for (char in values[0]) if (!sequence.contains(char.toLowerCase())) {
                invalidID()
                return
            }
            when {
                isNumber(values[1]) -> NUMBERS[values[0]] = values[1].toInt()
                memoryGet(values[1]) != null -> NUMBERS[values[0]] = memoryGet(values[1]) ?: 0
                else -> invalidAssign()
            }
        }
    }

    private fun memoryGet(value: String): Int? = if (NUMBERS.containsKey(value)) NUMBERS[value] else null

    private fun command(command: String) = if (command == "/help") help() else unknownCMD()

    private fun help() {
        println(
            "The program can add and subtract numerous numbers.\n" +
                    "Examples: 4 + 4 - 5 + 6 + -5"
        )
    }

    private fun unknownCMD() = println("Unknown command")

    private fun unknownVar() = println("Unknown variable")

    private fun invalidExp() = println("Invalid expression")

    private fun invalidAssign() = println("Invalid assignment")

    private fun invalidID() = println("Invalid identifier")

    private fun isNumber(number: String) = number.toIntOrNull() != null
}

fun main() {
    SmartCalculator.run()
}