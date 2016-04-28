package models

import play.api.libs.json.Json

/**
  * Created by khn3193 on 4/26/16.
  */
case class Grid(private val cells : List[List[Cell]]) {

  def cell(rowIndex: Int, colIndex: Int): Cell = cells(rowIndex)(colIndex)

  def rows: Int = cells.length

  def columns: Int = cells.head.length

  def afterMineClicked = {
    val newCells = cells.map(row => row.map(cell => {
      if(cell.value == Cell.MINE) cell.exposed else cell
    }))

    Grid(newCells)
  }

  def afterEmptyClicked(clickedCell: Cell) = {

    def loop(originalList: List[Cell], formedList: List[Cell]): List[Cell] = originalList match {
      case Nil => formedList
      case head :: tail if head.value != Cell.EMPTY => loop(tail, formedList)
      case head :: tail if head.value == Cell.EMPTY =>
        val adjCells = adjacentCells(head.rowIndex, head.colIndex, 1)
        val newCells = adjCells.map(cell => if(!formedList.contains(cell)) cell else Cell.INVALID_CELL)
        val validCells = newCells.filter(_ != Cell.INVALID_CELL)
        loop(tail ++ validCells, formedList ++ validCells)
    }

    val exposedCells = loop(List(clickedCell), List(clickedCell))

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

  def oneDimCellsWithoutMines = cells.flatten.filter(cell => cell.value != Cell.MINE)

  def flattenedCells = cells.flatten

  def getCells = cells

  def adjacentCells(currentRowIndex: Int, currentColumnIndex: Int, level: Int): List[Cell] = {
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

  override def toString: String = cells.flatMap(row => row.map(cell => cell.toString)).toSeq.toString()
}

object Grid {
  implicit val panelFormat = Json.format[Grid]
  val dimensionToMines = Map(81 -> 10, 256 -> 40)

  def apply(rows: Int, columns: Int, mines: Int): Grid = {
    val twoDimCellValues = generateTwoDimCellValues(rows, columns, mines)
    getGrid(twoDimCellValues)
  }

  def getGrid(gridValues: List[List[String]]): Grid = {
    val twoDimCellValues = gridValues.map(_.zipWithIndex).zipWithIndex

    val gridWithoutNumberCells = twoDimCellValues.map(row => {
      row._1.map(element => {
        Cell(element._1, row._2, element._2)
      })
    })

    val finalGrid = gridWithoutNumberCells.map(row => {
      row.map({ element =>
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

  private def generateTwoDimCellValues(rows: Int, columns: Int, mines: Int) : List[List[String]] = {
    val oneDimCellValues = generateOneDimCellValues(rows * columns, mines)

    def loop[A](formedList: List[List[A]], list : List[A]): List[List[A]] = list match {
      case Nil => formedList
      case _ => loop(formedList :+ list.take(columns), list.drop(columns))
    }

    loop(Nil, oneDimCellValues)
  }

  private def generateOneDimCellValues(size : Int, mines: Int) : List[String] = {
    val maxIndex = size - 1
    val rndLocations = randomLocations(mines)

    (0 to maxIndex).map(a => {
      if(rndLocations.contains(a)) Cell.MINE else Cell.EMPTY
    }).toList
  }

  private def randomLocations(n : Int): List[Int] = util.Random.shuffle(0 to 80).toList.take(n)

  def adjacentMineCount(grid: Grid, element: Cell): Int = {
    val adjCells = grid.adjacentCells(element.rowIndex, element.colIndex, 1)
    val adjCellValues = adjacentCellValues(adjCells)
    adjCellValues.map(isMine).sum
  }

  private def adjacentCellValues(list: List[Cell]) : List[String] = list.filter(_ != Cell.INVALID_CELL).map(c => c.value)

  private def isMine(s: String): Int = if (s == Cell.MINE) 1 else 0
}
