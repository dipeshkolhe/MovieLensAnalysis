package com.dipesh.movielens.cleandata

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.Column
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.functions.{explode, split, to_date, from_unixtime}
import org.apache.spark.sql.types.StringType

object DataCleaner {
  def cleanMoviesDF(movieDF: DataFrame)(implicit spark: SparkSession): DataFrame = {
    import spark.implicits._
    val extractYearUDF = udf(extractYear _)
    val removeReleaseYearUDF = udf(removeReleaseYear _)
    movieDF.withColumn("releaseYear", extractYearUDF('title))
      .withColumn("title", removeReleaseYearUDF('title))
      .withColumn("genre", explode(split('genres, "\\|")))
      .drop('genres)
  }
  
  def removeReleaseYear(title: String): String = {
    val yearPattern = "\\([0-9]{4}\\)".r
    val releaseYear = yearPattern.findFirstIn(title).getOrElse("")
    title.splitAt(title.indexOf(releaseYear))._1.trim
  }
  
  def extractYear(title: String): String = {
    val yearPattern = "\\(([0-9]{4})\\)".r
    val releaseYear = yearPattern.findFirstIn(title).getOrElse("")
    if(releaseYear.contains("(")){
      releaseYear.drop(1).dropRight(1)
    }
    else
      releaseYear
  }
  
  def cleanRatingsDF(ratingsDF: DataFrame)(implicit spark: SparkSession): DataFrame = {
    import spark.implicits._
    //ratingsDF.withColumn("datetime", to_date('timestamp.cast(StringType), "yyyy-MM-dd HH:mm:ss"))
    ratingsDF.withColumn("datetime", from_unixtime('timestamp, "yyyy-MM-dd HH:mm:ss"))
  }
  
  def cleanTagsDF(tagsDF: DataFrame)(implicit spark: SparkSession): DataFrame = {
    import spark.implicits._
    tagsDF.withColumn("datetime", from_unixtime('timestamp, "yyyy-MM-dd HH:mm:ss"))
  }
  
}