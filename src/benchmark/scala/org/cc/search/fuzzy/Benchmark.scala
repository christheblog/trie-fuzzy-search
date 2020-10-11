package org.cc.search.fuzzy

import scala.collection.parallel.ParSeq
import scala.concurrent.ExecutionContext.Implicits._

// Benchmark Trie search, anagram map search and naive levensthein search
object Benchmark extends App {

  // Dictionary contains 466467
  val DictFile = "./src/main/resources/dictionary.txt"

  val DictContent = io.Source
    .fromFile(DictFile)
    .getLines()
    .toList


  val Dict = {
    val (trie, duration) = timing { DictContent.foldLeft(Trie.empty)(Trie.add(_,_)) }
    println(s"Trie built in $duration ms")
    trie
  }
  val AnaDict = {
    val (anaMap, duration) = timing { DictContent.foldLeft(AnagramMap.empty)(AnagramMap.add(_,_)) }
    println(s"AnagramMap built in $duration ms")
    anaMap
  }

  val size = Trie.size(Dict)
  val contains = Trie.contains(Dict) _
  val maxDistance = 4
  val trie = TrieSearch.find(Dict)(_, maxDistance)
  val anaMap = AnagramMap.find(AnaDict) _
  val leven = Levenshtein.find(DictContent)(_, maxDistance)
  val parLeven = Levenshtein.parFind(DictContent)(_, maxDistance)

  println(s"Dictionary size is ${size}")

  def timing[T](block: => T): (T,Long) = {
    val start = System.currentTimeMillis
    val res = block
    val end = System.currentTimeMillis
    (res, end - start)
  }

  def printf(algo: String, res: (Seq[String], Long)): Unit =
    println(s"$algo search (${res._2} ms) : ${res._1.size} : ${res._1.sorted}")

  def parPrintf(algo: String, res: (ParSeq[String], Long)): Unit =
    println(s"$algo search (${res._2} ms) : ${res._1.size} : ${res._1.seq.sorted}")

  println()
  List("alphbaet","managemnt","platfrm","concurency","pastels","shoemaker","brogue","ancien","regrtful")
    .foreach { word =>
      println(s"$word")
      printf("Trie", timing { trie(word).map(_._2).toList })
      printf("AnagramMap", timing { anaMap(word) })
      printf("Naive Levensthtein", timing { leven(word) })
      parPrintf("Parallel Levensthtein", timing { parLeven(word) })
      println("-"*50)
    }

}
