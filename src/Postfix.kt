import java.util.*

object Postfix {
    private val STACK = Stack<String>()
    private val POSTFIX = mutableListOf<String>()
    private var HOLD = ""
    private var INDEX = 0
    private var LAST = 0
    private var INFIX = ""
    private const val SOME_OP = "*/^"
    private const val ALL_OP = "+-$SOME_OP"
    private const val OP_PLUS = "()$ALL_OP"

    fun convert(infix: String): Array<String> {
        reset()
        INFIX = infix.trim()
        while (INFIX.contains("  ")) INFIX = INFIX.replace("  ", " ")
        LAST = INFIX.lastIndex
        var shouldBeOperator = false

        while (!Error.triggered() && INDEX <= LAST) {
            if (INFIX[INDEX] == ' ') INDEX++
            if (shouldBeOperator) {
                val op = chkMultiOp()
                if (!Error.triggered()) operator(op.toString())
                shouldBeOperator = false
            } else {
                for (i in 1..4) {
                    if (Error.triggered() || INDEX > LAST) break
                    val char = INFIX[INDEX]
                    when (i) {
                        1 -> if (char == '(') leftParen()
                        2 -> if ("+-".contains(char)) chkMinus()
                        3 -> if (OP_PLUS.contains(char)) Error.invalidExp() else addNumber()
                        4 -> if (char == ')') rightParen()
                    }
                }
                shouldBeOperator = true
            }
        }
        if (Error.triggered()) POSTFIX.clear() else emptyStack()
        return POSTFIX.toTypedArray()
    }

    private fun reset() {
        if (STACK.isNotEmpty()) STACK.clear()
        if (POSTFIX.isNotEmpty()) POSTFIX.clear()
        if (HOLD != "") HOLD = ""
        if (INDEX != 0) INDEX = 0
    }

    private fun chkMultiOp(): Char {
        var count = 0
        var op = INFIX[INDEX]

        while (ALL_OP.contains(INFIX[INDEX])) {
            INDEX++
            count++
            if (chkIndexError()) return ' '
            if (ALL_OP.contains(INFIX[INDEX])) {
                if (INFIX[INDEX] != op || SOME_OP.contains(INFIX[INDEX])) {
                    Error.invalidExp()
                    return ' '
                }
            }
        }
        if (op == '-' && count % 2 == 0) op = '+'
        return op
    }

    private fun operator(op: String) {
        when (op) {
            "+", "-", "*", "/", "^" -> {
                if (STACK.isEmpty() || STACK.peek() == "(" || opIsGreater(op[0])) STACK.push(op) else {
                    while (STACK.isNotEmpty()) if (STACK.peek() != "(") POSTFIX.add(STACK.pop()) else break
                    STACK.push(op)
                }
            }
            else -> Error.invalidExp()
        }
    }

    private fun leftParen() {
        while (INFIX[INDEX] == '(') {
            STACK.push(INFIX[INDEX].toString())
            INDEX++
            if (chkIndexError()) return
        }
    }

    private fun chkMinus() {
        when (INFIX[INDEX]) {
            '+', '-' -> {
                if (INFIX[INDEX] == '-') HOLD += '-'
                INDEX++
                chkIndexError()
            }
        }
    }

    private fun addNumber() {
        while (INDEX <= LAST) {
            if (!OP_PLUS.contains(INFIX[INDEX]) && INFIX[INDEX] != ' ') {
                HOLD += INFIX[INDEX]
                INDEX++
            } else break
        }
        if (HOLD != "") {
            POSTFIX.add(HOLD)
            HOLD = ""
        }
    }

    private fun rightParen() {
        while (INFIX[INDEX] == ')') {
            var stop = false
            while (!stop && STACK.isNotEmpty()) {
                POSTFIX.add(STACK.pop())
                if (STACK.isNotEmpty()) if (STACK.peek() == "(") stop = true
            }
            if (STACK.isEmpty() && !stop) {
                Error.invalidExp()
                return
            } else STACK.pop()
            INDEX++
            if (INDEX > LAST) break
        }
    }

    private fun opIsGreater(char: Char): Boolean {
        when (char) {
            '+', '-' -> return false
            '*', '/' -> return when (STACK.peek()) {
                "+", "-" -> true
                else -> false
            }
            '^' -> return when (STACK.peek()) {
                "^" -> false
                else -> true
            }
        }
        return false
    }

    private fun emptyStack() {
        while (STACK.isNotEmpty()) {
            val temp = STACK.pop()
            if (temp == "(" || temp == ")") {
                Error.invalidExp()
                return
            }
            POSTFIX.add(temp)
        }
    }

    private fun chkIndexError(): Boolean {
        return if (INDEX > LAST) {
            Error.invalidExp()
            true
        } else false
    }
}