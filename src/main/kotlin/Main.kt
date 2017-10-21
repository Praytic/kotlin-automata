import com.google.gson.Gson
import java.io.File

fun main(args: Array<String>) {
  val automata = Gson()
      .fromJson(File("input/task1.json").reader(), IndeterminateAutomata::class.java)
  println(task1(automata, "2.5asdasd 24,-24e2;sd4e-2.3"))
  println(task1(automata, "2.5"))
  println(task2(automata, "2.5asdasd 24,-24e2;sd4e-2.3"))
}