package org.learningconcurrency.ch2

class SyncVar[T]
  extends Sync[T] {

  private var value: Option[T] = None

  override def get: T = lock.synchronized {
    if (isDefined) {
      val r = value.get
      value = None
      r
    } else {
      throw new IllegalStateException(s"SyncVar is empty")
    }
  }

  override def put(x: T): Unit = lock.synchronized {
    if (isEmpty) {
      value = Some(x)
    } else {
      throw new IllegalStateException(s"SyncVar is already defined with $get")
    }
  }

  override def getWait: T = lock.synchronized {
    while (isEmpty) {
      lock.wait()
    }
    val r = value.get
    value = None
    lock.notify()
    r
  }

  override def putWait(x: T): Unit = lock.synchronized {
    while (isDefined) {
      lock.wait()
    }
    value = Some(x)
    lock.notify()
  }

  override def isEmpty: Boolean = value.isEmpty

  override def isDefined: Boolean = !isEmpty
}
