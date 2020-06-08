package com.kiran.spark_scala.wiki.tutorial

import org.apache.spark.rdd.RDD

trait WikipediaRankingInterface {
  def occurencesOfLang(lang: String, rdd: RDD[WikipediaArticle]): Int
  def rankLangs(langs: List[String], rdd: RDD[WikipediaArticle]): List[(String, Int)]
  def makeIndex(langs: List[String], rdd: RDD[WikipediaArticle]): RDD[(String, Iterable[WikipediaArticle])]
  def rankLangsUsingIndex(index: RDD[(String, Iterable[WikipediaArticle])]): List[(String, Int)]
  def rankLangsReduceByKey(lang: List[String], rdd: RDD[WikipediaArticle]): List[(String, Int)]
}
