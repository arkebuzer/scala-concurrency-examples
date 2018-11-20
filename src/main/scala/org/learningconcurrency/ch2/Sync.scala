package org.learningconcurrency.ch2

trait Sync[T] {

  protected val lock = new AnyRef

  def get: T

  def put(x: T): Unit

  def getWait: T

  def putWait(x: T): Unit

  def isEmpty: Boolean

  def isDefined: Boolean
}
