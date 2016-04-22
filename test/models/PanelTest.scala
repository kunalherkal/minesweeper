package models

import org.scalatest.{Matchers, FunSpec}

/**
  * Created by khn3193 on 4/22/16.
  */
class PanelTest extends FunSpec with Matchers {

  describe("Panel") {

    it("should create panel of dimension 9x9") {

      val panel = Panel(9)

      panel.grid should not be empty
      assert(panel.grid.size == 9)

      panel.grid.foreach(row => {
        assert(row.size == 9)
      })

      assert(panel.dimension == 9)
      assert(panel.status == InProgress)
    }

    it("should create panel of dimension 20x20") {

      val panel = Panel(20)

      panel.grid should not be empty
      assert(panel.grid.size == 20)

      panel.grid.foreach(row => {
        assert(row.size == 20)
      })

      assert(panel.dimension == 20)
      assert(panel.status == InProgress)
    }

    it("should create a panel with give grid") {
      val grid = Array.ofDim[Cell](2, 2)
      grid(0)(0) = Cell(' ', 0, 0)
      grid(0)(1) = Cell(' ', 0, 1)
      grid(1)(0) = Cell(' ', 1, 0)
      grid(1)(1) = Cell(' ', 1, 1)

      val panel = Panel(grid)

      panel.grid should not be empty
      assert(panel.grid.size == 2)

      panel.grid.foreach(row => {
        assert(row.size == 2)
      })

      assert(panel.dimension == 2)
      assert(panel.status == InProgress)
    }

    it("should throw an exception") {
      val grid = Array.ofDim[Cell](2, 2)
      grid(0)(0) = Cell(' ', 0, 0)
      grid(0)(1) = Cell(' ', 0, 1)
      grid(1)(0) = Cell(' ', 1, 0)
      grid(1)(1) = Cell(' ', 1, 1)

      intercept[IllegalArgumentException] {
        val panel = Panel(3, grid)
      }

    }
  }


}


