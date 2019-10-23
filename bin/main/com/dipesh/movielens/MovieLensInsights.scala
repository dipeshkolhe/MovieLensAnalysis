package com.dipesh.movielens

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame

object MovieLensInsights{

    val logger = LoggerFactory.getLogger("MovieLensInsights")

    def readCSV(spark: SparkSession, path: String, header: Boolean, inferSchema: Boolean): DataFrame = {
        spark.read
            .format("csv")
            .option("header", header.toString)
            .option("inferSchema", inferSchema.toString)
            .load(path)
    }

    def main(args: Array[String]): Unit = {
        val spark = SparkSession.builder.appName("MovieLensInsights").master("spark://dipesh-pc:7077").getOrCreate
        
        val moviesDF = readCSV(spark, "ml-latest-small/movies.csv", true, true)
        val linksDF = readCSV(spark, "ml-latest-small/links.csv", true, true)
        val ratingsDF = readCSV(spark, "ml-latest-small/ratings.csv", true, true)
        val tagsDF = readCSV(spark, "ml-latest-small/tags.csv", true, true)

        moviesDF.printSchema
        moviesDF.show(10)

        linksDF.printSchema
        linksDF.show(10)

        ratingsDF.printSchema
        ratingsDF.show(10)

        tagsDF.printSchema
        tagsDF.show(10)
        
    }
}