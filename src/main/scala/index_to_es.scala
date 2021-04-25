import org.apache.spark.sql._
import org.elasticsearch.spark.sql.sparkDatasetFunctions


object index_to_es {
  def main(args: Array[String]): Unit = {
    println("Start app ...")

    // Create spark session
    val spark = SparkSession.builder()
                  .master("spark://master:7077")
                  .appName("Spark - Index to ES")
                  .config("spark.es.nodes","node1")
                  .config("spark.es.port","9200")
//                  .config("spark.es.nodes.wan.only","true") //Needed for ES on AWS
                  .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    // Write dataframe to ES
    println("Staring indexing to ES ...")
    val startTimeMillis = System.currentTimeMillis()

    // Create spark context
    val sc = spark.sparkContext
    sc.setLogLevel("ERROR")
    // Create RDD from file
    val rddFromFile = spark.sparkContext.wholeTextFiles("hdfs://master:9000/bigdata/bigger")
      .map(file => (file._1.split('/').last, file._2)) // RDD has 2 fields

    import spark.implicits._
    // Create Dataset[IndexDocument] from RDD
    val input_ds = spark.createDataset(rddFromFile)
    input_ds.saveToEs("bigdata")

    // End indexing
    val endTimeMillis = System.currentTimeMillis()
    val durationSeconds = (endTimeMillis - startTimeMillis) / 1000

    println("Indexing successfully! Time indexing: " + durationSeconds + " seconds")
  } // End main
} // End object index_to_es

//case class IndexDocument (fileName:String, content:String)
