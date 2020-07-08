import java.lang.ArithmeticException
import java.math.BigInteger
import java.util.*

object SmartCalculator {
    private val NUMBERS = mutableMapOf<String, BigInteger>()
    private val STACK = Stack<BigInteger>()
    private val ERROR = Error()
    
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
            if (ERROR.triggered()) ERROR.reset()
        }
        println("Bye!")
    }

    private fun memoryAdd(value: String) {
        val values = value.replace(" ", "").split('=').toTypedArray()
        val sequence: CharRange = 'a'..'z'
        if (values.size > 2) ERROR.invalidAssign() else {
            for (char in values[0]) if (!sequence.contains(char.toLowerCase())) {
                ERROR.invalidID()
                return
            }
            when {
                isNumber(values[1]) -> NUMBERS[values[0]] = values[1].toBigInteger()
                memoryGet(values[1]) != null -> NUMBERS[values[0]] = memoryGet(values[1]) ?: 0.toBigInteger()
                else -> ERROR.invalidAssign()
            }
        }
    }

    private fun doMath(postfix: Array<String>) {
        if (postfix.isNotEmpty()) {
            for (element in postfix) {
                when (element) {
                    "+", "-", "*", "/", "^" -> {
                        val num2 = STACK.pop()
                        val num1 = STACK.pop()
                        when (element) {
                            "+", "-", "*" -> operation(element[0], num1, num2)
                            "/" -> divide(num1, num2)
                            "^" -> exponent(num1, num2)
                        }
                    }
                    else -> pushNumber(element)
                }
                if (ERROR.triggered()) return
            }
            println(STACK.last())
        }
    }

    private fun command(command: String) = if (command == "/help") help() else ERROR.unknownCMD()

    private fun isNumber(number: String) = number.toBigIntegerOrNull() != null

    private fun memoryGet(value: String): BigInteger? = if (NUMBERS.containsKey(value)) NUMBERS[value] else null

    private fun operation(op: Char, num1: BigInteger, num2: BigInteger) {
        try {
            var result = num1
            when (op) {
                '+' -> result += num2
                '*' -> result *= num2
                '-' -> result -= num2
            }
            STACK.push(result)
        } catch (e: ArithmeticException) {
            ERROR.calcTooLarge()
        }
    }

    private fun divide(num1: BigInteger, num2: BigInteger) {
        if (num2 == 0.toBigInteger()) ERROR.zeroDiv() else STACK.push((num1 / num2))
    }

    private fun exponent(num1: BigInteger, num2: BigInteger) {
        when {
            num2 < 0.toBigInteger() -> ERROR.negExponent()
            num2 > Int.MAX_VALUE.toBigInteger() -> ERROR.calcTooLarge()
            else -> try {
                val result = num1.pow(num2.toInt())
                STACK.push(result)
            } catch (e: ArithmeticException) {
                ERROR.calcTooLarge()
            }
        }
    }

    private fun pushNumber(string: String) {
        val num: BigInteger? = if (isNumber(string)) string.toBigInteger() else memoryGet(string)
        if (num == null) ERROR.unknownVar() else STACK.push(num)
    }

    private fun help() {
        println(
            "The program can add, subtract, multiply, and divide numerous very large whole numbers, save values and" +
                    " supports exponentiation. Example:\na = 3\nb = 2\na + 8 * ((4 + a ^ b) * b + 1) - 6 / (b + 1)"
        )
    }
}