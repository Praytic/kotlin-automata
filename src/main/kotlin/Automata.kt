abstract class Automata(
    val alphabet: String,
    val initialState: String,
    val finalStates: Array<String>,
    val transitions: Map<String, Map<Char, Array<String>>>) {

  fun alphabetContains(signal: Char) = alphabet.any {
    when(it) {
      'W' -> signal.isLetter()
      'D' -> signal.isDigit()
      else -> it == signal
    }
  }

  abstract fun isFinalState(currentState: String): Boolean
  abstract fun getNextStates(currentState: String, signal: Char): Array<String>
}
