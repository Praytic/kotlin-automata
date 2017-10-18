import java.io.BufferedReader
import java.io.FileReader
import java.util.*
import javax.xml.bind.DatatypeConverter.parseString

fun main(args: Array<String>) {
  println(Controler().task2(read2("input/real.txt", 1), "2.5asdasd 24"))
}

fun read1(way: String, priority: Int): DeterministicAutomaton {
  val `in` = BufferedReader(FileReader(way))
  val begin = `in`.readLine().split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
  val end = `in`.readLine().split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]

  val abc = `in`.readLine().split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
  val table: SortedMap<String, SortedMap<String, String>> = sortedMapOf()
  var s: String? = `in`.readLine()
  while (s != null) {
    val strTmp = s.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    table.put(strTmp[0], TreeMap<String, String>())
    var item = 1
    while (item < strTmp.size) {
      table.get(strTmp[0])?.put(strTmp[item], strTmp[item + 1])
      item += 2
    }
    s = `in`.readLine()
  }
  `in`.close()
  return DeterministicAutomaton(abc, begin, end, table, priority)
}

fun read2(way: String, priority: Int): IndeterminateAutomaton {
  val `in` = BufferedReader(FileReader(way))
  val begin = `in`.readLine().split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
  val end = `in`.readLine().split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

  val tmp = `in`.readLine().split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
  val abc = tmp.map { parseString(it) }.toTypedArray()
  val table: SortedMap<String, SortedMap<String, Array<String>>> = sortedMapOf()
  var s: String? = `in`.readLine()
  while (s != null) {
    val strExternal = s.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    table.put(strExternal[0], TreeMap<String, Array<String>>())
    var item = 1
    while (item < strExternal.size) {
      val strInternal = strExternal[item + 1].split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
      table.get(strExternal[0])?.put(parseString(strExternal[item]),
                                     strInternal)
      item += 2
    }
    s = `in`.readLine()
  }
  `in`.close()
  return IndeterminateAutomaton(priority, abc, begin, end, table)
}