import com.google.gson.Gson
import java.io.File

fun main(args: Array<String>) {
  val automatas = arrayListOf<Automata>(
      Gson().fromJson(File("input/identificator.json").reader(), DeterministicAutomata::class.java),
      Gson().fromJson(File("input/keyword.json").reader(), IndeterminateAutomata::class.java),
      Gson().fromJson(File("input/integer_number.json").reader(), DeterministicAutomata::class.java),
      Gson().fromJson(File("input/real_number.json").reader(), IndeterminateAutomata::class.java),
      Gson().fromJson(File("input/open_bracket.json").reader(), DeterministicAutomata::class.java),
      Gson().fromJson(File("input/close_bracket.json").reader(), DeterministicAutomata::class.java),
      Gson().fromJson(File("input/space.json").reader(), DeterministicAutomata::class.java))

  val results = task3(automatas, "2.5asdaifs define if 24,-24e2;())(sd4e-2.3\t\n dep conlambdadac")
  println()
  println("Result:")
  for (result in results) {
    println(result)
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