package reductions

import java.util.concurrent._
import scala.collection._
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import common._
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory

@RunWith(classOf[JUnitRunner]) 
class LineOfSightSuite extends FunSuite {
  import LineOfSight._
  test("lineOfSight should correctly handle an array of size 4") {
    val output = new Array[Float](4)
    lineOfSight(Array[Float](0f, 1f, 8f, 9f), output)
    assert(output.toList == List(0f, 1f, 4f, 4f))
  }


  test("upsweepSequential should correctly handle the chunk 1 until 4 of an array of 4 elements") {
    val res = upsweepSequential(Array[Float](0f, 1f, 8f, 9f), 1, 4)
    assert(res == 4f)
  }


  test("downsweepSequential should correctly handle a 4 element array when the starting angle is zero") {
    val output = new Array[Float](4)
    downsweepSequential(Array[Float](0f, 1f, 8f, 9f), output, 0f, 1, 4)
    assert(output.toList == List(0f, 1f, 4f, 4f))
  }

  test("downsweep should correctly compute the output for a non-zero starting angle") {
    val output = new Array[Float](5)
    downsweep(Array[Float](0f, 7f, 10f, 33f, 48f), output, 8f, Node(Node(Leaf(1,2,7f),Leaf(2,3,5f)),Node(Leaf(3,4,11f),Leaf(4,5,12f))))
    assert(output.toList == List(0f, 8f, 8f, 11f, 12f))
  }

  test("parLineOfSight should correctly handle an array of size 4 - t1") {
    val output = new Array[Float](4)
    parLineOfSight(Array[Float](0f, 1f, 8f, 9f), output, 1)
    assert(output.toList == List(0f, 1f, 4f, 4f))
  }

  test("parLineOfSight should correctly handle an array of size 5 - t2") {
    val output = new Array[Float](5)
    parLineOfSight(Array[Float](0f, 1f, 8f, 9f, 20f), output, 2)
    assert(output.toList == List(0f, 1f, 4f, 4f, 5f))
  }

  test("parLineOfSight should correctly handle an array of size 4 - t2") {
    val output = new Array[Float](4)
    parLineOfSight(Array[Float](0f, 1f, 8f, 9f), output, 2)
    assert(output.toList == List(0f, 1f, 4f, 4f))
  }

  test("parLineOfSight should correctly handle an array of size 4 - t4") {
    val output = new Array[Float](4)
    parLineOfSight(Array[Float](0f, 1f, 8f, 9f), output, 4)
    assert(output.toList == List(0f, 1f, 4f, 4f))
  }

  test("parLineOfSight should correctly handle an array of size 17 - t4") {
    val output = new Array[Float](17)
    parLineOfSight(Array[Float](0f, 1f, 8f, 9f, 20f, 30f, 8f, 49f, 64f, 81f, 100f, 121f, 144f, 169f, 196f, 225f, 256f ), output, 4)
    assert(output.toList == List(0f, 1f, 4f, 4f, 5f, 6f, 6f, 7f, 8f, 9f, 10f, 11f, 12f, 13f, 14f, 15f, 16f))
  }

  test("parLineOfSight should correctly handle an array of size 5 - t2a") {
    val output = new Array[Float](5)
    parLineOfSight(Array[Float](0f, 7f, 10f, 33f, 48f), output, 2)
    assert(output.toList == List(0f, 7f, 7f, 11f, 12f))
  }
}

