package controllers

import javax.inject._
import julienrf.json.derived
import models.{Cell, Panel}
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() extends Controller {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def getPanel = Action {
    val panel: Panel = Panel(9)
    val panelJson: JsValue = Json.toJson(panel)
    println(panelJson)
    Ok(panelJson)
  }

}