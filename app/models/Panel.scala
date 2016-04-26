package models

import play.api.libs.json.Json
/**
  * Created by Kunal Herkal on 4/22/16.
  */
case class Panel(dimension: Int, grid: List[List[Cell]], status: String = PanelStatus.IN_PROGRESS) {

  require(grid.length == dimension)

  def solve(clickedRow:Int, clickedCol: Int): Panel = {
    val clickedCell = grid(clickedRow)(clickedCol)

    clickedCell match {
      case Cell(Cell.MINE, _, _, _) => mineClicked
      case Cell(Cell.EMPTY, _, _, _) => emptyClicked(clickedCell)
      case Cell(_, _, _, _) => numberClicked(clickedCell)
    }
  }

  private def mineClicked: Panel = {
    val newGrid = grid.map(row => row.map(cell => {
      if(cell.value == Cell.MINE) cell.exposed
      else cell
    }))
    Panel(dimension, newGrid, PanelStatus.FAILED)
  }

  private def emptyClicked(clickedCell : Cell) : Panel = {

    def loop(originalList: List[Cell], formedList: List[Cell]): List[Cell] = originalList match {
      case Nil => formedList
      case head :: tail if head.value != Cell.EMPTY => loop(tail, formedList)
      case head :: tail if head.value == Cell.EMPTY =>
        val adjCells = Panel.adjacentCells(grid, head.rowIndex, head.colIndex, 1)
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
    val arr = grid.flatMap(row => row.map(cell => cell.toString)).toSeq
    "Panel: [" + "Grid: " + arr.toString + ", Dimension: " + dimension + ", Status: " + status + "]"
  }
}

object Panel {
  implicit val panelFormat = Json.format[Panel]
  val dimensionToMines = Map(81 -> 10, 256 -> 40)

  def apply(dimension : Int): Panel = {
    apply(dimension, Panel.newGrid(dimension))
  }

  def apply(grid: List[List[Cell]]): Panel = {
    apply(grid.length, grid)
  }

  private def newGrid(dimension: Int) : List[List[Cell]] = {
    val twoDimCellValues = generate2D(dimension).map(_.zipWithIndex).zipWithIndex

    val gridWithoutNumberCells = twoDimCellValues.map(row => {
      row._1.map(element => {
        Cell(element._1, row._2, element._2)
      })
    })

    val finalGrid = gridWithoutNumberCells.map(row => {
      row.map({element =>
        element.value match {
          case Cell.MINE => Cell(Cell.MINE, element.rowIndex, element.colIndex)
          case _ =>
            val count = adjacentMines(gridWithoutNumberCells, element)
            count match {
              case 0 => Cell(Cell.EMPTY, element.rowIndex, element.colIndex)
              case _ => Cell(count.toString, element.rowIndex, element.colIndex)
            }
        }
      })
    })

    finalGrid
  }

  private def generate2D(dimension: Int) : List[List[String]] = {
    val oneDimCellValues = generate(dimension * dimension)

    def loop[A](formedList: List[List[A]], list : List[A]): List[List[A]] = list match {
      case Nil => formedList
      case _ => loop(formedList :+ list.take(dimension), list.drop(dimension))
    }

    loop(Nil, oneDimCellValues)
  }

  private def generate(size : Int) : List[String] = {
    val maxIndex = size - 1
    val mineCount = dimensionToMines.getOrElse(size, 0)
    val rndLocations = randomLocations(mineCount)

    (0 to maxIndex).map(a => {
     if(rndLocations.contains(a)) Cell.MINE else Cell.EMPTY
    }).toList
  }

  private def randomLocations(n : Int): List[Int] = {
    util.Random.shuffle(0 to 80).toList.take(n)
  }

  def adjacentMines(grid: List[List[Cell]], element: Cell): Int = {
    val adjCells = adjacentCells(grid, element.rowIndex, element.colIndex, 1)
    val adjCellValues = adjacentCellValues(adjCells)
    adjCellValues.map(isMine).sum
  }

  private def adjacentCells(grid: List[List[Cell]], currentRowIndex: Int, currentColumnIndex: Int, level: Int): List[Cell] = {
    val rows = grid.length
    val columns = grid.head.length

    val startRowIndex = currentRowIndex - level
    val endRowIndex = currentRowIndex + level
    val startColumnIndex = currentColumnIndex - level
    val endColumnIndex = currentColumnIndex + level

    (startRowIndex to endRowIndex).flatMap(row => (startColumnIndex to endColumnIndex).map(column => {
      val validElement = (row >= 0 && row < rows) && (column >= 0 && column < columns)
        if(validElement && !(row == currentRowIndex && column == currentColumnIndex)) {
          grid(row)(column)
        } else Cell.INVALID_CELL
    })).toList
  }

  private def adjacentCellValues(list: List[Cell]) : List[String] = {
    list.filter(_ != Cell.INVALID_CELL).map(c => c.value)
  }

  private def isMine(s: String): Int = {
    if (s == Cell.MINE) 1 else 0
  }
}

object PanelStatus {
  val IN_PROGRESS: String = "IN_PROGRESS"
  val SUCCESS: String = "SUCCESS"
  val FAILED: String = "FAILED"
}