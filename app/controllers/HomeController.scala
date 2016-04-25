package controllers

import javax.inject._
import models.{PanelSubmit, Panel}
import play.api.mvc._
import play.api.libs.json._

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

  def submitPanel = Action(BodyParsers.parse.json) { request =>
    val panelBody = request.body.validate[PanelSubmit]
    panelBody.fold(
      errors => {
        BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toJson(errors)))
      },
      panel => {
        println("Panel received: " + panel)
        Ok(Json.obj("status" ->"OK", "message" -> ("Submitted Panel '" + panel + "' saved.") ))
      }
    )
  }


}