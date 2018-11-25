package org.learningconcurrency.ch2

object SynchronizedProtectedUid {
  private var uidCount = 0L

  def getUniqueUid: Long = this.synchronized {
    val freshUid = uidCount + 1
    uidCount = freshUid
    freshUid
  }
}
