package org.learningconcurrency.ch1

import org.scalatest.{Matchers, WordSpecLike}

class ExercisesSpec
  extends WordSpecLike
    with Matchers {
  "Exercises.compose" must {
    "return correctly composed same type functions" in {
      val f = (x: Int) => x + 1
      val g = (x: Int) => x * 2

      val x = 5
      Exercises.compose(g, f)(x) should be(g(f(x)))
      Exercises.compose(f, g)(x) should be(f(g(x)))
    }

    "return correctly composed different type functions" in {
      val f = (x: Double) => x * 10
      val g: Double => String = (x: Double) => s"$x is a nice number"

      val x = 5
      Exercises.compose(g, f)(x) should be(g(f(x)))
      "Exercises.compose(f, g)(x)" shouldNot compile
    }
  }

  "Exercises.fuse" must {
    "return option containing tuple of its arguments values if they are not empty" in {
      val name = Some("Alice")
      val age = Some(21)

      val noName: Option[String] = None
      val noAge: Option[Int] = None

      Exercises.fuse(name, age) should be(Some((name.get, age.get)))
      Exercises.fuse(noName, age) should be(None)
      Exercises.fuse(name, noAge) should be(None)
      Exercises.fuse(noName, noAge) should be(None)
    }
  }

  "Exercises.check" must {
    "return true if all seq elements satisfies a predicate" in {
      val goodIntSeq = 1 until 40
      val goodDoubleSeq = Range.BigDecimal.apply(1.0, 100.0, 0.5)
      val badSeq = 0 until 10
      val badIntSeq = 1 until 100

      Exercises.check(badSeq)(40 / _ > 0) should be(false)
      Exercises.check(goodIntSeq)(40 / _ > 0) should be(true)
      Exercises.check(badIntSeq)(40 / _ > 0) should be(false)
      Exercises.check(goodDoubleSeq)(40 / _ > 0) should be(true)
    }
  }

  "Exercises.permutation" must {
    "return all string permutations" in {
      val emptyString = ""
      val shortString = "abc"
      val shortStringPermutations =
        Seq(
          "abc",
          "bac",
          "bca",
          "acb",
          "cab",
          "cba"
        )

      Exercises.permutation(emptyString) should be(Seq(""))

      val permutatedShortString = Exercises.permutation(shortString)
      permutatedShortString.toSet should be(shortStringPermutations.toSet)
      permutatedShortString.size should be(shortStringPermutations.size)
    }
  }

  "Exercises.combinations" must {
    "return all combinations of specified length" in {
      val emptySeq = Seq()
      val oneElemSeq = Seq(1)

      val someSeq = Seq(1, 4, 9, 16)
      val someSeqCombinationsOf2 =
        Set(
          Set(1, 4),
          Set(1, 9),
          Set(1, 16),
          Set(4, 9),
          Set(4, 16),
          Set(9, 16)
        )

      Exercises.combinations(1, emptySeq).toSeq should be(Seq())
      Exercises.combinations(1, oneElemSeq).toSeq should be(Seq(Seq(1)))

      val combinationsOf2OfSomeSeq = Exercises.combinations(2, someSeq)
      val combsOfSets = combinationsOf2OfSomeSeq.map(_.toSet).toSet

      combsOfSets should be(someSeqCombinationsOf2)
      combsOfSets.size should be(someSeqCombinationsOf2.size)
    }
  }

  "Exercises.matcher" must {
    "return list of matches or be undefined if there is no matches" in {
      val regex = "\\d+"
      val hasMatches = "1a22_333"
      val noMatches = "abc"

      Exercises.matcher(regex).isDefinedAt(noMatches) should be(false)
      Exercises.matcher(regex)(hasMatches) should be(List("1", "22", "333"))
    }
  }
}
