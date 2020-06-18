import java.util.*

object SmartCalculator {
    fun run() {
        val scanner = Scanner(System.`in`)
        var input = scanner.nextLine()

        while (input != "/exit") {
            if (input != "" && input != "/help") {
                println(
                    if (input.split(" ").size >= 2) {
                        val strings = input.split(" ").toTypedArray()
                        var sum = 0
                        var subtract = false

                        for (num in strings) {
                            var char = num[0]
                            if (num.length > 1 && char == '-' && char != num[1]) char = ' ' // if number is negative

                            if (char != '+' && char != '-') {
                                val num1 = if (isNumber(num)) num.toInt() else getNum(num)
                                sum += if (subtract) -num1 else num1
                                if (subtract) subtract = false
                            } else if (char == '-') {
                                for (char2 in num) if (char2 == '-') subtract = !subtract
                            }
                        }
                        sum
                    } else input
                )
            }
            if (input == "/help") help()
            input = scanner.nextLine()
        }
        println("Bye!")
    }

    private fun help() {
        println(
            "The program can add and subtract numerous numbers.\n" +
                    "Examples: 4 + 4 - 5 + 6 + -5"
        )
    }

    private fun getNum(text: String): Int {
        val strErrorNum = " was not a number, please try again: "
        var num = text

        do {
            print(num + strErrorNum)
            num = readLine()!!
        } while (!isNumber(num))

        return num.toInt()
    }

    private fun isNumber(number: String) = number.toIntOrNull() != null
}

fun main() {
    SmartCalculator.run()
}