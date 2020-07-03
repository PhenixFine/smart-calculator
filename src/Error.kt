object Error {
    private var TRIGGERED = false

    private fun printError(string: String) {
        TRIGGERED = true
        println(string)
    }

    fun reset() {
        TRIGGERED = false
    }

    fun triggered() = TRIGGERED

    fun unknownCMD() = printError("Unknown command")

    fun unknownVar() = printError("Unknown variable")

    fun invalidExp() = printError("Invalid expression")

    fun invalidAssign() = printError("Invalid assignment")

    fun invalidID() = printError("Invalid identifier")

    fun negExponent() = printError("negative exponent is not supported")

    fun calcTooLarge() = printError("Expression is too large to calculate")

    fun zeroDiv() = printError("Cannot divide by zero")
}