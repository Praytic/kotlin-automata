abstract class Automata(
    val alphabet: String,
    val initialStates: Array<String>,
    val finalStates: Array<String>,
    val transitions: Map<String, Map<Char, Array<String>>>) {

  fun alphabetContains(signal: Char) = alphabet.any {
    when(it) {
      'W' -> signal.isLetter()
      'D' -> signal.isDigit()
      else -> it == signal
    }
  }

  fun getNextStates(currentState: String, signal: Char): Array<String> {
    if (!transitions.containsKey(currentState)) return arrayOf()

    var nextStates = arrayOf<String>()
    when {
      transitions[currentState]?.containsKey(signal) ?: false ->
        nextStates = transitions[currentState]?.get(signal) ?: arrayOf()
      signal.isLetter() && transitions[currentState]?.containsKey('W') ?: false ->
        nextStates = transitions[currentState]?.get('W') ?: arrayOf()
      signal.isDigit() && transitions[currentState]?.containsKey('D') ?: false ->
        nextStates = transitions[currentState]?.get('D') ?: arrayOf()
    }
    return nextStates
  }

  abstract fun isFinalState(currentState: String): Boolean
}
