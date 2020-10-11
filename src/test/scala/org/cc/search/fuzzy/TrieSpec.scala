package org.cc.search.fuzzy

import org.scalatest.flatspec.AnyFlatSpec

class TrieSpec extends AnyFlatSpec {

  "A Trie" should "never contains the empty string" in  {
    val empty = Trie.empty
    val trie = Trie.add(empty, "")
    assert(!Trie.contains(trie)(""), "The trie should NEVER contain the empty string")
  }

  "Adding a non empty word to a trie" should "make the trie contain the word" in  {
    val empty = Trie.empty
    assert(!Trie.contains(empty)("alphabet"), "An empty trie should not contain a word")
    val trie = Trie.add(empty, "alphabet")
    assert(Trie.contains(trie)("alphabet"), "The trie should contain 'alphabet'")
    assert(!Trie.contains(trie)("alphabe"), "The trie should NOT contain 'alphabe'")
    assert(!Trie.contains(trie)("alphabeta"), "The trie should NOT contain 'alphabeta'")
  }

  "Adding several words to a trie" should "make the trie contain all the words" in  {
    val empty = Trie.empty
    val trie = Trie.add(empty, "king","queen","knight","rook","bishop","pawn")
    assert(!Trie.contains(trie)("alphabet"), "The trie should contain 'alphabet'")
    val trieContains= Trie.contains(trie) _
    List("king","queen","knight","rook","bishop","pawn").foreach { w =>
      assert(trieContains(w), s"Trie should contain $w")
    }
  }

  "Contains on a trie" should "be case sensitive" in  {
    val empty = Trie.empty
    val trie = Trie.add(empty, "alphabet")
    assert(Trie.contains(trie)("alphabet"), "The trie should contain 'alphabet'")
    assert(!Trie.contains(trie)("Alphabet"), "The trie should NOT contain 'Alphabet'")
    assert(!Trie.contains(trie)("alphaBet"), "The trie should NOT contain 'alphaBet'")
  }

  "Adding several times a word to a Trie" should "not alter its behaviour" in  {
    val empty = Trie.empty
    val trie = Trie.add(empty, "alphabet", "alphabet", "alphabet", "alphabet", "alphabet")
    assert(Trie.contains(trie)("alphabet"), "The trie should contain 'alphabet'")
    assert(!Trie.contains(trie)("Alphabet"), "The trie should NOT contain 'Alphabet'")
    assert(!Trie.contains(trie)("alphaBet"), "The trie should NOT contain 'alphaBet'")
  }

}
