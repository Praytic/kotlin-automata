
import java.util.HashMap
import kotlin.collections.HashSet
import kotlin.collections.component1
import kotlin.collections.component2

class Operation {


  fun makeIteration(noDetermAutomate: Automata): Automata {
    val newStates = cloneMap(noDetermAutomate.transitions).map {
      it.key to it.value.toMutableMap()
    }.toMap().toMutableMap()

    for (finState in noDetermAutomate.finalStates) {
      for (stState in noDetermAutomate.initialStates) {
        newStates[finState]?.putAll(newStates[stState]
            ?: throw Exception("No transitions for [$stState] key."))
            ?: throw Exception("No transitions for [$finState] key.")
      }
    }

    val nameOfNewState = (findMaxKey(newStates) + 1).toString()
    newStates.put(nameOfNewState, HashMap())

    val newStartStates = noDetermAutomate.initialStates.toMutableSet()
    newStartStates.add(nameOfNewState)

    val newFinishStates = noDetermAutomate.finalStates.toMutableSet()
    newFinishStates.add(nameOfNewState)
    val newAutomate = IndeterminateAutomata(
        initialStates = newStartStates,
        finalStates = newFinishStates,
        transitions = newStates
    )
    return deleteNotUsedStates(newAutomate)
  }

  fun makeConcatination(secondAutomate: Automata, firstAutomate: Automata): Automata {
    val newautomata = rename(firstAutomate, secondAutomate)

    val newStates = cloneMap(firstAutomate.transitions).map {
      it.key to it.value.toMutableMap()
    }.toMap().toMutableMap()
    val secondStates = cloneMap(newautomata.transitions).map {
      it.key to it.value.toMutableMap()
    }.toMap().toMutableMap()

    for (key in secondStates.keys) {
      newStates.put(key, secondStates[key]
          ?: throw Exception("No transitions for [$key] key."))
    }

    for (finState in firstAutomate.finalStates) {
      for (stState in newautomata.initialStates) {
        for (entry in newautomata.transitions.get(stState)?.entries
              ?: throw Exception("No transitions for [$stState] key.")) {
          newStates[finState]?.put(entry.key, entry.value)
        }
      }
    }

    val newFinishStates = newautomata.finalStates.toMutableSet()
    for (state in newautomata.initialStates) {
      if (newautomata.finalStates.contains(state)) {
        newFinishStates.addAll(firstAutomate.finalStates)
        break
      }
    }

    val newAutomate = IndeterminateAutomata(
        initialStates = firstAutomate.initialStates,
        transitions = newStates,
        finalStates = newFinishStates)
    return deleteNotUsedStates(newAutomate)
  }

  fun makeUnion(firstAutomate: Automata,
      secondAutomate: Automata): Automata {
    val newautomata = rename(firstAutomate, secondAutomate)
    val newStates = cloneMap(firstAutomate.transitions)
    val secondStates = cloneMap(newautomata.transitions)
    newStates.putAll(secondStates)

    val newStartStates = firstAutomate.initialStates.toMutableSet()
    newStartStates.addAll(newautomata.initialStates)

    val newFinishStates = firstAutomate.finalStates.toMutableSet()
    newFinishStates.addAll(newautomata.finalStates)

    val newAutomate = IndeterminateAutomata(
        initialStates = newStartStates,
        transitions = newStates,
        finalStates = newFinishStates)
    return deleteNotUsedStates(newAutomate)

  }

  fun deleteNotUsedStates(automate: Automata): Automata {

    val keys = HashMap<String, Int>()
    for (key in automate.transitions.keys) {
      for (subMapKey in automate.transitions.get(key)?.keys
            ?: throw Exception("No transitions for [$key] key.")) {
        for (value in automate.transitions.get(key)?.get(subMapKey)
              ?: throw Exception("No transitions for [$key] or [$subMapKey] key.")) {
          if (value != key) {
            if (keys.containsKey(value)) {
              var count = keys[value]!!
              keys.put(value, ++count)
            } else
              keys.put(value, 1)
          }
        }
      }
    }
    for (state in automate.initialStates) {
      if (keys.containsKey(state)) {
        var count = keys[state]!!
        keys.put(state, ++count)
      } else
        keys.put(state, 1)
    }

    val notUsedStates = java.util.ArrayList<String>()
    for (key in automate.transitions.keys) {
      if (!keys.containsKey(key)) {
        notUsedStates.add(key)
      }
    }
    val newTransitions = automate.transitions.toMutableMap()
    val newFinalStates = automate.finalStates.toMutableSet()
    for (key in notUsedStates) {
      newTransitions.remove(key)
      newFinalStates.remove(key)
    }
    return IndeterminateAutomata(
        name = automate.name,
        priority = automate.priority,
        alphabet = automate.alphabet,
        initialStates = automate.initialStates, 
        transitions = newTransitions, 
        finalStates = newFinalStates)
  }

  private fun cloneMap(oldMap: Map<String, Map<String, Set<String>>>): MutableMap<String, Map<String, Set<String>>> {
    val newMap = HashMap<String, Map<String, Set<String>>>()

    for (firstKey in oldMap.keys) {
      newMap.put(firstKey, cloneSubMap(oldMap[firstKey] ?:
          throw Exception("No transitions for [$firstKey] key.")))
    }
    return newMap
  }

  private fun cloneSubMap(stringListMap: Map<String, Set<String>>): Map<String, Set<String>> {
    val map = HashMap<String, Set<String>>()
    for (secondKey in stringListMap.keys) {
      val list = mutableSetOf<String>()
      for (str in stringListMap[secondKey] ?:
          throw Exception("No transitions for [$secondKey] key.")) {
        list.add(str)
      }
      map.put(secondKey, list)
    }
    return map
  }


  private fun findMaxKey(map: Map<String, Map<String, Set<String>>>): Int {
    var key = 0
    for (string in map.keys) {
      if (Integer.parseInt(string) > key)
        key = Integer.parseInt(string)
    }
    return key
  }

  private fun rename(firstAutomate: Automata, secondAutomate: Automata): Automata {
    val pos = findMaxKey(firstAutomate.transitions)
    return renameStates(secondAutomate, pos + 1)
  }

  private fun renameStates(automate: Automata, firstPosition: Int): Automata {
    val state = automate.transitions.map { (key, value) ->
      key to value.map { (key, value) ->
        key to value.toMutableSet()
      }.toMap().toMutableMap()
    }.toMap().toMutableMap()

    val startState = automate.initialStates.toMutableSet()
    val finishState = automate.finalStates.toMutableSet()

    var index = firstPosition
    val states = HashSet<String>(state.keys)
    for (oldState in states) {
      while (states.contains(index.toString())) {
        index++
      }
      if (!states.contains(index.toString())) {
        val newState = index.toString()
        renameInStates(state, oldState, newState)
        renameOutStates(state, oldState, newState)
        renameList(startState, oldState, newState)
        renameList(finishState, oldState, newState)
        index++
      }
    }
    return IndeterminateAutomata(
        name = automate.name,
        priority = automate.priority,
        alphabet = automate.alphabet,
        finalStates = finishState,
        initialStates = startState,
        transitions = state
    )
  }

  private fun renameList(list: MutableSet<String>,
      oldState: String,
      newState: String) {
    if (list.contains(oldState)) {
      list.remove(oldState)
      list.add(newState)
    }
  }

  private fun renameOutStates(state: MutableMap<String, MutableMap<String, MutableSet<String>>>,
      oldState: String,
      newState: String) {
    val value = state[oldState] ?:
        throw Exception("No transitions for [$oldState] key.")
    state.remove(oldState)
    state.put(newState, value)
  }

  private fun renameInStates(state: MutableMap<String, MutableMap<String, MutableSet<String>>>,
      oldState: String,
      newState: String) {
    for (map in state.values) {
      for ((_, value) in map) {
        if (value.contains(oldState)) {
          value.remove(oldState)
          value.add(newState)
        }
      }
    }
  }
}
