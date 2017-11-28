/**
 * Возвращает удовлетворяет ли строка [inputString] автомату [automata].
 */
fun task1(automata: Automata, inputString: String): Boolean {
  var currentStates = automata.initialStates.toList()
  var nextStates = mutableSetOf<String>()
  for (symbol in inputString) {
    if (currentStates.isNotEmpty()) {
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
 * Проводит лексический анализ входящей строки [inputString] согласно автоматам [automatas]
 * и возвращает список найденных токенов в порядке их появления в строке.
 */
fun task3(automatas: List<Automata>, inputString: String): List<Pair<Automata, String>> {
  println("Starting task3... ")
  val result = mutableListOf<Pair<Automata, String>>()
  var index = 0
  while (index < inputString.length) {
    print("\n[$index] Inspecting substring [${replaceWithEscapeSymbols(inputString.substring(index))}]... ")
    val bestMatch = automatas.map { it to f(it, inputString, index) }
        .maxWith(Comparator { o1, o2 ->
          if (o1.second > o2.second) 1
          else if (o1.second < o2.second) -1
          else if (o1.first.priority > o2.first.priority) 1
          else if (o1.first.priority < o2.first.priority) -1
          else 0
        })

    if (bestMatch != null && bestMatch.second > 0) {
      val satisfyingString = replaceWithEscapeSymbols(
          inputString.substring(index, index + bestMatch.second))
      result += Pair(bestMatch.first, satisfyingString)
      index += bestMatch.second
      println("Found substring [$satisfyingString] with [${bestMatch.first}].")
    }
    else {
      index++
      println("No substrings were found.")
    }
  }
  println("End of task3.")
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
    currentStates.forEach {
      nextStates.addAll(automata.getNextStates(it, inputString[i].toString()))
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