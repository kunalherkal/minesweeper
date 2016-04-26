package models

import play.api.libs.json.Json
/**
  * Created by Kunal Herkal on 4/22/16.
  */
case class Panel(dimension: Int, grid: List[List[Cell]], status: String = PanelStatus.IN_PROGRESS) {

  require(grid.length == dimension)

  def processClick(clickedRow:Int, clickedCol: Int): Panel = {
    val clickedCell = grid(clickedRow)(clickedCol)

    clickedCell match {
      case Cell(Cell.MINE, _, _, _) => mineClicked
      case Cell(Cell.EMPTY, _, _, _) => emptyClicked(clickedCell)
      case Cell(_, _, _, _) => numberClicked(clickedCell)
    }
  }

  private def mineClicked: Panel = {
    val newGrid = grid.map(row => row.map(cell => {
      if(cell.value == Cell.MINE) cell.exposed else cell
    }))
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

    val exposedCells = loop(List(clickedCell), List(clickedCell))

    val newGrid = grid.map(row => row.map(cell => {
      if(exposedCells.contains(cell)) cell.exposed
      else cell
    }))

    val panel = Panel(dimension, newGrid)
    if(panel.isComplete) Panel(dimension, newGrid, PanelStatus.SUCCESS) else panel
  }

  private def numberClicked(clickedCell : Cell) : Panel = {
    val newGrid = grid.map(row => row.map({
        case `clickedCell` => clickedCell.exposed
        case cell => cell
      }))

    val panel = Panel(dimension, newGrid)
    if(panel.isComplete) Panel(dimension, newGrid, PanelStatus.SUCCESS) else panel
  }

  def isComplete : Boolean = {
    val oneDimCellsWithoutMines = grid.flatten.filter(cell => cell.value != Cell.MINE)
    oneDimCellsWithoutMines.forall(cell => !cell.hidden)
  }

  override def toString: String = {
    val oneDimGrid = grid.flatMap(row => row.map(cell => cell.toString)).toSeq
    "Panel: [" + "Grid: " + oneDimGrid.toString + ", Dimension: " + dimension + ", Status: " + status + "]"
  }
}

object Panel {
  implicit val panelFormat = Json.format[Panel]

  def apply(dimension : Int): Panel = {
    apply(dimension, Grid.create(dimension))
  }

  def apply(grid: List[List[Cell]]): Panel = {
    apply(grid.length, grid)
  }
}
