import com.google.gson.Gson
import java.io.File

fun main(args: Array<String>) {
  File("input/input_task4.txt").readLines().forEach {
    val automata = parseRegularExpression(it)
    File("output/$automata.json").writeText(Gson().toJson(automata))
  }
}

fun replaceWithEscapeSymbols(str: String): String {
  var newStr = ""
  for (chr in str) {
    newStr += when(chr) {
      '\t' -> "\\t"
      ' ' -> "\\s"
      '\r' -> "\\t"
      '\n' -> "\\n"
      '\b' -> "\\b"
      else -> chr
    }
  }
  return newStr
}

fun parseRegularExpression(str: String): Automata {
  val lex = str.split(':')
  val tokenName = lex[0]
  val priority = lex[1]
  val regularExpression = lex[2]
  val alphabet = setOf<String>()
  val initialStates = setOf<String>()
  val finalStates = setOf<String>()
  val transitions = mapOf<String, Map<String, Set<String>>>()
  return IndeterminateAutomata(
      tokenName,
      priority.toInt(),
      alphabet,
      initialStates,
      finalStates,
      transitions)
}