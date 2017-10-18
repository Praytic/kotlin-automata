
import java.util.*

class IndeterminateAutomaton(
    alphabet: Array<String>,
    initialState: String,
    finalState: Array<String>,
    transitions: SortedMap<String, SortedMap<String, Array<String>>>)
  : Automation(alphabet, initialState, finalState, transitions) {

  override fun containsEnd(condition: String) = finalStates.any { it == condition }

  override fun getTable(condition: String, signal: String): Array<String> {
    if (transitions.containsKey(condition)) {
      if (transitions[condition]?.containsKey(signal) ?: false)
        return transitions[condition]?.get(signal) ?: arrayOf()
      if (signal.matches("\\w".toRegex())) {
        if (transitions[condition]?.containsKey("\\W") ?: false)
          return transitions[condition]?.get("\\W") ?: arrayOf()
      }
      if (signal.matches("\\d".toRegex())) {
        if (transitions[condition]?.containsKey("\\D") ?: false)
          return transitions[condition]?.get("\\D") ?: arrayOf()
      }
    }
    return arrayOf()
  }
}
