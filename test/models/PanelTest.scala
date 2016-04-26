package models

import org.scalatest.{Matchers, FunSpec}

/**
  * Created by khn3193 on 4/22/16.
  */
class PanelTest extends FunSpec with Matchers {

  describe("Method adjacentMines") {
    it("should return 8 for given grid and cell") {
      val grid = Array.ofDim[Cell](3, 3)
      grid(0)(0) = Cell("*", 0, 0)
      grid(0)(1) = Cell("*", 0, 1)
      grid(0)(2) = Cell("*", 0, 2)
      grid(1)(0) = Cell("*", 1, 0)
      grid(1)(1) = Cell(" ", 1, 1)
      grid(1)(2) = Cell("*", 1, 2)
      grid(2)(0) = Cell("*", 2, 0)
      grid(2)(1) = Cell("*", 2, 1)
      grid(2)(2) = Cell("*", 2, 2)

      val count = Panel.adjacentMines(grid, Cell(" ", 1, 1))

      count shouldBe 8
    }

    it("should return 3 for given grid and cell") {
      val grid = Array.ofDim[Cell](3, 3)
      grid(0)(0) = Cell(" ", 0, 0)
      grid(0)(1) = Cell("*", 0, 1)
      grid(0)(2) = Cell("*", 0, 2)
      grid(1)(0) = Cell("*", 1, 0)
      grid(1)(1) = Cell(" ", 1, 1)
      grid(1)(2) = Cell("*", 1, 2)
      grid(2)(0) = Cell("*", 2, 0)
      grid(2)(1) = Cell("*", 2, 1)
      grid(2)(2) = Cell("*", 2, 2)

      val count = Panel.adjacentMines(grid, Cell(" ", 0, 0))
      count shouldBe 2
    }

    it("should return 0 for given grid and cell") {
      val grid = Array.ofDim[Cell](3, 3)
      grid(0)(0) = Cell(" ", 0, 0)
      grid(0)(1) = Cell(" ", 0, 1)
      grid(0)(2) = Cell(" ", 0, 2)
      grid(1)(0) = Cell(" ", 1, 0)
      grid(1)(1) = Cell(" ", 1, 1)
      grid(1)(2) = Cell(" ", 1, 2)
      grid(2)(0) = Cell(" ", 2, 0)
      grid(2)(1) = Cell(" ", 2, 1)
      grid(2)(2) = Cell(" ", 2, 2)

      val count = Panel.adjacentMines(grid, Cell(" ", 1, 1))
      count shouldBe 0
    }
  }

  describe("Panel") {

    it("should create panel of dimension 9x9") {

      val panel = Panel(9)

      panel.grid should not be empty
      assert(panel.grid.length == 9)

      panel.grid.foreach(row => {
        assert(row.length == 9)
      })


      assert(panel.dimension == 9)
      assert(panel.status == PanelStatus.IN_PROGRESS)
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
      val grid = Array.ofDim[Cell](2, 2)
      grid(0)(0) = Cell(" ", 0, 0)
      grid(0)(1) = Cell(" ", 0, 1)
      grid(1)(0) = Cell(" ", 1, 0)
      grid(1)(1) = Cell(" ", 1, 1)

      val panel = Panel(grid)

      panel.grid should not be empty
      panel.grid.length shouldBe 2

      panel.grid.foreach(row => {
        row.length shouldBe 2
      })

      panel.dimension shouldBe 2
      panel.status shouldBe PanelStatus.IN_PROGRESS
    }

    it("should throw an exception") {
      val grid = Array.ofDim[Cell](2, 2)
      grid(0)(0) = Cell(" ", 0, 0)
      grid(0)(1) = Cell(" ", 0, 1)
      grid(1)(0) = Cell(" ", 1, 0)
      grid(1)(1) = Cell(" ", 1, 1)

      intercept[IllegalArgumentException] {
        Panel(3, grid)
      }

    }

    it("should give proper String formatted panel") {
      val grid = Array.ofDim[Cell](2, 2)
      grid(0)(0) = Cell(" ", 0, 0)
      grid(0)(1) = Cell(" ", 0, 1)
      grid(1)(0) = Cell(" ", 1, 0)
      grid(1)(1) = Cell(" ", 1, 1)

      val panel = Panel(2, grid)
      val panelString = panel.toString
      val expectedString = "Panel: [Grid: (Cell( ,0,0,true), Cell( ,0,1,true), Cell( ,1,0,true), Cell( ,1,1,true))," +
        " Dimension: 2, Status: IN_PROGRESS]"

      panelString shouldBe expectedString
    }
  }
}


