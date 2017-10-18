
import java.util.*

class Controler {

  // Возвращает удовл. ли строка автомату
  fun <T> task1(automaton: Automation, inputString: String): Boolean {
    val line = inputString.split("".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    var S: Array<String> = arrayOf(automaton.initialState)
    var newS: Array<String> = arrayOf()
    for (i in line.indices) {
      if (automaton.containsABC(line[i]) && S.isNotEmpty()) {
        for (j in S.indices) {
          newS = concatArray(newS, automaton.getTable(S[j], line[i]))
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
      if (automaton.containsABC(line[i])) {
        for (j in S.indices) {
          newS = concatArray(newS, automaton.getTable(S[j], line[i]))
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

  fun task3(inputString: String): Array<StringBuilder> {
    val automates = initialListAutomations()
    val results = initialStrings()
    var index = 0
    while (index < inputString.length) {
      var finalIndexAutomation = -1
      var finalN = -1
      for (indexAutomation in automates.indices.reversed()) {
        val n = f(automates[indexAutomation], inputString, index)
        if (n != -1) {
          if (finalIndexAutomation == -1 || n > finalN ||
              n == finalN && automates[indexAutomation].priority > automates[finalIndexAutomation].priority) {
            finalIndexAutomation = indexAutomation
            finalN = n
          }
        }
      }
      if (finalIndexAutomation != -1) {
        results[finalIndexAutomation].append(parseString(inputString.substring(
            index,
            index + finalN)) + "|")
        index += finalN
      } else
        index++
    }
    return results
  }

  private fun initialListAutomations(): ArrayList<Automation> {
    val automates = ArrayList<Automation>()
    automates.add(read2("input/com.txt", 1))
    automates.add(read2("input/str.txt", 2))
    automates.add(read2("input/ws.txt", 3))
    automates.add(read2("input/sing3.txt", 4))
    automates.add(read2("input/sing2.txt", 5))
    automates.add(read2("input/sing1.txt", 6))
    automates.add(read2("input/op_eg.txt", 7))
    automates.add(read2("input/op.txt", 8))
    automates.add(read2("input/real.txt", 10))
    automates.add(read2("input/id.txt", 9))
    automates.add(read2("input/kw.txt", 11))
    return automates
  }

  private fun initialStrings(): Array<StringBuilder> {
    return arrayOf(StringBuilder("com: "),
                   StringBuilder("str: "),
                   StringBuilder("ws: "),
                   StringBuilder(";: "),
                   StringBuilder("): "),
                   StringBuilder("(: "),
                   StringBuilder("op_eg: "),
                   StringBuilder("op: "),
                   StringBuilder("real: "),
                   StringBuilder("id: "),
                   StringBuilder("kw: "))
  }

  private fun parseString(str: String): String {
    when (str) {
      "\t" -> return "\\t"
      "\n" -> return "\\n"
      "\r" -> return "\\r"
      else -> return str
    }
  }

  private fun concatArray(a: Array<String>,
      b: Array<String>): Array<String> {
    return a + b
  }
}
