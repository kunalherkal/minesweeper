package models

import play.api.libs.json.Json
/**
  * Created by Kunal Herkal on 4/22/16.
  */
case class Panel(grid: Grid, rows: Int, columns: Int, mines: Int, status: String = PanelStatus.IN_PROGRESS) {

  require(grid.rows == rows)
  require(grid.columns == columns)

  def processClick(clickedRow:Int, clickedCol: Int): Panel = {
    val clickedCell = grid.cell(clickedRow, clickedCol)

    clickedCell match {
      case Cell(Cell.MINE, _, _, _) => mineClicked
      case Cell(Cell.EMPTY, _, _, _) => emptyClicked(clickedCell)
      case Cell(_, _, _, _) => numberClicked(clickedCell)
    }
  }

  private def mineClicked: Panel = {
    val newGrid = grid.afterMineClicked
    Panel(newGrid, rows, columns, mines, PanelStatus.FAILED)
  }

  private def emptyClicked(clickedCell : Cell) : Panel = {
    val newGrid = grid.afterEmptyClicked(clickedCell)

    val panel = Panel(newGrid, rows, columns, mines)
    if(panel.isComplete) Panel(newGrid, rows, columns, mines, PanelStatus.SUCCESS) else panel
  }

  private def numberClicked(clickedCell : Cell) : Panel = {
    val newGrid = grid.afterNumberClicked(clickedCell)

    val panel = Panel(newGrid, rows, columns, mines)
    if(panel.isComplete) Panel(newGrid, rows, columns, mines, PanelStatus.SUCCESS) else panel
  }

  def isComplete : Boolean = {
      grid.oneDimCellsWithoutMines.forall(cell => !cell.hidden)
  }

  override def toString: String = {
    "Panel: [" + "Grid: " + grid.toString + ", Rows: " + rows + ", Columns: " + columns + ", Status: " + status + "]"
  }
}

object Panel {
  implicit val panelFormat = Json.format[Panel]

  def apply(rows : Int, columns: Int, mines: Int): Panel = {
    apply(Grid(rows, columns, mines), rows, columns, mines)
  }
}
