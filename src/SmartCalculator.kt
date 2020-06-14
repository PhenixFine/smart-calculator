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
        val str1 = scanner.next()
        val str2 = scanner.next()
        val num1 = if (isNumber(str1)) str1.toInt() else getNum(str1 + strErrorNum)
        val num2 = if (isNumber(str2)) str2.toInt() else getNum(str2 + strErrorNum)
        println(sum(num1, num2))
    }
}

fun main() {
    SmartCalculator.run()
}