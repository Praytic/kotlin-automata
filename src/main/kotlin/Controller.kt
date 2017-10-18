
import javax.xml.bind.DatatypeConverter.parseString

/**
 * Возвращает удовлетворяет ли строка автомату.
 */
fun <T> task1(automaton: Automation, inputString: String): Boolean {
  val line = inputString.split("".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
  var S: Array<String> = arrayOf(automaton.initialState)
  var newS: Array<String> = arrayOf()
  for (i in line.indices) {
    if (automaton.alphabetContains(line[i]) && S.isNotEmpty()) {
      for (j in S.indices) {
        newS += automaton.getTable(S[j], line[i])
      }
      S = newS
      newS = arrayOf()
    } else
      return false
  }
  for (item in S) {
    if (automaton.containsEnd(item))
      return true
  }
  return false
}

/**
 * Эта функция выводит длину подстроки удовлетв. автомату. Если таковой нет,
 * то она выводит -1. Она проходит по циклу индексов. Если первый символ удв.
 * автомату (проверка подходит к алфавиту), тогда он достает переход куда и
 * достает новый переход, потом он меняет их местами (в S лежит куда перешел, а второй оьнуляет).
 * Потом проверяет если в S лежат сигналы заключетельные, тогда н сохраняет длину
 * строки, которую он прошел и сохраняет в результате.
 * @param automaton
 * @param inputString
 * @param index
 * @return
 */
fun f(automaton: Automation, inputString: String, index: Int): Int {
  println("inputString: $inputString, index: $index")
  val line = inputString.split("".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
  var S = arrayOf(automaton.initialState)
  var newS: Array<String> = arrayOf()
  var result = 0
  for (i in index until line.size) {
    if (automaton.alphabetContains(line[i])) {
      for (j in S.indices) {
        newS += automaton.getTable(S[j], line[i])
        println("newS: ${newS.toList()}")
      }
    }
    //System.out.println(newS[0]);
    if (newS.isNotEmpty()) {
      S = newS
      newS = arrayOf()
      for (item in S) {
        if (automaton.containsEnd(item)) {
          result = i - index + 1
          println("result: $result")
        }
      }
    } else break
  }
  return if (result != 0) result else -1
}

// Находит все подстроки в строке, являющиеся вещественынми числами.
fun task2(automaton: Automation, inputString: String): String {
  var n: Int
  val result = StringBuilder()
  var index = 0
  while (index < inputString.length) {
    n = f(automaton, inputString, index)
    println("n: $n")
    if (n != -1) {
      result.append(parseString(inputString.substring(index,
                                                      index + n)) + "|")
      index += n
    } else
      index++
    println("task2: $result")
  }
  return if (result.toString() != "")
    result.toString()
  else
    "Nothing"
}