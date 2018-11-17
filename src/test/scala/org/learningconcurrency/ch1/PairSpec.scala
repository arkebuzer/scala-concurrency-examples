package org.learningconcurrency.ch1

import org.scalatest.{Matchers, WordSpecLike}

class PairSpec
  extends WordSpecLike
    with Matchers {
  "Pair" must {
    "work with match statement" in {
      val pair = new Pair("Alice", 21)

      val result =
        pair match {
          case Pair(name, age) => (name, age)
        }
      result should be(("Alice", 21))
    }
  }
}