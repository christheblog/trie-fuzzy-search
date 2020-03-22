package orgcc.search.fuzzy

object LoadDictionary extends App {

  val DictFile = "./src/main/resources/dictionary.txt"

  val DictContent = io.Source
    .fromFile(DictFile)
    .getLines()
    .toList
  val Dict = DictContent.foldLeft(Trie.empty)(Trie.add(_,_))
  val AnaDict = DictContent.foldLeft(AnagramMap.empty)(AnagramMap.add(_,_))

  val size = Trie.size(Dict)
  val contains = Trie.contains(Dict) _

  val trieSearch = Trie.fuzzySearch(Dict)(_, 2)
  val anaMap = AnagramMap.fuzzySearch(AnaDict) _
  val leven = Levenshtein.fuzzySearch(DictContent)(_, 2)

  println(s"Dictionary size is ${size}")
//   println(s"Dictionary check: ${DictContent.filterNot(contains(_))}")
//   println(s"Dictionary check: ${DictContent.forall(fuzzyContains(_,0).size==1)}")

  def timing[T](block: => T): (T,Long) = {
    val start = System.currentTimeMillis
    val res = block
    val end = System.currentTimeMillis
    (res, end - start)
  }

  def printf(algo: String, res: (List[String], Long)): Unit =
    println(s"$algo search (${res._2} ms) : ${res._1.size} : ${res._1.sorted}")
  println()
  List("alphbaet","managemnt","platfrm","concurency","pastels")
    .foreach { word =>
      println(s"$word")
      printf("Trie", timing { trieSearch(word) })
      printf("AnagramMap", timing { anaMap(word) })
      printf("Naive Levensthtein", timing { leven(word) })
      println()
      println(s"Diff = ${leven(word).toSet -- trieSearch(word).toSet}")
      println("-"*50)
    }

}
