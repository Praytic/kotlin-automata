import java.util.*

class Parser {
  private val operation: Operation

  init {
    operation = Operation()
  }

  private fun isDelimiter(token: String): Boolean {
    if (token.length != 1) return false
    for (i in 0 until delimiters.length) {
      if (token[0] == delimiters[i]) return true
    }
    return false
  }

  private fun isOperator(token: String): Boolean {
    for (i in 0 until operators.length) {
      if (token[0] == operators[i]) return true
    }
    return false
  }

  private fun priority(token: String): Int {
    if (token == "(") return 1
    if (token == "|") return 2
    if (token == "^") return 3
    return if (token == "*") 4 else 5
  }

  fun parse(infix: String, regExpCharacterList: String): List<String> {
    val postfix = ArrayList<String>()
    val stack = ArrayDeque<String>()
    var curr = ""
    var i = 0
    while (i < regExpCharacterList.length) {
      curr = regExpCharacterList[i].toString()
      if (curr == "\\") {
        postfix.add(curr + regExpCharacterList[i + 1])
        i += 1
      } else if (isDelimiter(curr)) {
        when (curr) {
          "(" -> stack.push(curr)
          ")" -> {
            while (stack.peek() != "(") {
              postfix.add(stack.pop())
              if (stack.isEmpty()) {
                println("Скобки не согласованы.")
                return postfix
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
      if (isOperator(stack.peek()))
        postfix.add(stack.pop())
      else {
        println("Скобки не согласованы.")
        flag = false
        return postfix
      }
    }
    return postfix
  }

  fun calculate(postfix: List<String>): Automata {
    val stack = ArrayDeque<Automata>()
    for (x in postfix) {
      when (x) {
        "|" -> stack.push(operation.makeUnion(stack.pop(), stack.pop()))
        "^" -> stack.push(operation.makeConcatination(stack.pop(), stack.pop()))
        "*" -> stack.push(operation.makeIteration(stack.pop()))
        "\\*" -> stack.push(createNewAutomate("*"))
        "?" -> stack.push(createEmptyAutomate())
        "\\(" -> stack.push(createNewAutomate("("))
        "\\)" -> stack.push(createNewAutomate(")"))
        "\\|" -> stack.push(createNewAutomate("|"))
        else -> stack.push(createNewAutomate(x))
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

  companion object {
    private val operators = "|*^"
    private val delimiters = "()" + operators
    var flag = true

    fun createEmptyAutomate(): Automata {
      return IndeterminateAutomata(
          initialStates = setOf("1"),
          finalStates = setOf("1"),
          transitions = mapOf("1" to mapOf<String, Set<String>>())
      )
    }
  }
}