import Types.Point

case class KDNode(point: Point, lChild: Option[KDNode], rChild: Option[KDNode])