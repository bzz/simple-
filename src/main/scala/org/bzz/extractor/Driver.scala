package org.bzz.extractor

import io.grpc.ManagedChannelBuilder
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{BinaryType, StringType, StructField, StructType}
import org.bzz.extractor.protobuf.{RepositoriesBatch, UastExtractorGrpc}
import org.bzz.extractor.protobuf.UastExtractorGrpc.UastExtractorBlockingStub

import scala.util.Properties

object FilesUast {
   val schema = StructType(
      StructField("repoId", StringType, nullable = false) ::
      StructField("repoUrl", StringType, nullable = false) ::
      StructField("path", StringType, nullable = false) ::
      StructField("lang", StringType, nullable = true) ::
      StructField("uast", BinaryType, nullable = true) ::
      StructField("hash", StringType, nullable = false) ::
      StructField("ref", StringType, nullable = false) ::
      Nil)
}

object UastStreamIterator {
  val grpcHost = "localhost"
  val grpcPort = 8989
  private val channel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort).usePlaintext(true).build
  private val blockingUastClient: UastExtractorBlockingStub = UastExtractorGrpc.blockingStub(channel)

  def allFilesToUast(repoIdsRows: Iterator[Row]): Iterator[Row] = {
    val repoIds = repoIdsRows.map(_.getString(0)).toSeq
    blockingUastClient.parseEveryFileToUast(new RepositoriesBatch(repoIds))
      .map(f => Row(f.repoId, f.repoUrl, f.path, f.lang, f.uast, f.hash, f.reference))
  }
}

object BerserkerDriver  {
  val parquetFilename = "uastPerFile"
  val numberOfWorkers = 4
  private val master = Properties.envOrElse("MASTER", "local[*]")

  def main(args: Array[String]): Unit = {
    //TODO(bzz): propper CLI here to parametrize above and below
    val spark = getSparkSession(master)
    import spark.implicits._

    val jdbcDF = spark.read
      .format("jdbc")
      .option("url", "jdbc:postgresql://localhost:5432/testing")
      .option("dbtable", "repositories")
      .option("user", "testing")
      .option("password", "testing")
      .load()

    val filesUast = jdbcDF.select($"id").rdd.repartition(numberOfWorkers)
      .mapPartitions(partition => {
        //TODO: start analysis-pipeline gRpc server process here
        val blockingUastResponse: Iterator[Row] = UastStreamIterator.allFilesToUast(partition)
        blockingUastResponse
        // size of input != output
      })

    val filesUastDF = spark.createDataFrame(filesUast, FilesUast.schema)

    filesUastDF.write
      //.option("compression", "none")
      .mode("overwrite")
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

}
