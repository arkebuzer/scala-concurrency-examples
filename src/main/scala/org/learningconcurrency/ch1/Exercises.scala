package org.learningconcurrency.ch1

import scala.annotation.tailrec

object Exercises {

  def compose[A, B, C](g: B => C, f: A => B): A => C = (arg: A) => g(f(arg))

  def fuse[A, B](a: Option[A], b: Option[B]): Option[(A, B)] = {
    for {
      aVal <- a
      bVal <- b
    } yield (aVal, bVal)
  }

  def check[T](xs: Seq[T])(pred: T => Boolean): Boolean = {
    var checkPassed = true
    for (elem <- xs) {
      try {
        checkPassed = checkPassed && pred(elem)
      } catch {
        case _: Exception => checkPassed = false
      }
    }
    checkPassed
  }

  def permutation(x: String): Seq[String] = {
    @tailrec
    def recursivePermutation(x: String, permutations: Seq[String] = Seq("")): Seq[String] = {
      if (x.isEmpty) {
        permutations
      } else {
        val insertPermutations =
          for {
            p <- permutations
            i <- if (p.indices.isEmpty) Range(0, 1) else p.indices
          } yield p.substring(0, i) + x.head + p.substring(i, p.length)
        val appendPermutations = permutations.filter(!_.isEmpty).map(_ + x.head)

        val newPermutations = insertPermutations ++ appendPermutations
        recursivePermutation(x.drop(1), newPermutations)
      }
    }

    recursivePermutation(x)
  }

  def combinations(n: Int, xs: Seq[Int]): Iterator[Seq[Int]] = {
    @tailrec
    def recursiveCombinations(n: Int, xs: Seq[Int], combinations: Seq[Seq[Int]] = Seq(Seq())): Seq[Seq[Int]] = {
      if (n == 0) {
        combinations
      } else {
        val updatedCombinations =
          for {
            elem <- xs
            comb <- combinations if !comb.contains(elem)
          } yield elem +: comb

        recursiveCombinations(n - 1, xs, updatedCombinations)
      }
    }

    val allCombinations =
      if (n > xs.size) {
        Seq[Seq[Int]]()
      } else {
        recursiveCombinations(n, xs)
      }
    allCombinations.toIterator
  }

  def matcher(regex: String): PartialFunction[String, List[String]] = {
    new PartialFunction[String, List[String]] {
      override def isDefinedAt(x: String): Boolean = regex.r.findFirstMatchIn(x).isDefined

      override def apply(v1: String): List[String] = regex.r.findAllIn(v1).toList
    }
  }
}
