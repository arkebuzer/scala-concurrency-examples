package org.learningconcurrency.ch2

import org.scalatest.{Matchers, WordSpecLike}

import scala.collection.mutable.ListBuffer

class PriorityTaskPoolSpec
  extends WordSpecLike
    with Matchers {

  "PriorityTaskPool" must {
    "execute tasks in order of their priority with one worker" in {
      val queue = new PriorityTaskPool(1)

      val messages = ListBuffer[String]()
      //messages.append("a")

      queue.asynchronous(1) {
        Thread.sleep(2000)
        messages.synchronized {
          messages.append("1")
        }
      }

      queue.asynchronous(1) {
        Thread.sleep(500)
        messages.synchronized {
          messages.append("2")
        }
      }

      queue.asynchronous(1) {
        Thread.sleep(500)
        messages.synchronized {
          messages.append("3")
        }
      }

      queue.asynchronous(3) {
        Thread.sleep(500)
        messages.synchronized {
          messages.append("4")
        }
      }

      queue.asynchronous(10) {
        Thread.sleep(500)
        messages.synchronized {
          messages.append("5")
        }
      }

      queue.asynchronous(2) {
        Thread.sleep(500)
        messages.synchronized {
          messages.append("6")
        }
      }

      Thread.sleep(5000)

      messages.toList should be(List("1", "5", "4", "6", "2", "3"))
    }


    "execute tasks in order of their priority with two workers" in {
      val queue = new PriorityTaskPool(2)

      val messages = ListBuffer[String]()
      //messages.append("a")

      queue.asynchronous(1) {
        Thread.sleep(2000)
        messages.synchronized {
          messages.append("1")
        }
      }

      queue.asynchronous(1) {
        Thread.sleep(2000)
        messages.synchronized {
          messages.append("2")
        }
      }

      queue.asynchronous(1) {
        Thread.sleep(500)
        messages.synchronized {
          messages.append("3")
        }
      }

      queue.asynchronous(3) {
        Thread.sleep(500)
        messages.synchronized {
          messages.append("4")
        }
      }

      queue.asynchronous(10) {
        Thread.sleep(500)
        messages.synchronized {
          messages.append("5")
        }
      }

      queue.asynchronous(2) {
        Thread.sleep(500)
        messages.synchronized {
          messages.append("6")
        }
      }

      Thread.sleep(5000)

      messages.toList should be(List("1", "2", "5", "4", "6", "3"))
    }
  }
}
