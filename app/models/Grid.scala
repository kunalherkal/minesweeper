package models

import play.api.libs.json.Json

/**
  * Created by khn3193 on 4/26/16.
  */
case class Grid(cells : List[List[Cell]]) {

  def cell(rowIndex: Int, colIndex: Int): Cell = cells(rowIndex)(colIndex)

  def length: Int = cells.length

  def afterMineClicked = {
    val newCells = cells.map(row => row.map(cell => {
      if(cell.value == Cell.MINE) cell.exposed else cell
    }))

    Grid(newCells)
  }

  def afterEmptyClicked(exposedCells : List[Cell]) = {
    val newCells = cells.map(row => row.map(cell => {
      if(exposedCells.contains(cell)) cell.exposed
      else cell
    }))

    Grid(newCells)
  }

  def afterNumberClicked(clickedCell: Cell) = {
    val newCells = cells.map(row => row.map({
      case `clickedCell` => clickedCell.exposed
      case cell => cell
    }))

    Grid(newCells)
  }

  def oneDimCellsWithoutMines = {
    cells.flatten.filter(cell => cell.value != Cell.MINE)
  }

  override def toString: String = {
    cells.flatMap(row => row.map(cell => cell.toString)).toSeq.toString()
  }
}

object Grid {
  implicit val panelFormat = Json.format[Grid]
  val dimensionToMines = Map(81 -> 10, 256 -> 40)

  def create(dimension: Int) : Grid = {
    val twoDimCellValues = generateTwoDimCellValues(dimension).map(_.zipWithIndex).zipWithIndex

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
            val count = adjacentMineCount(Grid(gridWithoutNumberCells), element)
            count match {
              case 0 => Cell(Cell.EMPTY, element.rowIndex, element.colIndex)
              case _ => Cell(count.toString, element.rowIndex, element.colIndex)
            }
        }
      })
    })

    Grid(finalGrid)
  }

  private def generateTwoDimCellValues(dimension: Int) : List[List[String]] = {
    val oneDimCellValues = generateOneDimCellValues(dimension * dimension)

    def loop[A](formedList: List[List[A]], list : List[A]): List[List[A]] = list match {
      case Nil => formedList
      case _ => loop(formedList :+ list.take(dimension), list.drop(dimension))
    }

    loop(Nil, oneDimCellValues)
  }

  private def generateOneDimCellValues(size : Int) : List[String] = {
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

  def adjacentMineCount(grid: Grid, element: Cell): Int = {
    val adjCells = adjacentCells(grid, element.rowIndex, element.colIndex, 1)
    val adjCellValues = adjacentCellValues(adjCells)
    adjCellValues.map(isMine).sum
  }

  def adjacentCells(grid: Grid, currentRowIndex: Int, currentColumnIndex: Int, level: Int): List[Cell] = {
    val cells = grid.cells
    val rows = cells.length
    val columns = cells.head.length

    val startRowIndex = currentRowIndex - level
    val endRowIndex = currentRowIndex + level
    val startColumnIndex = currentColumnIndex - level
    val endColumnIndex = currentColumnIndex + level

    (startRowIndex to endRowIndex).flatMap(row => (startColumnIndex to endColumnIndex).map(column => {
      val validElement = (row >= 0 && row < rows) && (column >= 0 && column < columns)
      if(validElement && !(row == currentRowIndex && column == currentColumnIndex)) {
        cells(row)(column)
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
