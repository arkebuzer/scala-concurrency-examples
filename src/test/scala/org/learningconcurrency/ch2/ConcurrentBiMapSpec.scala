package org.learningconcurrency.ch2

import org.learningconcurrency.thread
import org.scalatest.{Matchers, WordSpecLike}

import scala.util.Random

class ConcurrentBiMapSpec
  extends WordSpecLike
    with Matchers {

  "ConcurrentBiMap" must {
    "work without deadlocks when used by multiple threads" in {
      val biMap = new ConcurrentBiMap[Int, Int]

      val threadsNum = 10
      val elementsPerThread = 1000000

      val insertThreads =
        for (_ <- 1 to threadsNum)
          yield {
            thread {
              val rand = new Random()
              for (_ <- 1 to elementsPerThread) {
                biMap.put(rand.nextInt(), rand.nextInt())
              }
            }
          }

      insertThreads.foreach(_.join())

      val biMapSize = biMap.size
      println(s"biMapSize = $biMapSize")

      val biMapIterator = biMap.iterator

      val replaceThreads =
        for (_ <- 1 to threadsNum)
          yield {
            thread {
              for ((k, v) <- biMapIterator) {
                biMap.replace(k, v, v, k)
              }
            }
          }

      replaceThreads.foreach(_.join())

      biMap.size should be(biMapSize)
    }
  }

}
