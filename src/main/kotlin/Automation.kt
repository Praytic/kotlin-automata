abstract class Automation(
    val alphabet: Array<String>,
    val priority: Int,
    val begin: Array<String>) {

  abstract fun containsABC(signal: String): Boolean
  abstract fun containsEnd(condition: String): Boolean
  abstract fun getTable(condition: String, signal: String): Array<String>
}
