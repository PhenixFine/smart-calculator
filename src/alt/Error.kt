package alt

class Error() {
    private var triggered = false

    private fun printError(string: String) {
        triggered = true
        println(string)
    }

    fun reset() {
        triggered = false
    }

    fun triggered() = triggered

    fun unknownCMD() = printError("Unknown command")

    fun unknownVar() = printError("Unknown variable")

    fun invalidExp() = printError("Invalid expression")

    fun invalidAssign() = printError("Invalid assignment")

    fun invalidID() = printError("Invalid identifier")

    fun negExponent() = printError("negative exponent is not supported")

    fun calcTooLarge() = printError("Expression is too large to calculate")

    fun zeroDiv() = printError("Cannot divide by zero")
}