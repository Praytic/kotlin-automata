
import java.util.*

class IndeterminateAutomaton(
    priority: Int,
    alphabet: Array<String>,
    initialState: Array<String>,
    val finalState: Array<String>,
    val transitions: SortedMap<String, SortedMap<String, Array<String>>>) : Automation(alphabet, priority,
                                                                                       initialState) {

  // 3 задание
  private fun parseString(str: String): String {
    when (str) {
      "\\t" -> return "\t"
      "\\n" -> return "\n"
      "\\r" -> return "\r"
      else -> return str
    }
  }

  override fun containsABC(signal: String): Boolean {
    for (item in alphabet) {
      when (item) {
        "\\W" -> if (signal.matches("\\w".toRegex()))
          return true
        "\\D" -> if (signal.matches("\\d".toRegex()))
          return true
        else -> if (item == signal)
          return true
      }
    }
    return false
  }

  override fun containsEnd(condition: String): Boolean {
    for (item in finalState) {
      if (item == condition)
        return true
    }
    return false
  }

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
