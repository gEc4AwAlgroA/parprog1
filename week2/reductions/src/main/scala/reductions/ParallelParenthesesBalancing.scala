package reductions

import scala.annotation._
import org.scalameter._
import common._

object ParallelParenthesesBalancingRunner {

  @volatile var seqResult = false

  @volatile var parResult = false

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 40,
    Key.exec.maxWarmupRuns -> 80,
    Key.exec.benchRuns -> 120,
    Key.verbose -> true
  ) withWarmer(new Warmer.Default)

  def main(args: Array[String]): Unit = {
    val length = 100000000
    val chars = new Array[Char](length)
    val threshold = 10000
    val seqtime = standardConfig measure {
      seqResult = ParallelParenthesesBalancing.balance(chars)
    }
    println(s"sequential result = $seqResult")
    println(s"sequential balancing time: $seqtime ms")

    val fjtime = standardConfig measure {
      parResult = ParallelParenthesesBalancing.parBalance(chars, threshold)
    }
    println(s"parallel result = $parResult")
    println(s"parallel balancing time: $fjtime ms")
    println(s"speedup: ${seqtime / fjtime}")
  }
}

object ParallelParenthesesBalancing {

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
   */
  def balance(chars: Array[Char]): Boolean = {
    def traverse(idx: Int, until: Int, sum: Int): Boolean = {
      if (sum < 0) false
      else if (idx == until) sum == 0
      else if (chars(idx) == '(') traverse(idx + 1, until, sum + 1)
      else if (chars(idx) == ')') traverse(idx + 1, until, sum - 1)
      else traverse(idx + 1, until, sum)
    }
    traverse(0, chars.length, 0)
  }

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
   */
  def parBalance(chars: Array[Char], threshold: Int): Boolean = {

    def traverse(idx: Int, until: Int, sum: Int, maxDepth: Int) : (Int, Int) = {
//      println("traverse", idx, until, sum, maxDepth)
      if (idx == until) (sum, maxDepth)
      else if (chars(idx) == '(') traverse(idx + 1, until, sum + 1, maxDepth)
      else if (chars(idx) == ')') traverse(idx + 1, until, sum - 1, maxDepth min sum - 1)
      else traverse(idx + 1, until, sum, maxDepth)
    }

    def reduce(from: Int, until: Int) : (Int, Int) = {
//      println("reduce", from, until)
      if (until - from > threshold) {
        val ((l1, r1), (l2, r2)) = parallel (reduce(from, (from + until) / 2),
                                             reduce((from + until) / 2, until) )
        (l1+l2, r1 min l1+r2)
      }
      else traverse(from, until, 0, 0)
    }

    reduce(0, chars.length) == (0, 0)
  }

  // For those who want more:
  // Prove that your reduction operator is associative!

}
