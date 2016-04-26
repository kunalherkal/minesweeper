package models

import org.scalatest.{Matchers, FunSpec}

/**
  * Created by khn3193 on 4/26/16.
  */
class GridTest extends FunSpec with Matchers  {

  describe("Method adjacentMines") {
    it("should return 8 for given grid and cell") {
      val cells = List(
        List(Cell(Cell.MINE, 0, 0), Cell(Cell.MINE, 0, 1), Cell(Cell.MINE, 0, 2)),
        List(Cell(Cell.MINE, 1, 0), Cell(Cell.EMPTY, 1, 1), Cell(Cell.MINE, 1, 2)),
        List(Cell(Cell.MINE, 2, 0), Cell(Cell.MINE, 2, 1), Cell(Cell.MINE, 2, 2)))

      val count = Grid.adjacentMineCount(Grid(cells), Cell(Cell.EMPTY, 1, 1))

      count shouldBe 8
    }

    it("should return 3 for given grid and cell") {
      val cells = List(
        List(Cell(Cell.EMPTY, 0, 0), Cell(Cell.MINE, 0, 1), Cell(Cell.MINE, 0, 2)),
        List(Cell(Cell.MINE, 1, 0), Cell(Cell.EMPTY, 1, 1), Cell(Cell.MINE, 1, 2)),
        List(Cell(Cell.MINE, 2, 0), Cell(Cell.MINE, 2, 1), Cell(Cell.MINE, 2, 2)))

      val count = Grid.adjacentMineCount(Grid(cells), Cell(Cell.EMPTY, 0, 0))
      count shouldBe 2
    }

    it("should return 0 for given grid and cell") {
      val cells = List(
        List(Cell(Cell.EMPTY, 0, 0), Cell(Cell.EMPTY, 0, 1), Cell(Cell.EMPTY, 0, 2)),
        List(Cell(Cell.EMPTY, 1, 0), Cell(Cell.EMPTY, 1, 1), Cell(Cell.EMPTY, 1, 2)),
        List(Cell(Cell.EMPTY, 2, 0), Cell(Cell.EMPTY, 2, 1), Cell(Cell.EMPTY, 2, 2)))

      val count = Grid.adjacentMineCount(Grid(cells), Cell(Cell.EMPTY, 1, 1))
      count shouldBe 0
    }
  }

}
