class DeterministicAutomaton(
    alphabet: Array<String>,
    initialState: String,
    finalState: String,
    transitions: Map<String, Map<String, String>>,
    priority: Int)
  : Automation(alphabet, priority, initialState, arrayOf(finalState),
               transitions.map {
                 it.key to it.value.map {
                   it.key to arrayOf(it.value)
                 }.toMap()
               }.toMap()) {

  val finalState: String get() = super.finalStates[0]
  val transition: Map<String, Map<String, String>> get() = super.transitions.map {
    it.key to it.value.map {
      it.key to it.value[0]
    }.toMap()
  }.toMap()

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
        return arrayOf<String>(transition[condition]?.get(signal) ?: throw Exception())
      }
      if (signal.matches("\\w".toRegex()))
        if (transitions[condition]?.containsKey("\\W") ?: false) {
          return arrayOf<String>(transition[condition]?.get("\\W") ?: throw Exception())
        }
      if (signal.matches("\\d".toRegex()))
        if (transitions[condition]?.containsKey("\\D") ?: false) {
          return arrayOf<String>(transition[condition]?.get("\\D") ?: throw Exception())
        }
    }
    return arrayOf()
  }

}