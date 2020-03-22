package org.cc.search.fuzzy

import org.scalatest.flatspec.AnyFlatSpec

class TrieSpec extends AnyFlatSpec {

  import orgcc.search.fuzzy._

//  "A Trie" should "never contains the empty string" in  {
//    val empty = Trie.empty
//    val trie = Trie.add(empty, "")
//    assert(!Trie.contains(trie)(""), "The trie should NEVER contain teh empty string")
//  }
//
//  "Adding a non empty word to a trie" should "make the trie contain the word" in  {
//    val empty = Trie.empty
//    assert(!Trie.contains(empty)("alphabet"), "An empty trie should not contain a word")
//    val trie = Trie.add(empty, "alphabet")
//    assert(Trie.contains(trie)("alphabet"), "The trie should contain 'alphabet'")
//    assert(!Trie.contains(trie)("alphabe"), "The trie should NOT contain 'alphabe'")
//    assert(!Trie.contains(trie)("alphabeta"), "The trie should NOT contain 'alphabeta'")
//  }
//
//  "Adding several words to a trie" should "make the trie contain all the words" in  {
//    val empty = Trie.empty
//    val trie = Trie.add(empty, "king","queen","knight","rook","bishop","pawn")
//    assert(!Trie.contains(trie)("alphabet"), "The trie should contain 'alphabet'")
//    val trieContains= Trie.contains(trie) _
//    List("king","queen","knight","rook","bishop","pawn").foreach { w =>
//      assert(trieContains(w), s"Trie should contain $w")
//    }
//  }
//
//  "Contains on a trie" should "be case sensitive" in  {
//    val empty = Trie.empty
//    val trie = Trie.add(empty, "alphabet")
//    assert(Trie.contains(trie)("alphabet"), "The trie should contain 'alphabet'")
//    assert(!Trie.contains(trie)("Alphabet"), "The trie should NOT contain 'Alphabet'")
//    assert(!Trie.contains(trie)("alphaBet"), "The trie should NOT contain 'alphaBet'")
//  }
//
//  "Adding several times a word to a Trie" should "not alter its behaviour" in  {
//    val empty = Trie.empty
//    val trie = Trie.add(empty, "alphabet", "alphabet", "alphabet", "alphabet", "alphabet")
//    assert(Trie.contains(trie)("alphabet"), "The trie should contain 'alphabet'")
//    assert(!Trie.contains(trie)("Alphabet"), "The trie should NOT contain 'Alphabet'")
//    assert(!Trie.contains(trie)("alphaBet"), "The trie should NOT contain 'alphaBet'")
//  }
//
//  "Performing a fuzzy search on a Trie" should "with a tolerance of 0 is equivalent to a standard search" in  {
//    val empty = Trie.empty
//    val trie = Trie.add(empty, "alphabet")
//    assert(Trie.fuzzySearch(trie)("alphabet", tolerance = 0) == List("alphabet"), "The trie should contain 'alphabet'")
//    assert(Trie.fuzzySearch(trie)("Alphabet", tolerance = 0).isEmpty, "The trie should NOT contain 'Alphabet'")
//    assert(Trie.fuzzySearch(trie)("alphaBet", tolerance = 0).isEmpty, "The trie should NOT contain 'alphaBet'")
//  }
//
//    "Performing a fuzzy search on a Trie" should "NOT yield words that are not within the tolerance given" in  {
//      val empty = Trie.empty
//      val trie = Trie.add(empty, "alphabet")
//      assert(Trie.fuzzySearch(trie)("Alphabet", tolerance = 0).isEmpty)
//      assert(Trie.fuzzySearch(trie)("AlphBAet", tolerance = 2).isEmpty)
//    }
//
//  "Performing a fuzzy search on a Trie" should "yield all words from the Trie within the tolerance given" in  {
//    val empty = Trie.empty
//    val trie = Trie.add(empty, "alphabet")
//        assert(Trie.fuzzySearch(trie)("alphabet"  , tolerance = 0) == List("alphabet"), "search for 'alphabet' with tol=1 should return 'alphabet'")
//        assert(Trie.fuzzySearch(trie)("Alphabet"  , tolerance = 1) == List("alphabet"), "search for 'Alphabet' with tol=1 should return 'alphabet'")
//        assert(Trie.fuzzySearch(trie)("AlphBAet"  , tolerance = 3) == List("alphabet"), "search for 'AlphBAet' with tol=3 should return 'alphabet'")
//        assert(Trie.fuzzySearch(trie)("alphabeta" , tolerance = 1) == List("alphabet"), "search for 'alphabeta' with tol=1 should return 'alphabet'")
//        assert(Trie.fuzzySearch(trie)("aalphabet" , tolerance = 1) == List("alphabet"), "search for 'aalphabet' with tol=1 should return 'alphabet'")
//        assert(Trie.fuzzySearch(trie)("lphabet"   , tolerance = 1) == List("alphabet"), "search for 'lphabet' with tol=1 should return 'alphabet'")
//        assert(Trie.fuzzySearch(trie)("alphaabet" , tolerance = 1) == List("alphabet"), "search for 'aalphaabet' with tol=1 should return 'alphabet'")
//        assert(Trie.fuzzySearch(trie)("alphaabeta", tolerance = 2) == List("alphabet"), "search for 'alphaabeta' with tol=2 should return 'alphabet'")
//        assert(Trie.fuzzySearch(trie)("Aalphabet" , tolerance = 1) == List("alphabet"), "search for 'Aalphabet' with tol=1 should return 'alphabet'")
//  }
//
//  "Performing a fuzzy search on a Trie" should "yield all contained words within the tolerance given - 1" in  {
//    val empty = Trie.empty
//    val trie = Trie.add(empty, "alphabet","alphastar","alphabeta","analphabet")
//    assert(Trie.contains(trie)("alphabet"))
//    assert(Trie.contains(trie)("alphastar"))
//    assert(Trie.contains(trie)("alphabeta"))
//    assert(Trie.contains(trie)("analphabet"))
//
//    assert(Trie.fuzzySearch(trie)("alphabet"  , tolerance = 0) == List("alphabet"), "search for 'alphabet' with tol=1 should return 'alphabet'")
//    assert(Trie.fuzzySearch(trie)("Alphabet"  , tolerance = 1) == List("alphabet"), "search for 'Alphabet' with tol=1 should return 'alphabet'")
//    assert(Trie.fuzzySearch(trie)("AlphBAet"  , tolerance = 3) == List("alphabet"), "search for 'AlphBAet' with tol=3 should return 'alphabet'")
//    assert(Trie.fuzzySearch(trie)("alphabeta" , tolerance = 0) == List("alphabeta"), "search for 'alphabeta' with tol=1 should return 'alphabet'")
//    assert(Trie.fuzzySearch(trie)("alphabetA" , tolerance = 1) == List("alphabet","alphabeta"), "search for 'alphabetA' with tol=1 should return 'alphabet'")
//   }
//
//  "Performing a fuzzy search on a Trie" should "yield all contained words within the tolerance given - 2" in  {
//    val empty = Trie.empty
//    val trie = Trie.add(empty, "management","manage")
//    assert(Trie.contains(trie)("manage"))
//    assert(Trie.contains(trie)("management"))
//
//    assert(Trie.fuzzySearch(trie)("managemnt" , tolerance = 3) == List("manage","management"))
//  }

  "Performing a fuzzy search on a Trie" should "yield all contained words within the tolerance given - 3" in  {
    val empty = Trie.empty
    val trie = Trie.add(empty, "astel","pastels")
    assert(Trie.contains(trie)("astel"))
    assert(Trie.contains(trie)("pastels"))

    assert(Trie.fuzzySearch(trie)("pastels" , tolerance = 2) == List("astel","pastels"))
  }

}
