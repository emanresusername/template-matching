package my.will.be.done.templatematching.client

import scala.scalajs.js
import org.scalajs.dom.document
import com.thoughtworks.binding.dom

object TemplateMatchingClient extends js.JSApp {
  @dom
  def render = {
    <form action="/api" method="post" enctype="multipart/form-data">
      <div>
      Full image:
        <input name="full" type="file"/>
      </div>
      <div>
      Image part to search for:
        <input name="part" type="file"/>
      </div>
      <input type="submit" value="Search"/>
    </form>
  }

  def main(): Unit = {
    dom.render(document.body, render)
  }
}
