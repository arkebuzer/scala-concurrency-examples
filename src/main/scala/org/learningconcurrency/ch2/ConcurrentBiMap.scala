package org.learningconcurrency.ch2

import scala.collection.mutable.{Map => MutableMap}

class ConcurrentBiMap[K, V] {

  private val keysToValues: MutableMap[K, V] = MutableMap[K, V]()
  private val valuesToKeys: MutableMap[V, K] = MutableMap[V, K]()

  def put(k: K, v: V): Option[(K, V)] = keysToValues.synchronized {
    valuesToKeys.synchronized {
      val vOpt = keysToValues.get(k)
      vOpt match {
        case Some(_) => None
        case None =>
          keysToValues += k -> v
          valuesToKeys += v -> k
          Some(k, v)
      }
    }
  }

  def removeKey(k: K): Option[V] = keysToValues.synchronized {
    valuesToKeys.synchronized {
      val vOpt = keysToValues.get(k)
      vOpt match {
        case Some(v) =>
          keysToValues -= k
          valuesToKeys -= v
          vOpt
        case None => None
      }
    }
  }

  def removeValue(v: V): Option[K] = keysToValues.synchronized {
    valuesToKeys.synchronized {
      val kOpt = valuesToKeys.get(v)
      kOpt match {
        case Some(k) =>
          keysToValues -= k
          valuesToKeys -= v
          kOpt
        case None => None
      }
    }
  }

  def getValue(k: K): Option[V] = keysToValues.synchronized {
    valuesToKeys.synchronized {
      keysToValues.get(k)
    }
  }

  def getKey(v: V): Option[K] = keysToValues.synchronized {
    valuesToKeys.synchronized {
      valuesToKeys.get(v)
    }
  }

  def size: Int = keysToValues.synchronized {
    valuesToKeys.synchronized {
      keysToValues.size
    }
  }

  def iterator: Iterator[(K, V)] = keysToValues.synchronized {
    valuesToKeys.synchronized {
      new ConcurrentBiMapIterator
    }
  }

  // Not sure if iterator works correctly
  class ConcurrentBiMapIterator
    extends Iterator[(K, V)] {

    private val iterator =
      keysToValues.synchronized {
        valuesToKeys.synchronized {
          keysToValues.clone().iterator
        }
      }

    override def hasNext: Boolean = iterator.synchronized {
      iterator.hasNext
    }

    override def next(): (K, V) = iterator.synchronized {
      iterator.next()
    }
  }

  def replace(k1: K, v1: V, k2: K, v2: V): Unit = keysToValues.synchronized {
    valuesToKeys.synchronized {
      getValue(k1) match {
        case Some(v) if v == v1 =>
          getValue(k2) match {
            case Some(_) =>
            case None =>
              removeKey(k1)
              put(k2, v2)
          }
        case None =>
      }
    }
  }

}
