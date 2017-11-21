abstract class Automata(
    val name: String = "",
    val priority: Int = 1,
    val alphabet: Set<String>,
    val initialStates: Set<String>,
    val finalStates: Set<String>,
    val transitions: Map<String, Map<String, Set<String>>>) {

  fun alphabetContains(signal: String) = alphabet.any {
    when(it) {
      "/W" -> signal.all { it.isLetter() }
      "/D" -> signal.all { it.isDigit() }
      "/S" -> signal.all { it.isWhitespace() }
      "/*" -> signal != "|"
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
      signal.all { it.isWhitespace() } && transitions[currentState]?.containsKey("/S") ?: false ->
        nextStates = transitions[currentState]?.get("/S") ?: emptySet()
      signal != "|" && transitions[currentState]?.containsKey("/*") ?: false ->
        nextStates = transitions[currentState]?.get("/*") ?: emptySet()
    }
    return nextStates
  }

  fun isFinalState(currentState: String) = finalStates.any { it == currentState }

  override fun toString(): String {
    return name
  }
}
