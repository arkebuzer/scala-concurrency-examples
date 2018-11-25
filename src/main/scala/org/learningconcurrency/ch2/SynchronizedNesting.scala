package org.learningconcurrency.ch2

import scala.collection.mutable.ArrayBuffer

object SynchronizedNesting
  extends App {
  private val transfers = ArrayBuffer[String]()

  def logTransfer(name: String, n: Int): Unit = transfers.synchronized {
    transfers += s"transfer to account '$name' = $n"
  }

  class Account(val name: String, var money: Int) {

    import SynchronizedProtectedUid.getUniqueUid

    val uid = getUniqueUid

    def add(account: Account, n: Int): Unit = account.synchronized {
      account.money += n
      if (n > 10) logTransfer(name, n)
    }
  }

}
