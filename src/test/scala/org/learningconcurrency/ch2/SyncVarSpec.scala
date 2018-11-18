package org.learningconcurrency.ch2

import org.scalatest.{Matchers, WordSpecLike}

// More demos than real tests
class SyncVarSpec
  extends WordSpecLike
    with Matchers {

  "SyncVar" must {
    "be able to pass values between Threads in synchronized manner using get and pur" in {
      val dataLength = 15
      val syncVar = new SyncVar[Int]

      val producer = new Thread {
        override def run(): Unit = {
          for (i <- 0 to dataLength) {
            while (syncVar.isDefined) {}
            syncVar.put(i)
          }
        }
      }

      val consumer = new Thread {
        override def run(): Unit = {
          for (_ <- 0 to dataLength) {
            while (syncVar.isEmpty) {}
            println(syncVar.get)
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
      val syncVar = new SyncVar[Int]

      val producer = new Thread {
        override def run(): Unit = {
          for (i <- 0 to dataLength) {
            syncVar.putWait(i)
          }
        }
      }

      val consumer = new Thread {
        override def run(): Unit = {
          for (_ <- 0 to dataLength) {
            println(syncVar.getWait)
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
