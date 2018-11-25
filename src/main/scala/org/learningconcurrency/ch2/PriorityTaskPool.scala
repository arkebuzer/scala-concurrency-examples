package org.learningconcurrency.ch2

import scala.collection.mutable


class PriorityTaskPool(p: Int, val important: Int) {

  private case class TaskWithPriority(priority: Int, task: () => Unit)

  private implicit object TaskWithPriorityOrdering extends Ordering[TaskWithPriority] {
    def compare(a: TaskWithPriority, b: TaskWithPriority): Int = a.priority compare b.priority
  }

  private val tasks = mutable.PriorityQueue[TaskWithPriority]()

  private var isShutdown = false
  private var runningWorkers: Int = p
  private val runningWorkersLock = new AnyRef

  def asynchronous(priority: Int)(task: => Unit): Unit = {
    if (!isShutdown) {
      tasks.synchronized {
        tasks.enqueue(TaskWithPriority(priority, () => task))
        tasks.notify()
      }
    }
  }

  def shutdown(): Unit = runningWorkersLock.synchronized {
    isShutdown = true
    while (runningWorkers != 0) runningWorkersLock.wait()
    tasks.clear()
  }

  private def importantTasksExists(): Boolean = {
    tasks.exists(_.priority > important)
  }

  private def workerMustStop(): Boolean = {
    isShutdown && !importantTasksExists()
  }

  private class Worker extends Thread {
    setDaemon(true)

    def poll(): () => Unit = tasks.synchronized {
      // If it is shutdown execute only important tasks
      while ((!isShutdown && tasks.isEmpty) || workerMustStop()) {
        if (workerMustStop()) {
          runningWorkersLock.synchronized {
            runningWorkers -= 1
            runningWorkersLock.notify()
          }
        }
        tasks.wait()
      }
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
