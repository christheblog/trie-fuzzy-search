# Trie Fuzzy Search

This small pet project was an attempt to write some fuzzy string search to see what I would come-up with.

The goal was to come-up with some algorithms that could work as orthographic corrector - using a dictionary as reference.


## Anagram Map

For each word from a dictionary we compute a key which is identical for anagrams and words with consecutive double letters.

### Implementation
Implementation is very cheap, but there is a lot of mistakes in words that are missed. Not your ideal orthographic corrector.

### Performances
Unbeatable ... O(1) search. But the quality of the output is not always there.

## Trie search

Dictionary is encoded into a Trie. When looking up for a word, at each node we allow for potential differences - like skipping the character or replacing it/adding a new one.
This means the search can go down the tree, even if the target character is not found.

### Implementation
The algorithm find results similar to what a naive search using a Levensthein distance as the tolerance would bring.

The search is implemented as an iterator returning closest words first. It could be use to get the first 5 closest words for instance.
### Performances
Degrades exponentially. 

With a tolerance for errors of 2, the search is 50 times faster than a naive search.
When the tolerance is > 2, performances are degrading quickly - which makes it impractical.

From the benchmark :
```
Searching for: concurency
==============================================

Error tolerance (or distance) : 2
----------------------------------------------
Trie search (50 ms) : 5 : List(concludency, concumbency, concurrence, concurrency, noncurrency)
AnagramMap search (0 ms) : 1 : List(concurrency)
Naive Levensthtein search (2422 ms) : 5 : List(concludency, concumbency, concurrence, concurrency, noncurrency)
Parallel Levensthtein search (748 ms) : 5 : Vector(concludency, concumbency, concurrence, concurrency, noncurrency)

Error tolerance (or distance) : 3
----------------------------------------------
Trie search (262 ms) : 24 : List(coherency, coincidency, concernancy, concipiency, concludence, concludency, concordancy, concresce, concumbency, concurrence, ...
AnagramMap search (0 ms) : 1 : List(concurrency)
Naive Levensthtein search (2531 ms) : 24 : List(coherency, coincidency, concernancy, concipiency, concludence, concludency, concordancy, concresce, concumbency, concurrence, ...
Parallel Levensthtein search (755 ms) : 24 : Vector(coherency, coincidency, concernancy, concipiency, concludence, concludency, concordancy, concresce, concumbency, concurrence, ...
```

There is several reason for the degradation of the performances :
- The nature of the search
- Different sequence of operations can transform the search word in an identical way - so we end-up computing the same proposal several times. 
  The higher the error tolerance, the more problematic this phenomenon is.

## Benchmark
There is a small benchmark app using a dictionary +460k english words.
It is taken from https://github.com/dwyl/english-words (Copyright belongs to them)

Algorithms can be tested against a naive search computing a Levensthein distance for each word - and a parallel version of that naive search.
Levensthein distance calculation code was taken and adapted from https://searchcode.com/codesearch/view/79494918/
