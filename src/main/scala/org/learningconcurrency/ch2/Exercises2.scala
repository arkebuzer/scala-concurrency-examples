package org.learningconcurrency.ch2

object Exercises2 {

  def parallel[A, B](a: => A, b: => B): (A, B) = {
    var aResult: Option[A] = None
    var bResult: Option[B] = None

    val aThread = new Thread {
      override def run(): Unit = {
        aResult = Some(a)
      }
    }

    val bThread = new Thread {
      override def run(): Unit = {
        bResult = Some(b)
      }
    }

    aThread.start()
    bThread.start()

    aThread.join()
    bThread.join()

    (aResult.get, bResult.get)
  }

  def periodically(duration: Long)(b: => Unit): Unit = {
    val worker = new Thread {
      override def run(): Unit = {
        while (true) {
          b
          Thread.sleep(duration)
        }
      }
    }
    worker.start()
  }

}
