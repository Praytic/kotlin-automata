class DeterministicAutomata(
    name: String,
    priority: Int,
    alphabet: Set<String>,
    initialState: String,
    finalStates: Set<String>,
    transitions: Map<String, Map<String, String>>)
  : Automata(name, priority, alphabet, setOf(initialState), finalStates,
             transitions.map {
                 it.key to it.value.map {
                   it.key to setOf(it.value)
                 }.toMap()
               }.toMap()) {

  val transition: Map<String, Map<String, String>> get() = super.transitions.map {
    it.key to it.value.map {
      it.key to it.value.single()
    }.toMap()
  }.toMap()
}