package models

import org.scalatest.{Matchers, FunSpec}

/**
  * Created by khn3193 on 4/26/16.
  */
class CellTest extends FunSpec with Matchers{

  describe("method exposed") {
    it("should return new cell with flag hidden false for given cell") {
      val inputCell = Cell("*", 999, 9999)
      val outputCell = inputCell.exposed

      outputCell shouldNot equal (inputCell)
      outputCell should not be inputCell
      outputCell.hidden shouldBe false
      outputCell.value shouldBe "*"
      outputCell.rowIndex shouldBe 999
      outputCell.colIndex shouldBe 9999

    }
  }

  describe("Object Cell") {
    it("should give correct Mine value") {
      Cell.EMPTY shouldBe " "
    }

    it("should give correct empty space value") {
      Cell.MINE shouldBe "*"
    }

    it("should give correct invalid cell") {
      Cell.INVALID_CELL shouldBe Cell("K", 999, 999, hidden = true)
    }
  }

}
