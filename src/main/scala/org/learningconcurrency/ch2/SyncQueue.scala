package org.learningconcurrency.ch2

import scala.collection.mutable.{Queue => MutableQueue}

class SyncQueue[T](val capacity: Int)
  extends Sync[T] {

  private val queue = MutableQueue[T]()

  override def get: T = lock.synchronized {
    if (isDefined) {
      queue.dequeue()
    } else {
      throw new IllegalStateException(s"SyncVar is empty")
    }
  }

  override def put(x: T): Unit = lock.synchronized {
    if (isEmpty) {
      queue.enqueue(x)
    } else {
      throw new IllegalStateException(s"SyncVar is already defined with $get")
    }
  }

  override def getWait: T = lock.synchronized {
    while (isEmpty) {
      lock.wait()
    }
    lock.notify()
    queue.dequeue()
  }

  override def putWait(x: T): Unit = lock.synchronized {
    while (isFull) {
      lock.wait()
    }
    queue.enqueue(x)
    lock.notify()
  }

  override def isEmpty: Boolean = queue.isEmpty

  override def isDefined: Boolean = !isEmpty

  private def isFull: Boolean = queue.size == capacity
}
