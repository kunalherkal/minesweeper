package controllers

import javax.inject._
import models.PanelSubmit
import play.api.mvc._
import play.api.libs.json._
import services.PanelService

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(panelService: PanelService) extends Controller {

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
    val panel = panelService.newPanel(9)
    Ok(Json.toJson(panel))
  }

  def submitPanel = Action(BodyParsers.parse.json) { request =>
    val panelBody = request.body.validate[PanelSubmit]
    panelBody.fold(
      errors => {
        BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toJson(errors)))
      },
      panelSubmit => {
        val panel = panelService.process(panelSubmit)
        Ok(Json.toJson(panel))
      }
    )
  }


}