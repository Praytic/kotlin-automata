abstract class Automata(
    val alphabet: Set<String>,
    val initialStates: Set<String>,
    val finalStates: Set<String>,
    val transitions: Map<String, Map<String, Set<String>>>) {

  fun alphabetContains(signal: String) = alphabet.any {
    when(it) {
      "/W" -> signal.all { it.isLetter() }
      "/D" -> signal.all { it.isDigit() }
      else -> it == signal
    }
  }

  fun getNextStates(currentState: String, signal: String): Set<String> {
    if (!transitions.containsKey(currentState)) return emptySet()

    var nextStates = setOf<String>()
    when {
      transitions[currentState]?.containsKey(signal) ?: false ->
        nextStates = transitions[currentState]?.get(signal) ?: emptySet()
      signal.all { it.isLetter() } && transitions[currentState]?.containsKey("/W") ?: false ->
        nextStates = transitions[currentState]?.get("/W") ?: emptySet()
      signal.all { it.isDigit() } && transitions[currentState]?.containsKey("/D") ?: false ->
        nextStates = transitions[currentState]?.get("/D") ?: emptySet()
    }
    return nextStates
  }

  abstract fun isFinalState(currentState: String): Boolean
}
