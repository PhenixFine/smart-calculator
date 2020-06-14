import java.util.*

object SmartCalculator {
    private val scanner = Scanner(System.`in`)
    private const val strErrorNum = " was not a number, please try again: "

    private fun getNum(text: String): Int {
        print(text)
        var num = readLine()!!
        while (!isNumber(num)) {
            print(num + strErrorNum)
            num = readLine()!!
        }
        return num.toInt()
    }

    private fun isNumber(number: String) = number.toIntOrNull() != null

    fun run() {
        var input = scanner.nextLine()

        while (input != "/exit") {
            if (input != "" && input != "/help") {
                println(
                    if (input.split(" ").size >= 2) {
                        val strings = input.split(" ").toTypedArray()
                        var sum1 = 0
                        for (num in strings) {
                            val num1 = if (isNumber(num)) num.toInt() else getNum(num + strErrorNum)
                            sum1 += num1
                        }
                        sum1
                    } else input
                )
            }
            if (input == "/help") println("The program calculates the sum of numbers")
            input = scanner.nextLine()
        }
        println("Bye!")
    }
}

fun main() {
    SmartCalculator.run()
}