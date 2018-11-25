package org.learningconcurrency.ch2

import org.learningconcurrency._
import org.learningconcurrency.ch2.SynchronizedNesting.Account
import org.learningconcurrency.ch2.SynchronizedDeadLock._
import org.scalatest.{Matchers, WordSpecLike}

class SynchronizedDeadLockSpec
  extends WordSpecLike
    with Matchers {

  "SynchronizedDeadLock" must {
    "finish without deadlock when using send method" in {
      val a = new Account("Jack", 1000)
      val b = new Account("Jill", 2000)
      val t1 = thread {
        for (_ <- 0 until 100) send(a, b, 1)
      }
      val t2 = thread {
        for (_ <- 0 until 100) send(b, a, 1)
      }
      t1.join()
      t2.join()
      log(s"a = ${a.money}, b = ${b.money}")
    }

    "finish without deadlock when using sendAll method" in {
      val accNum = 100
      val sources =
        (for (i <- 1 to accNum)
          yield new Account(s"acc$i", i)).toSet

      val target = new Account("target", 200)

      val expectedAmount = 200 + (1 + accNum) / 2.0 * accNum

      sendAll(sources, target)

      target.money should be(expectedAmount)
    }
  }

}
