package org.learningconcurrency.ch1

object Pair {
  def unapply[P, Q](pair: Pair[P, Q]): Option[(P, Q)] = {
    Some((pair.first, pair.second))
  }
}

class Pair[P, Q](val first: P, val second: Q)


