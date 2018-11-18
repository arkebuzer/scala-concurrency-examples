package org.learningconcurrency.ch2

import org.learningconcurrency.TestHelper
import org.scalatest.{Matchers, WordSpecLike}
import scala.math.pow

class Exercises2Spec
  extends WordSpecLike
    with Matchers {

  "Exercises2Spec.parallel" must {
    "execute two block parallel and return results" in {
      val sleepTime: Long = 1000
      val nanoSleepTime: Long = sleepTime * pow(10, 6).toLong

      def nameBlock: String = {
        Thread.sleep(sleepTime)
        "Alice"
      }

      def ageBlock: Int = {
        Thread.sleep(sleepTime)
        21
      }

      TestHelper.getExecutionTime {
        Exercises2.parallel(nameBlock, ageBlock) should be(("Alice", 21))
      } should be < 2 * nanoSleepTime
    }
  }

  "Exercises2Spec.periodically" must {
    "execute block every specified period" in {
      val duration: Long = 500

      var i = 0
      Exercises2.periodically(duration) {
        i += 1
      }

      Thread.sleep(duration * 3)

      i.synchronized {
        i should be(3)
      }
    }
  }

}
