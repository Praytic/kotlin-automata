/**
 * Возвращает удовлетворяет ли строка [inputString] автомату [automata].
 */
fun task1(automata: Automata, inputString: String): Boolean {
  var currentStates: Array<String> = arrayOf(automata.initialState)
  var nextStates = arrayOf<String>()
  for (symbol in inputString) {
    if (automata.alphabetContains(symbol) && currentStates.isNotEmpty()) {
      for (j in currentStates.indices) {
        nextStates += automata.getNextStates(currentStates[j], symbol)
      }
      currentStates = nextStates
      nextStates = arrayOf()
    } else return false
  }
  return currentStates.any { automata.isFinalState(it) }
}

/**
 * Находит все подстроки в строке [inputString], являющиеся вещественынми числами.
 */
fun task2(automaton: Automata, inputString: String): List<String> {
  val result = arrayListOf<String>()
  var index = 0
  while (index < inputString.length) {
    val n = f(automaton, inputString, index)
    if (n > 0) {
      result += inputString.substring(index, index + n)
      index += n
    } else index++
  }
  return result
}

/**
 * Эта функция выводит длину подстроки удовлетв. автомату. Если таковой нет,
 * то она выводит -1. Она проходит по циклу индексов. Если первый символ удв.
 * автомату (проверка подходит к алфавиту), тогда он достает переход куда и
 * достает новый переход, потом он меняет их местами (в S лежит куда перешел, а второй оьнуляет).
 * Потом проверяет если в S лежат сигналы заключетельные, тогда н сохраняет длину
 * строки, которую он прошел и сохраняет в результате.
 * @param automata
 * @param inputString
 * @param index
 * @return
 */
fun f(automata: Automata, inputString: String, index: Int): Int {
  var currentStates = arrayOf(automata.initialState)
  var nextStates = arrayOf<String>()
  var result = 0
  for (i in index until inputString.length) {
    if (automata.alphabetContains(inputString[i])) {
      currentStates.forEach {
        nextStates += automata.getNextStates(it, inputString[i])
      }
    }
    if (nextStates.isNotEmpty()) {
      currentStates = nextStates
      nextStates = arrayOf()
      if (currentStates.any { automata.isFinalState(it) }) {
        result = i - index + 1
      }
    } else break
  }
  return result
}
