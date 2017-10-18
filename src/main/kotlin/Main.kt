
import java.io.BufferedReader
import java.io.FileReader

fun main(args: Array<String>) {
  println(task1(read2("input/real.txt"), "2.5asdasd 24,-24e2;sd4e-2.3"))
  println(task1(read2("input/real.txt"), "2.5"))
  println(task2(read2("input/real.txt"), "2.5asdasd 24,-24e2;sd4e-2.3"))
}

fun read1(way: String): DeterministicAutomata {
  val `in` = BufferedReader(FileReader(way))
  val begin = `in`.readLine().split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
  val end = `in`.readLine().split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]

  val abc = `in`.readLine()
  val table = mutableMapOf<String, MutableMap<Char, String>>()
  var s: String? = `in`.readLine()
  while (s != null) {
    val strTmp = s.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    table.put(strTmp[0], mutableMapOf<Char, String>())
    var item = 1
    while (item < strTmp.size) {
      table.get(strTmp[0])?.put(strTmp[item].single(), strTmp[item + 1])
      item += 2
    }
    s = `in`.readLine()
  }
  `in`.close()
  return DeterministicAutomata(abc, begin, end, table)
}

fun read2(way: String): IndeterminateAutomata {
  val `in` = BufferedReader(FileReader(way))
  val begin = `in`.readLine().split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
  val end = `in`.readLine().split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

  val abc = `in`.readLine()
  val table = mutableMapOf<String, MutableMap<Char, Array<String>>>()
  var s: String? = `in`.readLine()
  while (s != null) {
    val strExternal = s.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    table.put(strExternal[0], mutableMapOf<Char, Array<String>>())
    var item = 1
    while (item < strExternal.size) {
      val strInternal = strExternal[item + 1].split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
      table.get(strExternal[0])?.put(strExternal[item].single(), strInternal)
      item += 2
    }
    s = `in`.readLine()
  }
  `in`.close()
  return IndeterminateAutomata(abc, begin[0], end, table)
}