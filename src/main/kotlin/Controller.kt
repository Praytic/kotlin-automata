/**
 * Возвращает удовлетворяет ли строка [inputString] автомату [automata].
 */
fun task1(automata: Automata, inputString: String): Boolean {
  var currentStates = automata.initialStates.toList()
  var nextStates = mutableSetOf<String>()
  for (symbol in inputString) {
    if (automata.alphabetContains(symbol.toString()) && currentStates.isNotEmpty()) {
      for (j in currentStates.indices) {
        nextStates.addAll(automata.getNextStates(currentStates[j], symbol.toString()))
      }
      currentStates = nextStates.toList()
      nextStates = mutableSetOf()
    } else return false
  }
  return currentStates.any { automata.isFinalState(it) }
}

/**
 * Находит все подстроки в строке [inputString], удовлетворяющие автомату [automata].
 */
fun task2(automata: Automata, inputString: String): List<String> {
  val result = arrayListOf<String>()
  var index = 0
  while (index < inputString.length) {
    val n = f(automata, inputString, index)
    if (n > 0) {
      result += inputString.substring(index, index + n)
      index += n
    } else index++
  }
  return result
}

/**
 * Возвращает длину подстроки в строке [inputString] удовлетворяющей автомату [automata]
 * начиная с индекса [index].
 */
fun f(automata: Automata, inputString: String, index: Int): Int {
  var currentStates = automata.initialStates
  var nextStates = mutableSetOf<String>()
  var result = 0
  for (i in index until inputString.length) {
    if (automata.alphabetContains(inputString[i].toString())) {
      currentStates.forEach {
        nextStates.addAll(automata.getNextStates(it, inputString[i].toString()))
      }
    }
    if (nextStates.isNotEmpty()) {
      currentStates = nextStates
      nextStates = mutableSetOf()
      if (currentStates.any { automata.isFinalState(it) }) {
        result = i - index + 1
      }
    } else break
  }
  return result
}
