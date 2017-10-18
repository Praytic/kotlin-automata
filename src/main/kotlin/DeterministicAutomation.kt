

class DeterministicAutomaton(
    alphabet: Array<String>,
    initialState: String,
    val finalState: String,
    val transitions: Map<String, Map<String, String>>,
    priority: Int) : Automation(alphabet, priority, arrayOf(initialState)) {

  override fun containsABC(signal: String): Boolean {
    for (item in alphabet) {
      when (item) {
        "\\W" -> if (signal.matches("\\w".toRegex()))
          return true
        "\\D" -> if (signal.matches("\\d".toRegex()))
          return true
        else -> if (item == signal)
          return true
      }
    }
    return false
  }

  override fun containsEnd(condition: String): Boolean {
    return finalState == condition
  }

  override fun getTable(condition: String, signal: String): Array<String> {
    if (transitions.containsKey(condition)) {
      if (transitions[condition]?.containsKey(signal) ?: false) {
        return arrayOf<String>(transitions[condition]?.get(signal) ?: throw Exception())
      }
      if (signal.matches("\\w".toRegex()))
        if (transitions[condition]?.containsKey("\\W") ?: false) {
          return arrayOf<String>(transitions[condition]?.get("\\W") ?: throw Exception())
        }
      if (signal.matches("\\d".toRegex()))
        if (transitions[condition]?.containsKey("\\D") ?: false) {
          return arrayOf<String>(transitions[condition]?.get("\\D") ?: throw Exception())
        }
    }
    return arrayOf()
  }

}