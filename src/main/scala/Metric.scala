import Types.Point

object Metric {
  def minkowski(src: Point, dst: Point, p: Double): Double =
    src.zip(dst).map(point => math.pow(math.abs(point._1 - point._2), p)).sum
}