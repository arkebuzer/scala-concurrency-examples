package org.learningconcurrency.ch2

class SyncVar[T] {

  private var value: Option[T] = None
  private val lock = new AnyRef

  def get: T = lock.synchronized {
    if (isDefined) {
      val r = value.get
      value = None
      r
    } else {
      throw new IllegalStateException(s"SyncVar is empty")
    }
  }

  def put(x: T): Unit = lock.synchronized {
    if (isEmpty) {
      value = Some(x)
    } else {
      throw new IllegalStateException(s"SyncVar is already defined with $get")
    }
  }

  def getWait: T = lock.synchronized {
    while (isEmpty) {
      lock.wait()
    }
    val r = value.get
    value = None
    lock.notify()
    r
  }

  def putWait(x: T): Unit = lock.synchronized {
    while (isDefined) {
      lock.wait()
    }
    value = Some(x)
    lock.notify()
  }

  def isEmpty: Boolean = value.isEmpty


  def isDefined: Boolean = !isEmpty

}
