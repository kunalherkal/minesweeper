package models

import play.api.libs.json.Json

/**
  * Created by Kunal Herkal on 4/22/16.
  */
case class Panel(dimension: Int, grid: Array[Array[Cell]], status: String = "InProgress") {

  require(grid.size == dimension)

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

  def adjacentGrid(arr: Array[Array[Cell]], currentRowIndex: Int, currentColumnIndex: Int, level: Int) : List[String] = {
    val rows = arr.length
    val columns = arr(0).length

    val startRowIndex = currentRowIndex - level
    val endRowIndex = currentRowIndex + level
    val startColumnIndex = currentColumnIndex - level
    val endColumnIndex = currentColumnIndex + level

    (startRowIndex to endRowIndex).flatMap(row => (startColumnIndex to endColumnIndex).map(column => {
      val validElement = (row >= 0 && row < rows) && (column >= 0 && column < columns)
      if(validElement) arr(row)(column).value else " "
    })).toList
  }

  def adjacentMines(arr: Array[Array[Cell]], element: Cell): Int = {
    val l = adjacentGrid(arr, element.rowIndex, element.colIndex, 1)
    l map isMine sum
  }

  def isMine(s: String): Int = {
    if (s == "*") 1 else 0
  }
}