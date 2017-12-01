class DeterministicAutomata(
    name: String = "Empty",
    priority: Int = 1,
    alphabet: Set<String> = setOf(),
    initialState: String? = null,
    finalStates: Set<String> = setOf(),
    transitions: Map<String, Map<String, String>> = mapOf())
  : Automata(name,
             priority,
             alphabet,
             if (initialState != null) setOf(initialState) else setOf(),
             finalStates,
             transitions.map { it.key to it.value.map { it.key to setOf(it.value) }.toMap() }.toMap()) {

  val transition: Map<String, Map<String, String>> get() = super.transitions.map {
    it.key to it.value.map {
      it.key to it.value.single()
    }.toMap()
  }.toMap()
}