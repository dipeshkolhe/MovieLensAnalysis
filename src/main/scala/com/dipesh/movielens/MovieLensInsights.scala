package com.dipesh.movielens

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame
import com.dipesh.movielens.cleandata.DataCleaner

object MovieLensInsights{

    val logger = LoggerFactory.getLogger("MovieLensInsights")

    def readCSV(path: String, header: Boolean, inferSchema: Boolean)(implicit spark: SparkSession): DataFrame = {
        spark.read
            .format("csv")
            .option("header", header.toString)
            .option("inferSchema", inferSchema.toString)
            .load(path)
    }

    def main(args: Array[String]): Unit = {
        implicit val spark = SparkSession.builder.appName("MovieLensInsights").master("local").getOrCreate
        
        val moviesDF = readCSV("ml-latest-small/movies.csv", true, true)
        val linksDF = readCSV("ml-latest-small/links.csv", true, true)
        val ratingsDF = readCSV("ml-latest-small/ratings.csv", true, true)
        val tagsDF = readCSV("ml-latest-small/tags.csv", true, true)
        
        val cleanMoviesDF = DataCleaner.cleanMoviesDF(moviesDF)
        cleanMoviesDF.show(10)
        
    }
}