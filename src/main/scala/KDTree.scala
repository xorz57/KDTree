import Types.{Point, Points}

import scala.annotation.tailrec

class KDTree {
  private var _root: Option[KDNode] = None
  private var _size: Int = 0
  private var _comparisons: Int = 0

  def size: Int = _size

  def comparisons: Int = _comparisons

  def isEmpty: Boolean = {
    _root.isEmpty
  }

  def nonEmpty: Boolean = {
    _root.nonEmpty
  }

  def contains(point: Point): Boolean = {
    @tailrec
    def containsHelper(node: Option[KDNode], point: Point, depth: Int): Boolean = node match {
      case None =>
        false
      case Some(node) =>
        _comparisons += 1
        if (point == node.point) {
          true
        } else {
          val axis: Int = depth % point.length
          if (point(axis) < node.point(axis)) {
            containsHelper(node.lChild, point, depth + 1)
          } else {
            containsHelper(node.rChild, point, depth + 1)
          }
        }
    }

    _comparisons = 0
    containsHelper(_root, point, 0)
  }

  def range(minimum: Point, maximum: Point): Points = {
    var result: Points = Vector.empty[Point]

    def rangeHelper(root: Option[KDNode], minimum: Point, maximum: Point, depth: Int): Unit = root match {
      case None =>
      case Some(node) =>
        _comparisons += 1
        if (node.point.indices.forall(axis => minimum(axis) < node.point(axis) && node.point(axis) < maximum(axis))) {
          result +:= node.point
        }
        val axis: Int = depth % node.point.length
        if (maximum(axis) < node.point(axis)) {
          rangeHelper(node.lChild, minimum, maximum, depth + 1)
        } else if (minimum(axis) > node.point(axis)) {
          rangeHelper(node.rChild, minimum, maximum, depth + 1)
        } else {
          rangeHelper(node.lChild, minimum, maximum, depth + 1)
          rangeHelper(node.rChild, minimum, maximum, depth + 1)
        }
    }

    _comparisons = 0
    rangeHelper(_root, minimum, maximum, 0)
    result
  }

  def nearest(point: Point, p: Double = 2): (Double, Point) = {
    var nearestPoint: Point = Vector.empty[Double]
    var nearestDistance: Double = Double.PositiveInfinity

    def nearestHelper(node: Option[KDNode], point: Point, p: Double, depth: Int): Unit = node match {
      case None =>
      case Some(node) =>
        _comparisons += 1
        val currentDistance: Double = Metric.minkowski(node.point, point, p)
        if (currentDistance < nearestDistance) {
          nearestPoint = node.point
          nearestDistance = currentDistance
        }
        val axis: Int = depth % node.point.length
        if (point(axis) < node.point(axis)) {
          nearestHelper(node.lChild, point, p, depth + 1)
          if (math.pow(math.abs(point(axis) - node.point(axis)), p) < nearestDistance) {
            nearestHelper(node.rChild, point, p, depth + 1)
          }
        } else {
          nearestHelper(node.rChild, point, p, depth + 1)
          if (math.pow(math.abs(point(axis) - node.point(axis)), p) < nearestDistance) {
            nearestHelper(node.lChild, point, p, depth + 1)
          }
        }
    }

    _comparisons = 0
    nearestHelper(_root, point, p, 0)
    (nearestDistance, nearestPoint)
  }

  def knn(point: Point, q: Int, p: Double = 2): Vector[(Double, Point)] = {
    var result: Vector[(Double, Point)] = Vector.empty[(Double, Point)]
    var nearestDistance: Double = Double.PositiveInfinity

    def knnHelper(node: Option[KDNode], point: Point, p: Double, depth: Int): Unit = node match {
      case None =>
      case Some(node) =>
        _comparisons += 1
        val currentDistance: Double = Metric.minkowski(node.point, point, p)
        if (result.length < q - 1) {
          result +:= (currentDistance, node.point)
        } else if (result.length == q - 1) {
          result +:= (currentDistance, node.point)
          result = result.sortBy(_._1)
          nearestDistance = result.take(q).last._1
        } else {
          if (currentDistance < nearestDistance) {
            result +:= (currentDistance, node.point)
            result = result.sortBy(_._1)
            nearestDistance = result.take(q).last._1
          }
        }
        val axis: Int = depth % node.point.length
        if (point(axis) < node.point(axis)) {
          knnHelper(node.lChild, point, p, depth + 1)
          if (math.pow(math.abs(point(axis) - node.point(axis)), p) < nearestDistance) {
            knnHelper(node.rChild, point, p, depth + 1)
          }
        } else {
          knnHelper(node.rChild, point, p, depth + 1)
          if (math.pow(math.abs(point(axis) - node.point(axis)), p) < nearestDistance) {
            knnHelper(node.lChild, point, p, depth + 1)
          }
        }
    }

    _comparisons = 0
    knnHelper(_root, point, p, 0)
    result.take(q)
  }
}

object KDTree {
  def empty: KDTree = {
    val tree: KDTree = new KDTree
    tree
  }

  def apply(points: Points): KDTree = {
    val tree: KDTree = new KDTree
    tree._root = build(points, 0)
    tree._size = points.length
    tree
  }

  private def build(points: Points, depth: Int): Option[KDNode] = points.length match {
    case 0 =>
      None
    case 1 =>
      Some(KDNode(points.head, None, None))
    case _ =>
      val axis: Int = depth % points.head.length
      val (lPoints: Points, rPoints: Points) = points.sortBy(_ (axis)).splitAt(points.length / 2)
      Some(KDNode(
        rPoints.head,
        build(lPoints, depth + 1),
        build(rPoints.tail, depth + 1)
      ))
  }
}