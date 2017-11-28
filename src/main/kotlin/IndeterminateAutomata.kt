class IndeterminateAutomata(
    name: String = "Empty",
    priority: Int = 1,
    alphabet: Set<String> = setOf(),
    initialStates: Set<String> = setOf(),
    finalStates: Set<String> = setOf(),
    transitions: Map<String, Map<String, Set<String>>> = mapOf())
  : Automata(name, priority, alphabet, initialStates, finalStates, transitions) {

  constructor(): this(
    initialStates = setOf("1"),
    finalStates = setOf("1"),
    transitions = mapOf("1" to mapOf<String, Set<String>>())
  )
}
