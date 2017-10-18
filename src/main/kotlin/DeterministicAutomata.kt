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

  override fun isFinalState(currentState: String): Boolean {
    return finalState == currentState
  }
}