abstract class Automata(
    val name: String,
    val priority: Int = 1,
    val alphabet: Set<String>,
    val initialStates: Set<String>,
    val finalStates: Set<String>,
    val transitions: Map<String, Map<String, Set<String>>>) {

  fun alphabetContains(signal: String) = alphabet.any {
    when(it) {
      "\\w" -> signal.all { it.isLetter() }
      "\\d" -> signal.all { it.isDigit() }
      "\\s" -> signal.all { it.isWhitespace() }
      "\\*" -> signal != "|"
      else -> it == signal
    }
  }

  fun getNextStates(currentState: String, signal: String): Set<String> {
    if (!transitions.containsKey(currentState)) return emptySet()

    var nextStates = setOf<String>()
    when {
      transitions[currentState]?.containsKey(signal) ?: false ->
        nextStates = transitions[currentState]?.get(signal) ?: emptySet()
      signal.all { it.isLetter() } && transitions[currentState]?.containsKey("\\w") ?: false ->
        nextStates = transitions[currentState]?.get("\\w") ?: emptySet()
      signal.all { it.isDigit() } && transitions[currentState]?.containsKey("\\d") ?: false ->
        nextStates = transitions[currentState]?.get("\\d") ?: emptySet()
      signal.all { it.isWhitespace() } && transitions[currentState]?.containsKey("\\s") ?: false ->
        nextStates = transitions[currentState]?.get("\\s") ?: emptySet()
      signal != "|" && transitions[currentState]?.containsKey("\\*") ?: false ->
        nextStates = transitions[currentState]?.get("\\*") ?: emptySet()
    }
    return nextStates
  }

  fun isFinalState(currentState: String) = finalStates.any { it == currentState }

  override fun toString(): String {
    return name
  }
}
