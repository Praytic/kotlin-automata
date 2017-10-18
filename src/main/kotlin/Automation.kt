abstract class Automation(
    val alphabet: Array<String>,
    val initialState: String,
    open val finalStates: Array<String>,
    val transitions: Map<String, Map<String, Array<String>>>) {
  
  fun alphabetContains(signal: String) = alphabet.any {
    when(it) {
      "\\W" -> signal.matches("\\w".toRegex())
      "\\D" -> signal.matches("\\d".toRegex())
      else -> it == signal
    }
  }


  abstract fun containsEnd(condition: String): Boolean
  abstract fun getTable(condition: String, signal: String): Array<String>
}
