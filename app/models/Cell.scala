package models

import play.api.libs.json.Json

/**
  * Created by Kunal Herkal on 4/22/16.
  */
case class Cell(value : String, rowIndex: Int, colIndex: Int, hidden : Boolean = true) {

  def exposed = Cell(value, rowIndex, colIndex, hidden = false)

}

object Cell {
  implicit val cellFormat = Json.format[Cell]
  val MINE: String = "*"
  val EMPTY: String = " "
}
