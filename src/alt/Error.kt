package alt

private var TRIGGERED = false

private fun printError(string: String) {
    TRIGGERED = true
    println(string)
}

fun errReset() {
    TRIGGERED = false
}

fun errTrig() = TRIGGERED

fun errUnknownCMD() = printError("Unknown command")

fun errUnknownVar() = printError("Unknown variable")

fun errInvalidExp() = printError("Invalid expression")

fun errInvalidAssign() = printError("Invalid assignment")

fun errInvalidID() = printError("Invalid identifier")

fun errNegExponent() = printError("negative exponent is not supported")

fun errCalcTooLarge() = printError("Expression is too large to calculate")

fun errZeroDiv() = printError("Cannot divide by zero")