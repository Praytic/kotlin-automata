class IndeterminateAutomata(
    name: String,
    priority: Int,
    alphabet: Set<String>,
    initialStates: Set<String>,
    finalState: Set<String>,
    transitions: Map<String, Map<String, Set<String>>>)
  : Automata(name, priority, alphabet, initialStates, finalState, transitions) {
}
