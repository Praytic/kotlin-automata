class DeterministicAutomata(
    alphabet: String,
    initialState: String,
    finalState: String,
    transitions: Map<String, Map<Char, String>>)
  : Automata(alphabet, initialState, arrayOf(finalState),
             transitions.map {
                 it.key to it.value.map {
                   it.key to arrayOf(it.value)
                 }.toMap()
               }.toMap()) {

  val finalState: String get() = super.finalStates[0]
  val transition: Map<String, Map<Char, String>> get() = super.transitions.map {
    it.key to it.value.map {
      it.key to it.value[0]
    }.toMap()
  }.toMap()

  override fun isFinalState(currentState: String): Boolean {
    return finalState == currentState
  }

  override fun getNextStates(currentState: String, signal: Char): Array<String> {
    if (transitions.containsKey(currentState)) {
      if (transitions[currentState]?.containsKey(signal) ?: false) {
        return arrayOf<String>(transition[currentState]?.get(signal) ?: throw Exception())
      }
      if (signal.isLetter())
        if (transitions[currentState]?.containsKey('W') ?: false) {
          return arrayOf<String>(transition[currentState]?.get('W') ?: throw Exception())
        }
      if (signal.isDigit())
        if (transitions[currentState]?.containsKey('D') ?: false) {
          return arrayOf<String>(transition[currentState]?.get('D') ?: throw Exception())
        }
    }
    return arrayOf()
  }

}