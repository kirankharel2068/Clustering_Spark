package com.kiran.spark_scala.wiki.tutorial

case class WikipediaArticle(title: String, text: String) {
/**
 * @return wheteher the text of this article mentions 'lang' or not
 * @param lang language to look for
 * */

  def mentionsLanguage(lang: String):Boolean = text.split(" ").contains(lang)
}
