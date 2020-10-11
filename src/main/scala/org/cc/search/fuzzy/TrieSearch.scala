package org.cc.search.fuzzy

import org.cc.search.fuzzy.Trie._

import scala.annotation.tailrec
import scala.collection.mutable

object TrieSearch {

  type Distance = Int

  // Returns an iterator going down the trie, and returning closest words first
  def find(trie: Trie)(word: String, tolerance: Int): Iterator[(Distance, String)] =
    findAndExplain(trie)(word, tolerance)
      .map { case (dist, word, _) => (dist, word) } // removing logs of transformations

  // Returns an iterator going down the trie, and returning closest words first
  // A log of changes is attached to each word, explaining how we transform the target word into the one found in the trie
  // This provides an understanding of the distance.
  def findAndExplain(trie: Trie)(word: String, tolerance: Int): Iterator[(Distance, String, Logs)] =
    new FuzzySearchIterator(trie, word, tolerance)
      .distinctBy(_._2) // unfortunate, but needed because several transformations can lead to the same word


  // There is no immutable priority queue in standard scala ... so going for a mutable one
  type PQ[T] = mutable.PriorityQueue[T]
  type Remainder = String
  // TODO : create enum for changelog to be able to manipulate log and format explanation from them
  type Logs = List[String]

  // Ordering in the priority queue by distance asc and remainder string
  private[this] implicit val Ord = new Ordering[(Distance, TrieNode, Remainder, List[Char], Logs)] {
    override def compare(x: (Distance, TrieNode, Remainder, List[Char], Logs), y: (Distance, TrieNode, Remainder, List[Char], Logs)): Int = {
      y._1.compareTo(x._1) match {
        case 0 => x._3.compareTo(y._3)
        case n => n
      }
    }
  }

  protected[fuzzy] class FuzzySearchIterator(trie: Trie, word: String, tolerance: Int) extends Iterator[(Distance, String, Logs)] {
    private val queue = {
      val q = new PQ[(Distance, TrieNode, Remainder, List[Char], Logs)]()
      // Enqueuing the root to kick-off iteration
      q.enqueue((0, trie.root, word, List.empty, List.empty))
      q
    }

    private[this] val cl = (n: TrieNode) => children(n).toList

    @tailrec private[this] def nextOne(): Option[(Distance, String, Logs)] = {
      if (queue.isEmpty) None
      else {
        val (dist, node, rmnd, acc, logs) = queue.dequeue()
        if (rmnd.isEmpty && isLeaf(node)) {
          if (dist + 1 <= tolerance) cl(node).foreach { case (char, child) =>
            queue.enqueue((dist + 1, child, rmnd, char :: acc, s"+$char" :: logs))
          }
          Some((dist, acc.reverse.mkString(""), logs))
        } else if (rmnd.isEmpty && dist + 1 <= tolerance) {
          cl(node).foreach { case (char, child) => queue.enqueue((dist + 1, child, rmnd, char :: acc, s"+$char" :: logs)) }
          nextOne()
        }
        else if (rmnd.isEmpty) nextOne()
        else {
          val (head, tail) = (rmnd.head, rmnd.drop(1))
          if (isLeaf(node) && dist + rmnd.length <= tolerance) queue.enqueue((dist + rmnd.length, node, "", acc, "*" :: logs))
          if (dist + 1 <= tolerance) queue.enqueue((dist + 1, node, tail, acc, s"-$head" :: logs))
          cl(node).foreach { case (char, child) =>
            if (head == char) queue.enqueue((dist, child, tail, char :: acc, s"$char" :: logs))
            else if (dist + 1 <= tolerance) {
              queue.enqueue((dist + 1, child, tail, char :: acc, s"$head=>$char" :: logs))
              queue.enqueue((dist + 1, child, rmnd, char :: acc, s"+$char" :: logs))
            }
          }
          nextOne()
        }
      }
    }


    // Iterator implementation

    // This iterator's state.
    // Ideally we should be lazy at initialization to start computing only when the iterator is used
    var nextOccur = nextOne()

    override def hasNext: Boolean =
      nextOccur.nonEmpty

    override def next(): (Distance, String, Logs) = {
      val res = nextOccur.head
      nextOccur = nextOne()
      res
    }
  }

}
