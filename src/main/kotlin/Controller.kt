
import javax.xml.bind.DatatypeConverter.parseString

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
  var S = arrayOf(automata.initialState)
  var newS: Array<String> = arrayOf()
  var result = 0
  for (i in index until inputString.length) {
    if (automata.alphabetContains(inputString[i])) {
      for (j in S.indices) {
        newS += automata.getNextStates(S[j], inputString[i])
      }
    }
    //System.out.println(newS[0]);
    if (newS.isNotEmpty()) {
      S = newS
      newS = arrayOf()
      for (item in S) {
        if (automata.isFinalState(item)) {
          result = i - index + 1
        }
      }
    } else break
  }
  return if (result != 0) result else -1
}

/**
 * Находит все подстроки в строке [inputString], являющиеся вещественынми числами.
 */
fun task2(automaton: Automata, inputString: String): String {
  var n: Int
  val result = StringBuilder()
  var index = 0
  while (index < inputString.length) {
    n = f(automaton, inputString, index)
    if (n != -1) {
      result.append(parseString(inputString.substring(index, index + n)) + "|")
      index += n
    } else index++
  }
  return result.toString()
}