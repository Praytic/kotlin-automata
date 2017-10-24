class IndeterminateAutomata(
    alphabet: Set<String>,
    initialStates: Set<String>,
    finalState: Set<String>,
    transitions: Map<String, Map<String, Set<String>>>)
  : Automata(alphabet, initialStates, finalState, transitions) {

  override fun isFinalState(currentState: String) = finalStates.any { it == currentState }
}
