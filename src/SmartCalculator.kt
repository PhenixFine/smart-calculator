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

    private fun sum(num1: Int, num2: Int) = num1 + num2

    fun run() {
        var input = scanner.nextLine()

        while (input != "/exit") {
            if (input != "") {
                println(
                    if (input.split(" ").size == 2) {
                        val strings = input.split(" ").toTypedArray()
                        val num1 = if (isNumber(strings[0])) strings[0].toInt() else getNum(strings[0] + strErrorNum)
                        val num2 = if (isNumber(strings[1])) strings[1].toInt() else getNum(strings[1] + strErrorNum)
                        sum(num1, num2)
                    } else input
                )
            }
            input = scanner.nextLine()
        }
        println("Bye!")
    }
}

fun main() {
    SmartCalculator.run()
}