import Types.{Point, Points}
import org.scalatest.flatspec._
import org.scalatest.matchers._

class KDTreeSpec extends AnyFlatSpec with should.Matchers {
  val n: Int = 256
  val k: Int = 3
  val p: Double = 2.00

  val points: Points = Vector.fill[Point](n)(Vector.fill[Double](k)(scala.util.Random.nextDouble()))
  val point: Point = Vector.fill[Double](k)(scala.util.Random.nextDouble())
  var minimum: Vector[Double] = Vector.fill[Double](k)(scala.util.Random.nextDouble())
  var maximum: Vector[Double] = Vector.fill[Double](k)(scala.util.Random.nextDouble())
  while (minimum.indices.exists(axis => minimum(axis) >= maximum(axis))) {
    minimum = Vector.fill[Double](k)(scala.util.Random.nextDouble())
    maximum = Vector.fill[Double](k)(scala.util.Random.nextDouble())
  }

  val tree: KDTree = KDTree(points)
  val naive: Naive = Naive(points)

  "A KDTree" should "not be empty" in {
    tree.nonEmpty shouldBe true
    tree.isEmpty shouldBe false
  }

  it should "contain existing points" in {
    points.forall(tree.contains) shouldBe points.forall(naive.contains)
    tree.comparisons <= points.length shouldBe true
  }

  it should "return all points within minimum and maximum coordinates" in {
    val result1: Points = tree.range(minimum, maximum)
    val result2: Points = naive.range(minimum, maximum)
    result1.length shouldBe result2.length
    tree.comparisons <= points.length shouldBe true
  }

  it should "return the nearest neighbor" in {
    val result1: (Double, Point) = tree.nearest(point, p)
    val result2: (Double, Point) = naive.nearest(point, p)
    result1 shouldBe result2
    tree.comparisons <= points.length shouldBe true
  }

  it should "return the k nearest neighbors" in {
    (1 to 5).foreach(q => {
      val result1 = tree.knn(point, q, p)
      val result2 = naive.knn(point, q, p)
      result1 shouldBe result2
      tree.comparisons <= points.length shouldBe true
    })
  }
}