class DeterministicAutomata(
    alphabet: Set<String>,
    initialState: String,
    finalState: String,
    transitions: Map<String, Map<String, String>>)
  : Automata(alphabet, setOf(initialState), setOf(finalState),
             transitions.map {
                 it.key to it.value.map {
                   it.key to setOf(it.value)
                 }.toMap()
               }.toMap()) {

  val finalState: String get() = super.finalStates.single()

  override fun isFinalState(currentState: String): Boolean {
    return finalState == currentState
  }
}