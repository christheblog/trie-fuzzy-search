package orgcc.search.fuzzy

object AnagramMap {

  type Key = String
  type Word = String
  case class AnagramMap(map: Map[Key, List[Word]])

  def empty: AnagramMap =
    AnagramMap(map = Map())

  def add(am: AnagramMap, word: Word): AnagramMap = {
    val key = computeKey(word)
    AnagramMap(am.map + (key -> (word :: am.map.getOrElse(key, Nil))))
  }

  def fuzzySearch(am: AnagramMap)(word: Word): List[Word] = {
    val key = computeKey(word)
    variations(key).flatMap(am.map.getOrElse(_, Nil)).toList
  }


  private[fuzzy] def computeKey(word: Word): Key =
    removeConsecutiveDuplicates(word).toSeq.sorted.unwrap

  private[fuzzy] def removeConsecutiveDuplicates(s: String): String =
    if(s.size < 2) s
    else if(s.charAt(0)==s.charAt(1)) removeConsecutiveDuplicates(s.substring(1))
    else s.head + removeConsecutiveDuplicates(s.tail)

  // Computes variations of a key of a word, to enlarge scope of proposals
  private[fuzzy] def variations(key: Key): Set[Key] = {
    Set(key)// ++ key.toList.map(c => key.filter(c !=_)).toSet
  }

}
