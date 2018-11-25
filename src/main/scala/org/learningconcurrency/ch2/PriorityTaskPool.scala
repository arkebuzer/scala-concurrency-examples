package org.learningconcurrency.ch2

import scala.collection.mutable


class PriorityTaskPool(p: Int) {

  private case class TaskWithPriority(priority: Int, task: () => Unit)

  private implicit object TaskWithPriorityOrdering extends Ordering[TaskWithPriority] {
    def compare(a: TaskWithPriority, b: TaskWithPriority): Int = a.priority compare b.priority
  }

  private val tasks = mutable.PriorityQueue[TaskWithPriority]()

  def asynchronous(priority: Int)(task: => Unit): Unit = tasks.synchronized {
    tasks.enqueue(TaskWithPriority(priority, () => task))
    tasks.notify()
  }

  private class Worker extends Thread {
    setDaemon(true)

    def poll(): () => Unit = tasks.synchronized {
      while (tasks.isEmpty) tasks.wait()
      tasks.dequeue().task
    }

    override def run(): Unit = {
      while (true) {
        val task = poll()
        task()
      }
    }
  }

  private val workers =
    for (_ <- 1 to p)
      yield new Worker

  workers.foreach(_.start())
}
