import java.util.*

class Parser {

  fun toPostfixNotation(regexp: String): List<String> {
    val postfix = mutableListOf<String>()
    val stack = ArrayDeque<String>()
    val priority = { token: String -> when(token) {
      "(" -> 1
      "|" -> 2
      "^" -> 3
      "*" -> 4
      else -> 5
    } }
    var i = 0
    while (i < regexp.length) {
      val curr = regexp[i].toString()
      val isDelimiter = "()|*^".any { curr == it.toString() }
      if (curr == "\\") {
        postfix.add(curr + regexp[i + 1])
        i += 1
      } else if (isDelimiter) {
        when (curr) {
          "(" -> stack.push(curr)
          ")" -> {
            while (stack.peek() != "(") {
              postfix.add(stack.pop())
              if (stack.isEmpty()) {
                throw Exception("Brackets aren't consistent.")
              }
            }
            stack.pop()
          }
          else -> {
            while (!stack.isEmpty() && priority(curr) <= priority(stack.peek())) {
              postfix.add(stack.pop())
            }
            stack.push(curr)
          }
        }
      } else {
        postfix.add(curr)
      }
      i++
    }

    while (!stack.isEmpty()) {
      val isOperator = "|*^".any { stack.peek() == it.toString() }
      if (isOperator) postfix.add(stack.pop())
      else throw Exception("Brackets aren't consistent.")
    }
    return postfix
  }

  fun generateAutomata(regexp: String): Automata {
    val regexpWithConcat = addConcatinationOperators(regexp)
    val regexpPostfixed = toPostfixNotation(regexpWithConcat)
    val operation = Operation()
    val stack = ArrayDeque<Automata>()
    for (literal in regexpPostfixed) {
      when (literal) {
        "|" -> stack.push(operation.makeUnion(stack.pop(), stack.pop()))
        "^" -> stack.push(operation.makeConcatination(stack.pop(), stack.pop()))
        "*" -> stack.push(operation.makeIteration(stack.pop()))
        "?" -> stack.push(IndeterminateAutomata())
        "\\*" -> stack.push(IndeterminateAutomata("*"))
        "\\(" -> stack.push(IndeterminateAutomata("("))
        "\\)" -> stack.push(IndeterminateAutomata(")"))
        "\\|" -> stack.push(IndeterminateAutomata("|"))
        else -> stack.push(IndeterminateAutomata(literal))
      }
    }
    return stack.pop()
  }

  fun addConcatinationOperators(regular: String): String {
    var reg = ""
    for (i in 0..regular.length - 2) {
      if (regular[i] != '(' && regular[i] != '|' && regular[i] != '^' && regular[i] != '\\' &&
          regular[i + 1] != ')' && regular[i + 1] != '|' && regular[i + 1] != '*' || i > 1 && regular[i - 1] == '\\' && regular[i] == '|') {
        reg += "${regular[i]}^"
      }
      else reg += regular[i]
    }
    reg += regular.last()
    return reg
  }
}