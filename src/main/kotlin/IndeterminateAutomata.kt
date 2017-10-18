class IndeterminateAutomata(
    alphabet: String,
    initialState: String,
    finalState: Array<String>,
    transitions: Map<String, Map<Char, Array<String>>>)
  : Automata(alphabet, initialState, finalState, transitions) {

  override fun isFinalState(currentState: String) = finalStates.any { it == currentState }

  override fun getNextStates(currentState: String, signal: Char): Array<String> {
    if (transitions.containsKey(currentState)) {
      if (transitions[currentState]?.containsKey(signal) ?: false)
        return transitions[currentState]?.get(signal) ?: arrayOf()
      if (signal.isLetter()) {
        if (transitions[currentState]?.containsKey('W') ?: false)
          return transitions[currentState]?.get('W') ?: arrayOf()
      }
      if (signal.isDigit()) {
        if (transitions[currentState]?.containsKey('D') ?: false)
          return transitions[currentState]?.get('D') ?: arrayOf()
      }
    }
    return arrayOf()
  }
}
