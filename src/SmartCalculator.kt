import java.util.*

object SmartCalculator {
    fun run() {
        val scanner = Scanner(System.`in`)
        var input = scanner.nextLine()

        while (input != "/exit") {
            if (input != "") {
                if (input[0] != '/') {
                    val strings = input.split(" ").toTypedArray()
                    var sum = 0
                    var shouldBeNumber = true
                    var subtract = false
                    var print = true

                    for (num in strings) {
                        var char = num[0]
                        if (num.length > 1 && (char == '-' || char == '+') && char != num[1]) char = ' '

                        if (shouldBeNumber) {
                            val num1 = if (isNumber(num)) num.toInt() else {
                                print = false
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
                                continue
                            }
                        }
                    }
                    if (print) println(sum) else invalid()
                } else command(input)
            }
            input = scanner.nextLine()
        }
        println("Bye!")
    }

    private fun command(command: String) = if (command == "/help") help() else unknown()

    private fun help() {
        println(
            "The program can add and subtract numerous numbers.\n" +
                    "Examples: 4 + 4 - 5 + 6 + -5"
        )
    }

    private fun unknown() = println("Unknown command")

    private fun invalid() = println("Invalid expression")

    private fun isNumber(number: String) = number.toIntOrNull() != null
}

fun main() {
    SmartCalculator.run()
}