package org.learningconcurrency.ch2

import org.scalatest.{Matchers, WordSpecLike}

// More demos than real tests
class SyncQueueSpec
  extends WordSpecLike
    with Matchers {

  val capacity = 5

  "SyncQueue" must {
    "be able to pass values between Threads in synchronized manner using get and pur" in {
      val dataLength = 15
      val syncQueue = new SyncQueue[Int](capacity)

      val producer = new Thread {
        override def run(): Unit = {
          for (i <- 0 to dataLength) {
            while (syncQueue.isDefined) {}
            syncQueue.put(i)
          }
        }
      }

      val consumer = new Thread {
        override def run(): Unit = {
          for (_ <- 0 to dataLength) {
            while (syncQueue.isEmpty) {}
            println(syncQueue.get)
          }
        }
      }

      consumer.start()
      producer.start()

      producer.join()
      consumer.join()
    }

    "be able to pass values between Threads in synchronized manner using getWait and putWait without active wait" in {
      val dataLength = 15
      val syncQueue = new SyncQueue[Int](capacity)

      val producer = new Thread {
        override def run(): Unit = {
          for (i <- 0 to dataLength) {
            syncQueue.putWait(i)
          }
        }
      }

      val consumer = new Thread {
        override def run(): Unit = {
          for (_ <- 0 to dataLength) {
            println(syncQueue.getWait)
          }
        }
      }

      consumer.start()
      producer.start()

      producer.join()
      consumer.join()
    }
  }

}
