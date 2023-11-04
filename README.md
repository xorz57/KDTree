<h1 align="center">KDTree</h1>

<div align="center">
    <a href="https://sonarcloud.io/summary/new_code?id=xorz57_KDTree">
        <img src="https://sonarcloud.io/api/project_badges/measure?project=xorz57_KDTree&metric=alert_status" alt="Quality Gate Status">
    </a>
    <a href="https://sonarcloud.io/summary/new_code?id=xorz57_KDTree">
        <img src="https://sonarcloud.io/api/project_badges/measure?project=xorz57_KDTree&metric=bugs" alt="Bugs">
    </a>
    <a href="https://sonarcloud.io/summary/new_code?id=xorz57_KDTree">
        <img src="https://sonarcloud.io/api/project_badges/measure?project=xorz57_KDTree&metric=vulnerabilities" alt="Vulnerabilities">
    </a>
    <a href="https://sonarcloud.io/summary/new_code?id=xorz57_KDTree">
        <img src="https://sonarcloud.io/api/project_badges/measure?project=xorz57_KDTree&metric=code_smells" alt="Code Smells">
    </a>
    <!-- <a href="https://sonarcloud.io/summary/new_code?id=xorz57_KDTree">
        <img src="https://sonarcloud.io/api/project_badges/measure?project=xorz57_KDTree&metric=coverage" alt="Coverage">
    </a> -->
    <a href="https://sonarcloud.io/summary/new_code?id=xorz57_KDTree">
        <img src="https://sonarcloud.io/api/project_badges/measure?project=xorz57_KDTree&metric=duplicated_lines_density" alt="Duplicated Lines (%)">
    </a>
</div>

## Example

```scala
object KDTreeSample {
  def main(args: Array[String]): Unit = {
    val points = Vector(
      Vector(2.0, 3.0),
      Vector(5.0, 4.0),
      Vector(9.0, 6.0),
      Vector(4.0, 7.0),
      Vector(8.0, 1.0),
      Vector(7.0, 2.0)
    )

    val tree = KDTree(points)

    val result1 = tree.contains(Vector(2.0, 3.0))
    val result2 = tree.range(Vector(3.0, 3.0), Vector(6.0, 8.0))
    val result3 = tree.nearest(Vector(5.0, 6.0))
    val result4 = tree.knn(Vector(5.0, 6.0), 3)

    println(s"result1: $result1")
    println(s"result2: $result2")
    println(s"result3: $result3")
    println(s"result4: $result4")
  }
}
```

## Output

```console
result1: true
result2: Vector(Vector(4.0, 7.0), Vector(5.0, 4.0))
result3: (2.0,Vector(4.0, 7.0))
result4: Vector((2.0,Vector(4.0, 7.0)), (4.0,Vector(5.0, 4.0)), (16.0,Vector(9.0, 6.0)))
```
