package com.kiran.spark_scala.stackoverflow.tutorial

case class Posting(postingType: Int, id: Int, acceptedAnswer: Option[Int], parentId: Option[QID], score: Int, tags: Option[String]) extends Serializable
