import Types.{Point, Points}

class Naive {
  private var _points: Points = Vector.empty[Point]
  private var _size: Int = 0

  def size: Int = _size

  def isEmpty: Boolean = {
    _points.isEmpty
  }

  def nonEmpty: Boolean = {
    _points.nonEmpty
  }

  def contains(point: Point): Boolean = {
    _points.contains(point)
  }

  def range(minimum: Point, maximum: Point): Points = {
    _points.filter(point => point.indices.forall(axis => minimum(axis) <= point(axis) && point(axis) <= maximum(axis)))
  }

  def nearest(point: Point, p: Double = 2.0): (Double, Point) = {
    var nearestPoint: Point = Vector.empty[Double]
    var nearestDistance: Double = Double.PositiveInfinity
    _points.foreach(current => {
      val currentDistance: Double = Metric.minkowski(current, point, p)
      if (currentDistance < nearestDistance) {
        nearestPoint = current
        nearestDistance = currentDistance
      }
    })
    (nearestDistance, nearestPoint)
  }

  //  def nearest(point: KDPoint, p: Double = 2.0): (Double, KDPoint) = {
  //      _points.map(current => (Metric.minkowski(current, point, p), current)).minBy(_._1)
  //  }

  def knn(point: Point, q: Int, p: Double = 2.0): Vector[(Double, Point)] = {
    var result: Vector[(Double, Point)] = Vector.empty[(Double, Point)]
    var nearestDistance: Double = Double.PositiveInfinity
    _points.foreach(current => {
      val currentDistance: Double = Metric.minkowski(current, point, p)
      if (result.length < q - 1) {
        result +:= (currentDistance, current)
      } else if (result.length == q - 1) {
        result +:= (currentDistance, current)
        result = result.sortBy(_._1)
        nearestDistance = result.take(q).last._1
      } else {
        if (currentDistance < nearestDistance) {
          result +:= (currentDistance, current)
          result = result.sortBy(_._1)
          nearestDistance = result.take(q).last._1
        }
      }
    })
    result.take(q)
  }
}

object Naive {
  def empty: Naive = {
    val naive: Naive = new Naive
    naive._points = Vector.empty[Point]
    naive
  }

  def apply(points: Points): Naive = {
    val naive: Naive = new Naive
    naive._points = points
    naive._size = points.length
    naive
  }
}