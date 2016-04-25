package models

import play.api.libs.json.Json
/**
  * Created by Kunal Herkal on 4/22/16.
  */
case class Panel(dimension: Int, grid: Array[Array[Cell]], status: String = "InProgress") {

  require(grid.length == dimension)

  def solve(clickedRow:Int, clickedCol: Int): Panel = {
    val clickedCell = grid(clickedRow)(clickedCol)

    clickedCell match {
      case Cell("*", _, _, _) => mineClicked()
      case Cell(" ", _, _, _) => emptyClicked(clickedCell)
      case Cell(_, _, _, _) => numberClicked(clickedCell)
      case _ => throw new IllegalArgumentException("Unexpected input")
    }
  }

  def mineClicked() : Panel = {
    val newGrid = grid.map(row => row.map(cell => {
      if(cell.value == "*") Cell(cell.value, cell.rowIndex, cell.colIndex, hidden = false)
      else cell
    }))
    Panel(dimension, newGrid, "Failed")
  }

  def emptyClicked(clickedCell : Cell) : Panel = {

    def loop(originalList: List[Cell], formedList: List[Cell]): List[Cell] = originalList match {
      case Nil => formedList
      case head :: tail if head.value != " " => loop(tail, formedList)
      case head :: tail if head.value == " " =>
        val adjCells = Panel.adjacentCells(grid, head.rowIndex, head.colIndex, 1)
        val newCells = adjCells.map(c => if(!formedList.contains(c)) c else Cell("K", 999, 999))
        val validCells = newCells.filter(c => c.value != "K")
        loop(tail ++ validCells, formedList ++ validCells)
    }


    val exposedCells = loop(List(clickedCell), List(clickedCell))

    val newGrid = grid.map(row => row.map(cell => {
      if(exposedCells.contains(cell)) Cell(cell.value, cell.rowIndex, cell.colIndex, false)
      else cell
    }))

    Panel(dimension, newGrid, "In Progress")
  }

  def numberClicked(clickedCell : Cell) : Panel = {
    val newGrid = grid.map(row => row.map(cell =>
      cell match {
        case `clickedCell` => Cell(cell.value, cell.rowIndex, cell.colIndex, hidden = false)
        case _ => cell
      }
    ))
    Panel(dimension, newGrid, "In Progress")
  }

  override def toString: String = {
    val arr = grid.flatMap(row => row.map(cell => cell.toString)).toSeq
    "Grid: \n" + arr.toString.drop(12) + "\nDimension: " + dimension //+ "\nStatus: " + status
  }
}

object Panel {
  implicit val panelFormat = Json.format[Panel]

  def apply(dimension : Int): Panel = {
    apply(dimension, Panel.newGrid(dimension))
  }

  def apply(grid: Array[Array[Cell]]): Panel = {
    apply(grid.length, grid)
  }

  def generate(size : Int) : Array[String] = {
    val maxIndex = size - 1
    val minePanel = (0 to maxIndex).map(a => " ").toArray
    randomLocations(10).foreach(l => minePanel(l) = "*")
    minePanel
  }

  def generate2D(dimension: Int) : Array[Array[String]] = {
    val panel1D = generate(dimension * dimension)

    def loop(formedList: List[List[String]], list : List[String]): List[List[String]] = list match {
      case Nil => formedList
      case _ => loop(formedList :+ list.take(dimension), list.drop(dimension))
    }

    loop(Nil, panel1D.toList).map(row => row.toArray).toArray
  }

  def newGrid(dimension: Int) : Array[Array[Cell]] = {
    val arr2 = generate2D(dimension).map(_.zipWithIndex).zipWithIndex

    val array2D = arr2.map(row => {
      row._1.map(element => {
        Cell(element._1, row._2, element._2)
      })
    })

    array2D.map(row => {
      row.map({element =>
        element.value match {
          case "*" => Cell("*", element.rowIndex, element.colIndex)
          case _ =>
            val count = adjacentMines(array2D, element)
            count match {
              case 0 => Cell(" ", element.rowIndex, element.colIndex)
              case _ => Cell(count.toString, element.rowIndex, element.colIndex)
            }
        }
      })
    })
  }

  def randomLocations(n : Int): Array[Int] = {
    util.Random.shuffle(0 to 80).toArray.take(n)
  }

  def adjacentCellValues(list: List[Cell]) : List[String] = {
    list.filter(c => c.value != "K").map(c => c.value)
  }

  def adjacentCells(arr: Array[Array[Cell]], currentRowIndex: Int, currentColumnIndex: Int, level: Int): List[Cell] = {
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
        } else Cell("K", 999, 999, hidden = true)
    })).toList
  }

  def adjacentMines(arr: Array[Array[Cell]], element: Cell): Int = {
    val adjCells = adjacentCells(arr, element.rowIndex, element.colIndex, 1)
    val adjCellValues = adjacentCellValues(adjCells)
    adjCellValues map isMine sum
  }

  def isMine(s: String): Int = {
    if (s == "*") 1 else 0
  }
}