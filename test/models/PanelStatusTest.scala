package models

import org.scalatest.{Matchers, FunSpec}

/**
  * Created by khn3193 on 4/26/16.
  */
class PanelStatusTest extends FunSpec with Matchers {

  describe("PanelStatus") {
    it("should give correct In progress status") {
      PanelStatus.IN_PROGRESS shouldBe "IN_PROGRESS"
    }

    it("should give correct failed status") {
      PanelStatus.FAILED shouldBe "FAILED"
    }

    it("should give correct success status") {
      PanelStatus.SUCCESS shouldBe "SUCCESS"
    }

  }
}
