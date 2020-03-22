package orgcc.search.fuzzy

object Levenshtein {

  def fuzzySearch(words: List[String])(word: String, maxDist: Int = 3) =
    words.filter(distance(word,_) <= maxDist)

  private[this] def distance(s1: String, s2: String): Int = {
    val dist = Array.tabulate(s2.length + 1, s1.length + 1) { (j, i) =>
      if (j == 0) i else if (i == 0) j else 0
    }
    @inline def minimum(i: Int*): Int = i.min

    for {
      j <- dist.indices.tail
      i <- dist(0).indices.tail
    } {
      dist(j)(i) =
        if (s2(j - 1) == s1(i - 1)) dist(j - 1)(i - 1)
        else minimum(dist(j - 1)(i) + 1, dist(j)(i - 1) + 1, dist(j - 1)(i - 1) + 1)
    }

    dist(s2.length)(s1.length)
  }
}
