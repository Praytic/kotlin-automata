import com.google.gson.Gson
import java.io.File

fun main(args: Array<String>) {
  val automata = Gson()
      .fromJson(File("input/task1.json").reader(), Automata::class.java)
  automate(automata)
  println("Done.")
}

fun automate(automata: Automata) {
  println("Start automation for automata: $automata")
  // Process automata algorithm
  val resultantState = processAlgorithm(automata)
  println("Resultant state: $resultantState")
  // Validate current state
  validateState(resultantState, automata.finalState)
}

private fun processAlgorithm(automata: Automata): Set<String> {
  val currentState = automata.initialState
  for (symbol in automata.alphabet) {
    val newState = currentState
        .map { state ->
          val transition = automata.transitions[state] ?:
              throw Exception("State is not presented in the matrix of " +
                                  "transitions: $state")
          transition[symbol] ?:
              throw Exception("Symbol is not presented in the" +
                                  "transition: $symbol")
        }
        .flatten()
    println("New state for symbol [$symbol] is: $newState")
    currentState.addAll(newState)
  }
  return currentState
}

fun validateState(state: Set<String>, finalState: Set<String>) {
  if (finalState.any { state.contains(it) })
    throw Exception("Automata has stopped with non-final state.")
}

data class Automata(
    val alphabet: String,
    val initialState: MutableSet<String>,
    val finalState: Set<String>,
    val transitions: Map<String, Map<Char, Set<String>>>) {

  override fun toString(): String {
    return "Automata(\nalphabet='$alphabet',\ninitialState=$initialState,\n" +
        "finalState=$finalState,\ntransitions=$transitions)"
  }
}