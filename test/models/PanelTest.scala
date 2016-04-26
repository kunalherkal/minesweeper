package models

import org.scalatest.{Matchers, FunSpec}

/**
  * Created by khn3193 on 4/22/16.
  */
class PanelTest extends FunSpec with Matchers {
  val cells2dAllCellEmpty = List(
    List(Cell(Cell.EMPTY, 0, 0), Cell(Cell.EMPTY, 0, 1)),
    List(Cell(Cell.EMPTY, 1, 0), Cell(Cell.EMPTY, 1, 1)))

  describe("Panel") {

    it("should create panel of dimension 9x9") {

      val panel = Panel(9)

      panel.grid.dimension shouldBe 9

      panel.grid.getCells.foreach(row => {
        row.length shouldBe 9
      })

      panel.dimension shouldBe 9
      panel.status shouldBe PanelStatus.IN_PROGRESS
    }

    it("should create panel of dimension 20x20") {

      val panel = Panel(20)

      panel.grid.dimension shouldBe 20

      panel.grid.getCells.foreach(row => {
        row.length shouldBe 20
      })

      panel.dimension shouldBe 20
      panel.status shouldBe PanelStatus.IN_PROGRESS
    }

    it("should create a panel with give grid") {
      val panel = Panel(Grid(cells2dAllCellEmpty))

      panel.grid.dimension shouldBe 2

      panel.grid.getCells.foreach(row => {
        row.length shouldBe 2
      })

      panel.dimension shouldBe 2
      panel.status shouldBe PanelStatus.IN_PROGRESS
    }

    it("should throw an exception when dimension mismatches the grid size") {
      intercept[IllegalArgumentException] {
        Panel(3, Grid(cells2dAllCellEmpty))
      }
    }
  }

    describe("Method panel.toString()") {
    it("should give proper String formatted panel") {
      val panel = Panel(2, Grid(cells2dAllCellEmpty))
      val panelString = panel.toString
      val expectedString = "Panel: [Grid: List(Cell( ,0,0,true), Cell( ,0,1,true), Cell( ,1,0,true), Cell( ,1,1,true))," +
        " Dimension: 2, Status: IN_PROGRESS]"

      panelString shouldBe expectedString
    }
  }

  describe("Method panel.isComplete") {

    it("should return false when panel isn't complete") {
      val cells = List(
        List(Cell("1", 0, 0), Cell(Cell.MINE, 0, 1), Cell("2", 0, 2)),
        List(Cell("2", 1, 0), Cell("3", 1, 1), Cell(Cell.MINE, 1, 2)),
        List(Cell(Cell.MINE, 2, 0), Cell("2", 2, 1), Cell("1", 2, 2)))

      val panel = Panel(3, Grid(cells))

      panel.isComplete shouldBe false
    }

    it("should return true when panel is complete") {
      val cells = List(
        List(Cell("1", 0, 0, hidden = false), Cell(Cell.MINE, 0, 1), Cell("2", 0, 2, hidden = false)),
        List(Cell("2", 1, 0, hidden = false), Cell("3", 1, 1, hidden = false), Cell(Cell.MINE, 1, 2)),
        List(Cell(Cell.MINE, 2, 0), Cell("2", 2, 1, hidden = false), Cell("1", 2, 2, hidden = false)))

      val panel = Panel(3, Grid(cells))

      panel.isComplete shouldBe true
    }
   }
}


