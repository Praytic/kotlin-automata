class IndeterminateAutomata(
    alphabet: String,
    initialState: String,
    finalState: Array<String>,
    transitions: Map<String, Map<Char, Array<String>>>)
  : Automata(alphabet, initialState, finalState, transitions) {

  override fun isFinalState(currentState: String) = finalStates.any { it == currentState }
}
