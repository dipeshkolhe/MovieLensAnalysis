package com.dipesh.movielens.exploration

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions.Window

object InsightsExplorer {
  def exploreInsights(moviesDF: DataFrame, moviesWithExplodedGenreDF: DataFrame, ratingsDF: DataFrame, tagsDF: DataFrame)(implicit spark: SparkSession): Unit = {
    
    import spark.implicits._
    //Find movie with highest average rating such that it has at least 100 ratings
    val averageRatingPerMovieWithRankDF = ratingsDF
      .groupBy('movieID)
      .agg(avg('rating).as("avgRating"), count('rating).as("numberOfRatings"))
      .filter('numberOfRatings >= 100)
      .withColumn("rank", dense_rank.over(Window.orderBy('avgRating.desc)))
    
    
    val highestRatedMovie = averageRatingPerMovieWithRankDF.filter('rank === 1)
      
    moviesDF.join(highestRatedMovie, moviesDF("movieID") === highestRatedMovie("movieID"))
      .show
    
  }
}