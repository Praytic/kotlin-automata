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

  fun calculate(regexp: String): Automata {
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
        "\\*" -> stack.push(createNewAutomate("*"))
        "\\(" -> stack.push(createNewAutomate("("))
        "\\)" -> stack.push(createNewAutomate(")"))
        "\\|" -> stack.push(createNewAutomate("|"))
        else -> stack.push(createNewAutomate(literal))
      }
    }
    return stack.pop()
  }

  private fun createNewAutomate(x: String): Automata {
    val finalStates = mutableSetOf<String>()
    val states = mutableSetOf<String>()
    val transitions = mutableMapOf<String, Map<String, Set<String>>>()
    if (x == "Empty") {
      states.add("1")
      finalStates.add("1")
      transitions.put("1", mutableMapOf<String, Set<String>>())
      return IndeterminateAutomata(
          name = x,
          finalStates = finalStates,
          initialStates = states,
          transitions = transitions
      )
    }

    finalStates.add("2")
    val subList = mutableSetOf<String>()
    subList.add("2")
    val subList2 = mutableSetOf<String>()
    subList2.add("F")
    for (i in 1..2) {
      val subMap = mutableMapOf<String, Set<String>>()
      if (i == 1) {
        subMap.put(x, subList)
      }
      if (i == 2) subMap.put(x, subList2)
      transitions.put(i.toString(), subMap)
    }
    states.add("1")

    return IndeterminateAutomata(
        name = x,
        finalStates = finalStates,
        initialStates = states,
        transitions = transitions
    )
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