package controllers

import javax.inject._
import models.PanelSubmit
import play.api.mvc._
import play.api.libs.json._
import services.PanelService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

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

  def getPanel = Action.async {
    val panel = panelService.newPanel(9)
    val futurePanel = Future(panel)
    futurePanel.map { result =>
      val panelJson = Json.toJson(result)
      Ok(panelJson)
    }
  }

  def submitPanel = Action(BodyParsers.parse.json) { request =>
    val panelBody = request.body.validate[PanelSubmit]
    panelBody.fold(
      errors => {
        BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toJson(errors)))
      },
      panelSubmit => {
        println("Panel received: " + panelSubmit)
        val panel = panelService.process(panelSubmit)
        val panelJson = Json.toJson(panel)
        println("Panel sent: " + panelJson)
        Ok(panelJson)
      }
    )
  }


}