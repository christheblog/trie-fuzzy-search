package org.cc.search.fuzzy

import scala.collection.immutable.{SortedMap, TreeMap}

object Trie {

  case class Trie(root: Root)

  // A trie node is either a root node, or a normal node
  // A normal node can finish encoding a word, even if it has children (ie is also considered a "leaf")
  // Note: This Trie implementation CANNOT contain the empty string (ie the root cannot be a leaf)
  sealed trait TrieNode

  case class Root(children: SortedMap[Char, Node]) extends TrieNode

  case class Node(char: Char,
                  leaf: Boolean,
                  children: SortedMap[Char, Node]) extends TrieNode


  def empty: Trie =
    Trie(root = Root(children = TreeMap.empty))

  def size(trie: Trie): Int =
    size(trie.root)

  private[this] def size(n: TrieNode): Int = n match {
    case Root(xs) => xs.values.map(size).sum
    case Node(_, false, xs) => xs.values.map(size).sum
    case Node(_, true, xs) => 1 + xs.values.map(size).sum
  }

  def isLeaf(n: TrieNode) = n match {
    case Root(xs) if xs.isEmpty => true
    case Node(_, true, _) => true
    case Node(_, _, xs) if xs.isEmpty => true
    case _ => false
  }

  def isRoot(n: TrieNode) = n match {
    case Root(_) => true
    case _ => false
  }

  def children(n: TrieNode): SortedMap[Char, Node] = n match {
    case Root(xs) => xs
    case Node(_, _, xs) => xs
  }

  def child(n: TrieNode)(forChar: Char): Option[Node] =
    children(n).get(forChar)

  def isChildLeaf(n: TrieNode)(forChar: Char): Boolean =
    child(n)(forChar).exists(_.leaf)

  def add(trie: Trie, words: String*): Trie =
    words.foldLeft(trie) { case (t, w) => add(t, w) }

  def add(trie: Trie, word: String): Trie =
    if (word.isEmpty) trie
    else {
      val (char, remainder) = (word.head, word.tail)
      val child = children(trie.root).getOrElse(word.head, Node(char, false, children = TreeMap.empty))
      val newRoot = Root(children = children(trie.root) + (char -> add(child.copy(leaf = child.leaf || remainder.isEmpty), remainder)))
      Trie(root = newRoot)
    }

  private[fuzzy] def add(node: Node, word: String): Node = (word.headOption, word.tail, node) match {
    case (None, _, node) => node
    case (Some(char), remainder, Node(x, leaf, children)) =>
      val child = children.getOrElse(char, Node(char, false, children = TreeMap.empty))
      Node(x, leaf, children = children + (char -> add(child.copy(leaf = child.leaf || remainder.isEmpty), remainder)))
  }

  def contains(trie: Trie)(word: String): Boolean =
    if (word.isEmpty) false
    else children(trie.root)
      .get(word.head)
      .exists(contains(_)(word.tail))

  private[fuzzy] def contains(node: Node)(word: String): Boolean = (word.headOption, word.tail, node) match {
    case (None, _, _) => false
    case (Some(char), "", node) => children(node).get(char).exists(_.leaf)
    case (Some(char), remainder, node) => children(node).get(char).exists(contains(_)(word.tail))
  }

}
