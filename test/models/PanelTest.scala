package models

import org.scalatest.{Matchers, FunSpec}

/**
  * Created by khn3193 on 4/22/16.
  */
class PanelTest extends FunSpec with Matchers {
  val grid2dAllCellEmpty = List(
    List(Cell(Cell.EMPTY, 0, 0), Cell(Cell.EMPTY, 0, 1)),
    List(Cell(Cell.EMPTY, 1, 0), Cell(Cell.EMPTY, 1, 1)))

  describe("Method adjacentMines") {
    it("should return 8 for given grid and cell") {
      val grid = List(
        List(Cell(Cell.MINE, 0, 0), Cell(Cell.MINE, 0, 1), Cell(Cell.MINE, 0, 2)),
        List(Cell(Cell.MINE, 1, 0), Cell(Cell.EMPTY, 1, 1), Cell(Cell.MINE, 1, 2)),
        List(Cell(Cell.MINE, 2, 0), Cell(Cell.MINE, 2, 1), Cell(Cell.MINE, 2, 2)))

      val count = Panel.adjacentMines(grid, Cell(Cell.EMPTY, 1, 1))

      count shouldBe 8
    }

    it("should return 3 for given grid and cell") {
      val grid = List(
        List(Cell(Cell.EMPTY, 0, 0), Cell(Cell.MINE, 0, 1), Cell(Cell.MINE, 0, 2)),
        List(Cell(Cell.MINE, 1, 0), Cell(Cell.EMPTY, 1, 1), Cell(Cell.MINE, 1, 2)),
        List(Cell(Cell.MINE, 2, 0), Cell(Cell.MINE, 2, 1), Cell(Cell.MINE, 2, 2)))

      val count = Panel.adjacentMines(grid, Cell(Cell.EMPTY, 0, 0))
      count shouldBe 2
    }

    it("should return 0 for given grid and cell") {
      val grid = List(
        List(Cell(Cell.EMPTY, 0, 0), Cell(Cell.EMPTY, 0, 1), Cell(Cell.EMPTY, 0, 2)),
        List(Cell(Cell.EMPTY, 1, 0), Cell(Cell.EMPTY, 1, 1), Cell(Cell.EMPTY, 1, 2)),
        List(Cell(Cell.EMPTY, 2, 0), Cell(Cell.EMPTY, 2, 1), Cell(Cell.EMPTY, 2, 2)))

      val count = Panel.adjacentMines(grid, Cell(Cell.EMPTY, 1, 1))
      count shouldBe 0
    }
  }

  describe("Panel") {

    it("should create panel of dimension 9x9") {

      val panel = Panel(9)

      panel.grid should not be empty
      panel.grid.length shouldBe 9

      panel.grid.foreach(row => {
        row.length shouldBe 9
      })

      panel.dimension shouldBe 9
      panel.status shouldBe PanelStatus.IN_PROGRESS
    }

    it("should create panel of dimension 20x20") {

      val panel = Panel(20)

      panel.grid should not be empty
      panel.grid.length shouldBe 20

      panel.grid.foreach(row => {
        row.length shouldBe 20
      })

      panel.dimension shouldBe 20
      panel.status shouldBe PanelStatus.IN_PROGRESS
    }

    it("should create a panel with give grid") {
      val panel = Panel(grid2dAllCellEmpty)

      panel.grid should not be empty
      panel.grid.length shouldBe 2

      panel.grid.foreach(row => {
        row.length shouldBe 2
      })

      panel.dimension shouldBe 2
      panel.status shouldBe PanelStatus.IN_PROGRESS
    }

    it("should throw an exception") {
      intercept[IllegalArgumentException] {
        Panel(3, grid2dAllCellEmpty)
      }
    }

    it("should give proper String formatted panel") {
      val panel = Panel(2, grid2dAllCellEmpty)
      val panelString = panel.toString
      val expectedString = "Panel: [Grid: List(Cell( ,0,0,true), Cell( ,0,1,true), Cell( ,1,0,true), Cell( ,1,1,true))," +
        " Dimension: 2, Status: IN_PROGRESS]"

      panelString shouldBe expectedString
    }
  }
}


