package models

import play.api.libs.json.Json
/**
  * Created by Kunal Herkal on 4/22/16.
  */
case class Panel(dimension: Int, grid: Array[Array[Cell]], status: String = PanelStatus.IN_PROGRESS) {

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
        val newCells = adjCells.map(c => if(!formedList.contains(c)) c else Cell.INVALID_CELL)
        val validCells = newCells.filter(_ != Cell.INVALID_CELL)
        loop(tail ++ validCells, formedList ++ validCells)
    }


    val exposedCells = loop(List(clickedCell), List(clickedCell))

    val newGrid = grid.map(row => row.map(cell => {
      if(exposedCells.contains(cell)) cell.exposed
      else cell
    }))

    Panel(dimension, newGrid, PanelStatus.IN_PROGRESS)
  }

  private def numberClicked(clickedCell : Cell) : Panel = {
    val newGrid: Array[Array[Cell]] = grid.map(row => row.map({
        case `clickedCell` => clickedCell.exposed
        case cell => cell
      }))
    Panel(dimension, newGrid, PanelStatus.IN_PROGRESS)
  }

  override def toString: String = {
    val arr = grid.flatMap(row => row.map(cell => cell.toString)).toSeq
    "Panel: [" + "Grid: " + arr.toString.drop(12) + ", Dimension: " + dimension + ", Status: " + status + "]"
  }
}

object Panel {
  implicit val panelFormat = Json.format[Panel]
  val dimensionToMines = Map(81 -> 10, 256 -> 40)

  def apply(dimension : Int): Panel = {
    apply(dimension, Panel.newGrid(dimension))
  }

  def apply(grid: Array[Array[Cell]]): Panel = {
    apply(grid.length, grid)
  }

  private def newGrid(dimension: Int) : Array[Array[Cell]] = {
    val arr2 = generate2D(dimension).map(_.zipWithIndex).zipWithIndex

    val array2D = arr2.map(row => {
      row._1.map(element => {
        Cell(element._1, row._2, element._2)
      })
    })

    array2D.map(row => {
      row.map({element =>
        element.value match {
          case Cell.MINE => Cell(Cell.MINE, element.rowIndex, element.colIndex)
          case _ =>
            val count = adjacentMines(array2D, element)
            count match {
              case 0 => Cell(Cell.EMPTY, element.rowIndex, element.colIndex)
              case _ => Cell(count.toString, element.rowIndex, element.colIndex)
            }
        }
      })
    })
  }

  private def generate2D(dimension: Int) : Array[Array[String]] = {
    val panel1D = generate(dimension * dimension)

    def loop[A](formedList: List[List[A]], list : List[A]): List[List[A]] = list match {
      case Nil => formedList
      case _ => loop(formedList :+ list.take(dimension), list.drop(dimension))
    }

    loop(Nil, panel1D.toList).map(row => row.toArray).toArray
  }

  private def generate(size : Int) : Array[String] = {
    val maxIndex = size - 1
    val mineCount = dimensionToMines.getOrElse(size, 0)
    val rndLocations = randomLocations(mineCount)

    (0 to maxIndex).map(a => {
     if(rndLocations.contains(a)) Cell.MINE else Cell.EMPTY
    }).toArray
  }

  private def randomLocations(n : Int): Array[Int] = {
    util.Random.shuffle(0 to 80).toArray.take(n)
  }

  def adjacentMines(arr: Array[Array[Cell]], element: Cell): Int = {
    val adjCells = adjacentCells(arr, element.rowIndex, element.colIndex, 1)
    val adjCellValues = adjacentCellValues(adjCells)
    adjCellValues.map(isMine).sum
  }

  private def adjacentCells(arr: Array[Array[Cell]], currentRowIndex: Int, currentColumnIndex: Int, level: Int): List[Cell] = {
    val rows = arr.length
    val columns = arr(0).length

    val startRowIndex = currentRowIndex - level
    val endRowIndex = currentRowIndex + level
    val startColumnIndex = currentColumnIndex - level
    val endColumnIndex = currentColumnIndex + level

    (startRowIndex to endRowIndex).flatMap(row => (startColumnIndex to endColumnIndex).map(column => {
      val validElement = (row >= 0 && row < rows) && (column >= 0 && column < columns)
        if(validElement && !(row == currentRowIndex && column == currentColumnIndex)) {
          arr(row)(column)
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