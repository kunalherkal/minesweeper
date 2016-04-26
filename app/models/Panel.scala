package models

import play.api.libs.json.Json
/**
  * Created by Kunal Herkal on 4/22/16.
  */
case class Panel(dimension: Int, grid: Grid, status: String = PanelStatus.IN_PROGRESS) {

  require(grid.length == dimension)

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
    Panel(dimension, newGrid, PanelStatus.FAILED)
  }

  private def emptyClicked(clickedCell : Cell) : Panel = {

    def loop(originalList: List[Cell], formedList: List[Cell]): List[Cell] = originalList match {
      case Nil => formedList
      case head :: tail if head.value != Cell.EMPTY => loop(tail, formedList)
      case head :: tail if head.value == Cell.EMPTY =>
        val adjCells = Grid.adjacentCells(grid, head.rowIndex, head.colIndex, 1)
        val newCells = adjCells.map(cell => if(!formedList.contains(cell)) cell else Cell.INVALID_CELL)
        val validCells = newCells.filter(_ != Cell.INVALID_CELL)
        loop(tail ++ validCells, formedList ++ validCells)
    }

    val exposedCells: List[Cell] = loop(List(clickedCell), List(clickedCell))

    val newGrid = grid.afterEmptyClicked(exposedCells)

    val panel = Panel(dimension, newGrid)
    if(panel.isComplete) Panel(dimension, newGrid, PanelStatus.SUCCESS) else panel
  }

  private def numberClicked(clickedCell : Cell) : Panel = {
    val newGrid = grid.afterNumberClicked(clickedCell)

    val panel = Panel(dimension, newGrid)
    if(panel.isComplete) Panel(dimension, newGrid, PanelStatus.SUCCESS) else panel
  }

  def isComplete : Boolean = {
      grid.oneDimCellsWithoutMines.forall(cell => !cell.hidden)
  }

  override def toString: String = {
    "Panel: [" + "Grid: " + grid.toString + ", Dimension: " + dimension + ", Status: " + status + "]"
  }
}

object Panel {
  implicit val panelFormat = Json.format[Panel]

  def apply(dimension : Int): Panel = {
    apply(dimension, Grid.create(dimension))
  }

  def apply(grid: Grid): Panel = {
    apply(grid.length, grid)
  }
}
