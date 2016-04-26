package services

import models.{PanelStatus, Panel, PanelSubmit, Cell}
import org.scalatest.{Matchers, FunSpec}

/**
  * Created by khn3193 on 4/25/16.
  */
class PanelServiceTest extends FunSpec with Matchers {

  describe("method process") {
    val grid = List(
      List(Cell(" ",0,0,true), Cell(" ",0,1,true), Cell(" ",0,2,true), Cell("1",0,3,true), Cell("1",0,4,true), Cell("1",0,5,true), Cell(" ",0,6,true), Cell(" ",0,7,true), Cell(" ",0,8,true)),
      List(Cell("1",1,0,true), Cell("1",1,1,true), Cell("2",1,2,true), Cell("2",1,3,true), Cell("*",1,4,true), Cell("1",1,5,true), Cell(" ",1,6,true), Cell(" ",1,7,true), Cell(" ",1,8,true)),
      List(Cell("1",2,0,true), Cell("*",2,1,true), Cell("2",2,2,true), Cell("*",2,3,true), Cell("3",2,4,true), Cell("2",2,5,true), Cell("1",2,6,true), Cell(" ",2,7,true), Cell(" ",2,8,true)),
      List(Cell("2",3,0,true), Cell("2",3,1,true), Cell("2",3,2,true), Cell("1",3,3,true), Cell("2",3,4,true), Cell("*",3,5,true), Cell("1",3,6,true), Cell("1",3,7,true), Cell("1",3,8,true)),
      List(Cell("*",4,0,true), Cell("1",4,1,true), Cell(" ",4,2,true), Cell(" ",4,3,true), Cell("2",4,4,true), Cell("2",4,5,true), Cell("3",4,6,true), Cell("*",4,7,true), Cell("1",4,8,true)),
      List(Cell("1",5,0,true), Cell("1",5,1,true), Cell(" ",5,2,true), Cell(" ",5,3,true), Cell("1",5,4,true), Cell("*",5,5,true), Cell("2",5,6,true), Cell("2",5,7,true), Cell("2",5,8,true)),
      List(Cell("1",6,0,true), Cell("1",6,1,true), Cell("1",6,2,true), Cell(" ",6,3,true), Cell("1",6,4,true), Cell("2",6,5,true), Cell("2",6,6,true), Cell("2",6,7,true), Cell("*",6,8,true)),
      List(Cell("1",7,0,true), Cell("*",7,1,true), Cell("1",7,2,true), Cell(" ",7,3,true), Cell(" ",7,4,true), Cell("1",7,5,true), Cell("*",7,6,true), Cell("2",7,7,true), Cell("1",7,8,true)),
      List(Cell("1",8,0,true), Cell("1",8,1,true), Cell("1",8,2,true), Cell(" ",8,3,true), Cell(" ",8,4,true), Cell("1",8,5,true), Cell("1",8,6,true), Cell("1",8,7,true), Cell(" ",8,8,true)))

    val panel = Panel(9, grid)
    it("should return panel with state as failed") {
      val panelService = new PanelService
      val panelSubmit = PanelSubmit(panel, 4, 0)
      val outputPanel = panelService.process(panelSubmit)

      assert(outputPanel.status == PanelStatus.FAILED)
    }

    it("should return panel with clicked cell as hidden false") {
      val panelService = new PanelService
      val panelSubmit = PanelSubmit(panel, 1, 0)
      val outputPanel = panelService.process(panelSubmit)

      assert(outputPanel.status == PanelStatus.IN_PROGRESS)
      assert(outputPanel.grid(1)(0).hidden == false)
    }

    it("should return panel with clicked and adjacent cells as hidden false for 0,0") {
      val panelService = new PanelService
      val panelSubmit = PanelSubmit(panel, 0, 0)
      val outputPanel = panelService.process(panelSubmit)

      assert(outputPanel.status == PanelStatus.IN_PROGRESS)
      assert(outputPanel.grid(0)(0).hidden == false)
      assert(outputPanel.grid(0)(1).hidden == false)
      assert(outputPanel.grid(0)(2).hidden == false)
      assert(outputPanel.grid(0)(3).hidden == false)
      assert(outputPanel.grid(1)(0).hidden == false)
      assert(outputPanel.grid(1)(1).hidden == false)
      assert(outputPanel.grid(1)(2).hidden == false)
      assert(outputPanel.grid(1)(3).hidden == false)
    }

    it("should return panel with clicked and adjacent cells as hidden false for 4, 2") {
      val panelService = new PanelService
      val panelSubmit = PanelSubmit(panel, 4, 2)
      val outputPanel = panelService.process(panelSubmit)

      assert(outputPanel.status == PanelStatus.IN_PROGRESS)
      assert(outputPanel.grid(3)(1).hidden == false)
      assert(outputPanel.grid(3)(2).hidden == false)
      assert(outputPanel.grid(3)(3).hidden == false)
      assert(outputPanel.grid(3)(4).hidden == false)
      assert(outputPanel.grid(4)(1).hidden == false)
      assert(outputPanel.grid(4)(2).hidden == false)
      assert(outputPanel.grid(4)(3).hidden == false)
      assert(outputPanel.grid(4)(4).hidden == false)
      assert(outputPanel.grid(5)(1).hidden == false)
      assert(outputPanel.grid(5)(2).hidden == false)
      assert(outputPanel.grid(5)(3).hidden == false)
      assert(outputPanel.grid(5)(4).hidden == false)
      assert(outputPanel.grid(6)(1).hidden == false)
      assert(outputPanel.grid(6)(2).hidden == false)
      assert(outputPanel.grid(6)(3).hidden == false)
      assert(outputPanel.grid(6)(4).hidden == false)
      assert(outputPanel.grid(6)(5).hidden == false)
      assert(outputPanel.grid(7)(2).hidden == false)
      assert(outputPanel.grid(7)(3).hidden == false)
      assert(outputPanel.grid(7)(4).hidden == false)
      assert(outputPanel.grid(7)(5).hidden == false)
      assert(outputPanel.grid(8)(2).hidden == false)
      assert(outputPanel.grid(8)(3).hidden == false)
      assert(outputPanel.grid(8)(4).hidden == false)
      assert(outputPanel.grid(8)(5).hidden == false)
    }


    describe("method newPanel") {
      it("should give new panel of given dimension") {
        val panelService = new PanelService
        val panel1 = panelService.newPanel(10)
        val panel2 = panelService.newPanel(20)

        panel1.grid.length shouldBe 10
        panel2.grid.length shouldBe 20
      }
    }
  }

}