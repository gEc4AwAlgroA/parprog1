import java.util.concurrent._
import scala.util.DynamicVariable
import scala.util.Random

  val forkJoinPool = new ForkJoinPool

val scheduler =
  new DynamicVariable[TaskScheduler](new DefaultTaskScheduler)

def task[T](body: => T): ForkJoinTask[T] = {
  scheduler.value.schedule(body)
}

abstract class TaskScheduler {
    def schedule[T](body: => T): ForkJoinTask[T]
    def parallel[A, B](taskA: => A, taskB: => B): (A, B) = {
      val right = task {
        taskB
      }
      val left = taskA
      (left, right.join())
    }
  }

  class DefaultTaskScheduler extends TaskScheduler {
    def schedule[T](body: => T): ForkJoinTask[T] = {
      val t = new RecursiveTask[T] {
        def compute = body
      }
      Thread.currentThread match {
        case wt: ForkJoinWorkerThread =>
          t.fork()
        case _ =>
          forkJoinPool.execute(t)
      }
      t
    }
  }





  def parallel[A, B](taskA: => A, taskB: => B): (A, B) = {
    scheduler.value.parallel(taskA, taskB)
  }

  def parallel[A, B, C, D](taskA: => A, taskB: => B, taskC: => C, taskD: => D): (A, B, C, D) = {
    val ta = task { taskA }
    val tb = task { taskB }
    val tc = task { taskC }
    val td = taskD
    (ta.join(), tb.join(), tc.join(), td)
  }

1 to 6
def monteCarloCount(iter: Long): Long = {
  val random = new Random
  var hits = 0L
  for (i <- 0L until iter) {
    val x = random.nextDouble
    val y = random.nextDouble
//    println(x, y)
    if (x * x + y * y < 1) hits = hits + 1
  }
  hits
}

def monteCarloPiSeq(iter: Long): Double = {
  4.0 * monteCarloCount(iter) / iter
}

monteCarloPiSeq(10)
monteCarloPiSeq(100)
monteCarloPiSeq(1000)
//monteCarloPiSeq(10000)
//monteCarloPiSeq(100000)
//monteCarloPiSeq(1000000)

def monteCarloPiPar(iter: Long): Double = {
  val (((pi1, pi2),(pi3, pi4)),((pi1a, pi2a),(pi3a, pi4a))) =
    parallel(parallel(parallel(monteCarloCount(iter/8), monteCarloCount(iter/8)),
                      parallel(monteCarloCount(iter/8), monteCarloCount(iter/8))),
             parallel(parallel(monteCarloCount(iter/8), monteCarloCount(iter/8)),
                      parallel(monteCarloCount(iter/8), monteCarloCount(iter/8))))
  4.0 * (pi1 + pi2 + pi3 + pi4 + pi1a + pi2a + pi3a + pi4a) / iter
}

monteCarloPiPar(10000)
//monteCarloPiPar(1000000000L) //slow
