package tech.sourced.berserker

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{StringType, StructField, StructType}

object FilesSchema {
  val file = StructType(
      StructField("repoId", StringType, nullable = false) ::
      StructField("repoUrl", StringType, nullable = false) ::
      StructField("path", StringType, nullable = false) ::
      StructField("lang", StringType, nullable = true) ::
      StructField("uast", BinaryType, nullable = true) ::
      StructField("hash", StringType, nullable = false) ::
      Nil
  )
}

object UastStreamIterator {
  val grpcHost = localhost
  val grpcport = 8989
  val channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build
  val blockingUastClient: UastBlockingStub = UastGrpc.blockingStub(channel)
  def allFilesToUast(repoIds: Seq[String]) = {
    blockingUastClient.ParseEveryFileToUast(repoIds)
  }
}

  val master = Properties.envOrElse("MASTER", "local[*]")
object BerserkerDriver extends App {
  val spark = getSparkSession(master)

  val jdbcDF = spark.read
    .format("jdbc")
    .option("url", "jdbc:postgresql://localhost:5432/testing")
    .option("dbtable", "repositories")
    .option("user", "testing")
    .option("password", "testing")
    .load()

  val filesUast = jdbcDF.select($"id").rdd
    .repartition(numberOfWorkers).mapPartitions(partition => {
      //TODO: start analysis-pipeline gRpc server process here
      val blockingUastResponse: Iterator[FileUast] = UastStreamIterator.allFilesToUast(partitions.toSeq)
      blockingUastResponse
      // size of input != output
    })
    .map(fileInfo => FilesSchema(fileInfo.repoId, fileInfo.repoUrl, ..., fileInfo.uast))
    .toDF

  val parquetFilename = "uastPerFile"
  filesUast.write
    //.option("compression", "none")
    //.mode("overwrite")
    .parquet(parquetFilename)
}

def getSparkSession(sparkMaster: String): SparkSession = {
  val spark = SparkSession
    .builder()
    .appName(this.getClass.getSimpleName)
    .master(sparkMaster)
    //.config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .getOrCreate()
  spark
}
