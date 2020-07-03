import java.math.BigInteger
import java.util.*

object SmartCalculator {
    private val NUMBERS = mutableMapOf<String, Long>()
    private val STACK = Stack<Long>()

    fun run() {
        val scanner = Scanner(System.`in`)
        var input = scanner.nextLine()

        while (input != "/exit") {
            if (input != "") {
                if (input[0] != '/') {
                    if (input.contains('=')) memoryAdd(input) else doMath(Postfix.convert(input))
                } else command(input)
            }
            input = scanner.nextLine()
            if (STACK.isNotEmpty()) STACK.clear()
            if (Error.triggered()) Error.reset()
        }
        println("Bye!")
    }

    private fun memoryAdd(value: String) {
        val values = value.replace(" ", "").split('=').toTypedArray()
        val sequence: CharRange = 'a'..'z'
        if (values.size > 2) Error.invalidAssign() else {
            for (char in values[0]) if (!sequence.contains(char.toLowerCase())) {
                Error.invalidID()
                return
            }
            when {
                isNumber(values[1]) -> NUMBERS[values[0]] = values[1].toLong()
                memoryGet(values[1]) != null -> NUMBERS[values[0]] = memoryGet(values[1]) ?: 0
                else -> Error.invalidAssign()
            }
        }
    }

    private fun doMath(postfix: Array<String>) {
        if (!Error.triggered()) {
            for (element in postfix) {
                when (element) {
                    "+", "-", "*", "/", "^" -> {
                        val num2 = STACK.pop().toLong()
                        val num1 = STACK.pop().toLong()
                        when (element) {
                            "+", "-", "*" -> operation(element[0], num1, num2)
                            "/" -> divide(num1, num2)
                            "^" -> exponent(num1, num2)
                        }
                    }
                    else -> pushNumber(element)
                }
                if (Error.triggered()) return
            }
            println(STACK.last())
        }
    }

    private fun command(command: String) = if (command == "/help") help() else Error.unknownCMD()

    private fun isNumber(number: String) = number.toLongOrNull() != null

    private fun memoryGet(value: String): Long? = if (NUMBERS.containsKey(value)) NUMBERS[value] else null

    private fun operation(op: Char, num1: Long, num2: Long) {
        var result = num1.toBigInteger()
        when (op) {
            '+' -> result += num2.toBigInteger()
            '*' -> result *= num2.toBigInteger()
            '-' -> result -= num2.toBigInteger()
        }
        if (!tooBigChk(result)) STACK.push(result.toLong())
    }

    private fun divide(num1: Long, num2: Long) {
        if (num2 == 0L) Error.zeroDiv() else STACK.push((num1 / num2))
    }

    private fun exponent(num1: Long, num2: Long) {
        when {
            num2 < 0 -> Error.negExponent()
            num2 > Int.MAX_VALUE.toLong() -> Error.calcTooLarge()
            num2 == 0L -> STACK.push(1L)
            else -> {
                var num3 = num1.toBigInteger()
                if (num2 > 1) {
                    repeat((num2 - 1).toInt()) {
                        num3 *= num1.toBigInteger()
                        if (tooBigChk(num3)) return
                    }
                }
                STACK.push(num3.toLong())
            }
        }
    }

    private fun pushNumber(string: String) {
        val num: Long? = if (isNumber(string)) string.toLong() else memoryGet(string)
        if (num == null) Error.unknownVar() else STACK.push(num.toLong())
    }

    private fun help() {
        println(
            "The program can add, subtract, multiply, and divide numerous whole numbers, save values and supports " +
                    "exponentiation. Example:\na = 3\nb = 2\na + 8 * ((4 + a^b) * b + 1) - 6 / (b + 1)"
        )
    }

    private fun tooBigChk(num: BigInteger): Boolean {
        return if (num > Long.MAX_VALUE.toBigInteger() || num < Long.MIN_VALUE.toBigInteger()) {
            Error.calcTooLarge()
            true
        } else false
    }
}