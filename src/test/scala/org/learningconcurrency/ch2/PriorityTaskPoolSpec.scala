package org.learningconcurrency.ch2

import org.scalatest.{Matchers, WordSpecLike}

import scala.collection.mutable.ListBuffer

class PriorityTaskPoolSpec
  extends WordSpecLike
    with Matchers {

  "PriorityTaskPool" must {
    "execute tasks in order of their priority with one worker" in {
      val tasksPool = new PriorityTaskPool(1, 0)

      val messages = ListBuffer[String]()

      tasksPool.asynchronous(1) {
        Thread.sleep(2000)
        messages.synchronized {
          messages.append("1")
        }
      }

      tasksPool.asynchronous(1) {
        Thread.sleep(500)
        messages.synchronized {
          messages.append("2")
        }
      }

      tasksPool.asynchronous(1) {
        Thread.sleep(500)
        messages.synchronized {
          messages.append("3")
        }
      }

      tasksPool.asynchronous(3) {
        Thread.sleep(500)
        messages.synchronized {
          messages.append("4")
        }
      }

      tasksPool.asynchronous(10) {
        Thread.sleep(500)
        messages.synchronized {
          messages.append("5")
        }
      }

      tasksPool.asynchronous(2) {
        Thread.sleep(500)
        messages.synchronized {
          messages.append("6")
        }
      }

      Thread.sleep(5000)

      messages.toList should be(List("1", "5", "4", "6", "2", "3"))
    }


    "execute tasks in order of their priority with two workers" in {
      val tasksPool = new PriorityTaskPool(2, 0)

      val messages = ListBuffer[String]()

      tasksPool.asynchronous(1) {
        Thread.sleep(2000)
        messages.synchronized {
          messages.append("1")
        }
      }

      tasksPool.asynchronous(1) {
        Thread.sleep(2200)
        messages.synchronized {
          messages.append("2")
        }
      }

      tasksPool.asynchronous(1) {
        Thread.sleep(500)
        messages.synchronized {
          messages.append("3")
        }
      }

      tasksPool.asynchronous(3) {
        Thread.sleep(700)
        messages.synchronized {
          messages.append("4")
        }
      }

      tasksPool.asynchronous(10) {
        Thread.sleep(500)
        messages.synchronized {
          messages.append("5")
        }
      }

      tasksPool.asynchronous(2) {
        Thread.sleep(500)
        messages.synchronized {
          messages.append("6")
        }
      }

      Thread.sleep(5000)

      messages.toList should be(List("1", "2", "5", "4", "6", "3"))
    }

    "execute execute only important tasks after shutdown" in {
      val tasksPool = new PriorityTaskPool(2, 2)

      val messages = ListBuffer[String]()

      tasksPool.asynchronous(1) {
        Thread.sleep(2000)
        messages.synchronized {
          messages.append("1")
        }
      }

      tasksPool.asynchronous(1) {
        Thread.sleep(2200)
        messages.synchronized {
          messages.append("2")
        }
      }

      tasksPool.asynchronous(1) {
        Thread.sleep(500)
        messages.synchronized {
          messages.append("3")
        }
      }

      tasksPool.asynchronous(3) {
        Thread.sleep(500)
        messages.synchronized {
          messages.append("4")
        }
      }

      tasksPool.asynchronous(10) {
        Thread.sleep(500)
        messages.synchronized {
          messages.append("5")
        }
      }

      tasksPool.asynchronous(2) {
        Thread.sleep(500)
        messages.synchronized {
          messages.append("6")
        }
      }

      tasksPool.shutdown()

      messages.toList should be(List("1", "2", "5", "4"))
    }
  }
}
