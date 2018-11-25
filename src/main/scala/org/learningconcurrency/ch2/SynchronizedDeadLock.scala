package org.learningconcurrency.ch2

object SynchronizedDeadLock
  extends App {

  import SynchronizedNesting.Account

  def send(a1: Account, a2: Account, n: Int): Unit = {
    def adjust(): Unit = {
      a1.money -= n
      a2.money += n
    }

    if (a1.uid < a2.uid) {
      a1.synchronized {
        a2.synchronized {
          adjust()
        }
      }
    } else {
      a2.synchronized {
        a1.synchronized {
          adjust()
        }
      }
    }
  }

  def sendAll(accounts: Set[Account], target: Account): Unit = {
    def sendAllFromAccount(account: Account): Unit = {
      target.money += account.money
      account.money = 0
    }

    for (acc <- accounts) {
      if (acc.uid < target.uid) {
        acc.synchronized {
          target.synchronized {
            sendAllFromAccount(acc)
          }
        }
      } else {
        target.synchronized {
          acc.synchronized {
            sendAllFromAccount(acc)
          }
        }
      }
    }
  }
}
