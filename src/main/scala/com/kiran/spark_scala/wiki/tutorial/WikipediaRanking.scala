package com.kiran.spark_scala.wiki.tutorial

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object WikipediaRanking extends WikipediaRankingInterface{

  val langs = List(
    "JavaScript", "Java", "PHP", "Python", "C#", "C++", "Ruby", "CSS",
    "Objective-C", "Perl", "Scala", "Haskell", "MATLAB", "Clojure", "Groovy")

  val conf = new SparkConf().setAppName("Wikipedia").setMaster("local[*]")
  val sc = new SparkContext(conf)

  def main(args: Array[String]): Unit = {
    //1. read in wikipedia article
    val wikiRdd: RDD[WikipediaArticle] = sc.textFile(WikipediaData.filePath).map(WikipediaData.parse)

    //2. Compute rankings of programming languages
    //2.1 Rank languages attempt #1: rankLangs
    val langsRanked: List[(String, Int)] = timed("Part1: Naive Ranking", rankLangs(langs, wikiRdd))
    println("### Output of part1 ### "+ langsRanked)

    //2.2 Rank languages attempt #2: rankLangsUsingIndex
    def index: RDD[(String, Iterable[WikipediaArticle])] = makeIndex(langs, wikiRdd)
    val langsRanked2: List[(String, Int)] = timed("Part2: Ranking using index", rankLangsUsingIndex(index))
    println("### Output of Part 2####"+langsRanked2)

    //2.3 Rank languages attempt #3: rankLangsReduceByKey
    val ranked3: List[(String, Int)] = timed("Part 3: ranking with reduce by key", rankLangsReduceByKey(langs, wikiRdd))
    println("### Output of part3 ###"+ranked3)

    /* Output the speed of each ranking */
    println(timing)
    sc.stop()
  }

  override def occurencesOfLang(lang: String, rdd: RDD[WikipediaArticle]): Int =
    rdd.filter(_.mentionsLanguage(lang)).aggregate(0)((x,y) => x+1, _+_)

  override def rankLangs(langs: List[String], rdd: RDD[WikipediaArticle]): List[(String, Int)] =
    langs.map(x => (x, occurencesOfLang(x, rdd))).sortBy(-_._2)

  override def makeIndex(langs: List[String], rdd: RDD[WikipediaArticle]):
  RDD[(String, Iterable[WikipediaArticle])] =
    rdd.flatMap(x => langs.filter(l => x.mentionsLanguage(l)).map(a => (a, x))).groupByKey()

  override def rankLangsUsingIndex(index: RDD[(String, Iterable[WikipediaArticle])]): List[(String, Int)] =
    index.mapValues(v => v.size).sortBy(-_._2).collect().toList

  override def rankLangsReduceByKey(lang: List[String], rdd: RDD[WikipediaArticle]): List[(String, Int)] =
    rdd.flatMap(x => lang.filter(x.mentionsLanguage).map((_,1))).reduceByKey(_+_).sortBy(-_._2).collect().toList

  val timing = new StringBuffer
  def timed[T](label: String, code: =>T): T ={
    val start = System.currentTimeMillis()
    val result = code
    val stop = System.currentTimeMillis()
    timing.append(s"Processing $label took ${stop-start} ms. \n")
    result
  }
}
