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

  constructor(singleState: String): this(
    finalStates = mutableSetOf("2"),
    initialStates = mutableSetOf("1"),
    transitions = mutableMapOf(
    "1" to mutableMapOf(singleState to mutableSetOf("2")),
    "2" to mutableMapOf(singleState to mutableSetOf("F"))
    )
  )
}
