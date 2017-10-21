class IndeterminateAutomata(
    alphabet: String,
    initialStates: Array<String>,
    finalState: Array<String>,
    transitions: Map<String, Map<Char, Array<String>>>)
  : Automata(alphabet, initialStates, finalState, transitions) {

  override fun isFinalState(currentState: String) = finalStates.any { it == currentState }
}
