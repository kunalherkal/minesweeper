package models

import play.api.libs.json.Json

/**
  * Created by Kunal Herkal on 4/25/16.
  */
case class PanelSubmit(panel: Panel, clickedRowIndex: Int, clickedColIndex: Int)

object PanelSubmit {
  implicit val panelSubmitFormat = Json.format[PanelSubmit]
}
