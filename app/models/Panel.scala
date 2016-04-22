package models

/**
  * Created by Kunal Herkal on 4/22/16.
  */
case class Panel(dimension: Int, grid: Array[Array[Cell]], status: Status = InProgress) {
  require(grid.size == dimension)
}

object Panel extends App {

  def apply(dimension : Int): Panel = {
    apply(dimension, Panel.newGrid(dimension))
  }

  def apply(grid: Array[Array[Cell]]): Panel = {
    apply(grid.size, grid)
  }

  private val emptySpace = ' '
  private val mine = '*'

  def generate(size : Int) : Array[Char] = {
    val maxIndex = size - 1
    val minePanel = (0 to maxIndex).map(a => emptySpace).toArray
    randomLocations(10).foreach(l => minePanel(l) = mine)
    minePanel
  }

  def generate2D(dimension: Int) : Array[Array[Char]] = {
    val panel1D = generate(dimension * dimension)

    def loop(formedList: List[List[Char]], list : List[Char]): List[List[Char]] = list match {
      case Nil => formedList
      case _ => loop(formedList :+ list.take(dimension), list.drop(dimension))
    }

    loop(Nil, panel1D.toList).map(row => row.toArray).toArray
  }

  def newGrid(dimension: Int) : Array[Array[Cell]] = {
    val arr2 = generate2D(dimension).map(_.zipWithIndex).zipWithIndex

    arr2.map(row => {
      row._1.map(element => {
        Cell(element._1, row._2, element._2)
      })
    })
  }

  def randomLocations(n : Int): Array[Int] = {
    util.Random.shuffle(0 to 80).toArray.take(n)
  }
}