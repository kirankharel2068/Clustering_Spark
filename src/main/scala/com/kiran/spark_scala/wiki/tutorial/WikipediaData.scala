package com.kiran.spark_scala.wiki.tutorial

import java.io.File


object WikipediaData {

  private[tutorial] def filePath: String = {
    val resources = this.getClass.getClassLoader.getResource("wikipedia/wikipedia.dat")
    if(resources == null) sys.error("Plese make sure that the file is in right path")
    new File(resources.toURI).getPath
  }

  private[tutorial] def parse(line: String): WikipediaArticle={
    val subs = "</title><text>"
    val i = line.indexOf(subs)
    val title = line.substring(14, i)
    val text = line.substring(i + subs.length, line.length-16)
    WikipediaArticle(title, text)
  }
}
