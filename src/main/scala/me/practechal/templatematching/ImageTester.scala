package me.practechal.templatematching

import boofcv.factory.feature.detect.template.{FactoryTemplateMatching, TemplateScoreType}
import boofcv.io.image.UtilImageIO
import boofcv.struct.feature.Match
import boofcv.struct.image.GrayF32

import scala.collection.JavaConversions._

object ImageTester {
  /**
    * Demonstrates how to search for matches of a template inside an image
    *
    * @param image           Image being searched
    * @param template        Template being looked for
    * @param expectedMatches Number of expected matches it hopes to find
    * @return List of match location and scores
    */
  private def findMatches(image: GrayF32, template: GrayF32, expectedMatches: Int): Seq[Match] = {
    // create template matcher.
    val matcher = FactoryTemplateMatching.createMatcher(TemplateScoreType.SUM_DIFF_SQ, classOf[GrayF32])
    // Find the points which match the template the best
    matcher.setTemplate(template, null, expectedMatches)
    matcher.process(image)
    matcher.getResults.toList
  }

  def findMatches(fullImagePath: String, partOfImagePath: String, expectedMatches: Int): Seq[Match] = {
    val image = UtilImageIO.loadImage(fullImagePath, classOf[GrayF32])
    val template = UtilImageIO.loadImage(partOfImagePath, classOf[GrayF32])
    findMatches(image, template, 1)
  }

  def doesImageContainPart(fullImagePath: String, partOfImagePath: String): Boolean = {
    findMatches(fullImagePath, partOfImagePath, 1).exists(_.score == -0.0)
  }
}
