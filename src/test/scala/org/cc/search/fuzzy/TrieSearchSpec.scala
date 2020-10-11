package org.cc.search.fuzzy

import org.scalatest.flatspec.AnyFlatSpec

class TrieSearchSpec extends AnyFlatSpec {
  
  import TrieSearch.find
  
  "Performing a fuzzy search on a Trie" should "with a tolerance of 0 is equivalent to a standard search" in  {
    val empty = Trie.empty
    val trie = Trie.add(empty, "alphabet")
    assert(find(trie)("alphabet", tolerance = 0).toList === List((0,"alphabet")), "The trie should contain 'alphabet'")
    assert(find(trie)("Alphabet", tolerance = 0).isEmpty, "The trie should NOT contain 'Alphabet'")
    assert(find(trie)("alphaBet", tolerance = 0).isEmpty, "The trie should NOT contain 'alphaBet'")
  }

  it should "NOT yield words that are not within the tolerance given" in  {
    val empty = Trie.empty
    val trie = Trie.add(empty, "alphabet")
    assert(find(trie)("Alphabet", tolerance = 0).isEmpty)
    assert(find(trie)("AlphBAet", tolerance = 2).isEmpty)
  }

  it should "yield all words from the Trie within the tolerance given" in  {
    val empty = Trie.empty
    val trie = Trie.add(empty, "alphabet")
    assert(find(trie)("alphabet"  , tolerance = 0).toList == List((0,"alphabet")), "search for 'alphabet' with tol=1 should return 'alphabet'")
    assert(find(trie)("Alphabet"  , tolerance = 1).toList == List((1,"alphabet")), "search for 'Alphabet' with tol=1 should return 'alphabet'")
    assert(find(trie)("AlphBAet"  , tolerance = 3).toList == List((3,"alphabet")), "search for 'AlphBAet' with tol=3 should return 'alphabet'")
    assert(find(trie)("alphabeta" , tolerance = 1).toList == List((1,"alphabet")), "search for 'alphabeta' with tol=1 should return 'alphabet'")
    assert(find(trie)("aalphabet" , tolerance = 1).toList == List((1,"alphabet")), "search for 'aalphabet' with tol=1 should return 'alphabet'")
    assert(find(trie)("lphabet"   , tolerance = 1).toList == List((1,"alphabet")), "search for 'lphabet' with tol=1 should return 'alphabet'")
    assert(find(trie)("alphaabet" , tolerance = 1).toList == List((1,"alphabet")), "search for 'aalphaabet' with tol=1 should return 'alphabet'")
    assert(find(trie)("alphaabeta", tolerance = 2).toList == List((2,"alphabet")), "search for 'alphaabeta' with tol=2 should return 'alphabet'")
    assert(find(trie)("Aalphabet" , tolerance = 1) .toList== List((1,"alphabet")), "search for 'Aalphabet' with tol=1 should return 'alphabet'")
  }

  it should "yield all contained words within the tolerance given - 1" in  {
    val empty = Trie.empty
    val trie = Trie.add(empty, "alphabet","alphastar","alphabeta","analphabet")
    assert(Trie.contains(trie)("alphabet"))
    assert(Trie.contains(trie)("alphastar"))
    assert(Trie.contains(trie)("alphabeta"))
    assert(Trie.contains(trie)("analphabet"))

    assert(find(trie)("alphabet"  , tolerance = 0).toList == List((0,"alphabet")), "search for 'alphabet' with tol=1 should return 'alphabet'")
    assert(find(trie)("Alphabet"  , tolerance = 1).toList == List((1,"alphabet")), "search for 'Alphabet' with tol=1 should return 'alphabet'")
    assert(find(trie)("AlphBAet"  , tolerance = 3).toList == List((3,"alphabet")), "search for 'AlphBAet' with tol=3 should return 'alphabet'")
    assert(find(trie)("alphabeta" , tolerance = 0).toList == List((0,"alphabeta")), "search for 'alphabeta' with tol=1 should return 'alphabet'")
    assert(find(trie)("alphabetA" , tolerance = 1).toList == List((1,"alphabet"),(1,"alphabeta")), "search for 'alphabetA' with tol=1 should return 'alphabet'")
  }

  it should "yield all contained words within the tolerance given - 2" in  {
    val empty = Trie.empty
    val trie = Trie.add(empty, "management","manage")
    assert(Trie.contains(trie)("manage"))
    assert(Trie.contains(trie)("management"))

    assert(find(trie)("managemnt" , tolerance = 3).toList == List((1,"management"),(3,"manage")))
  }

  it should "yield all contained words within the tolerance given - 3" in  {
    val empty = Trie.empty
    val trie = Trie.add(empty, "astel","pastels")
    assert(Trie.contains(trie)("astel"))
    assert(Trie.contains(trie)("pastels"))

    assert(find(trie)("pastels" , tolerance = 2).toList == List((0,"pastels"),(2,"astel")))
  }

}
