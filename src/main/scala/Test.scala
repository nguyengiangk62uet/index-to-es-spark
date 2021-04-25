

object Test {
  def main(args: Array[String]): Unit = {
    val obj = new IndexDocument("1", "name")
    val obj2 = new IndexDocument("2", "name")
    var docs = List[IndexDocument]()
    docs :+ obj :+ obj2
    println(docs)
  }

}

