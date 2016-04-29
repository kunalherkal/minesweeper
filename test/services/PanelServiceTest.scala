package services

import models._
import org.scalatest.{Matchers, FunSpec}

/**
  * Created by khn3193 on 4/25/16.
  */
class PanelServiceTest extends FunSpec with Matchers {

  describe("method process") {
    val cells = List(
      List(Cell(" ",0,0,hidden = true), Cell(" ",0,1,hidden = true), Cell(" ",0,2,hidden = true), Cell("1",0,3,hidden = true), Cell("1",0,4,hidden = true), Cell("1",0,5,hidden = true), Cell(" ",0,6,hidden = true), Cell(" ",0,7,hidden = true), Cell(" ",0,8,hidden = true)),
      List(Cell("1",1,0,hidden = true), Cell("1",1,1,hidden = true), Cell("2",1,2,hidden = true), Cell("2",1,3,hidden = true), Cell("*",1,4,hidden = true), Cell("1",1,5,hidden = true), Cell(" ",1,6,hidden = true), Cell(" ",1,7,hidden = true), Cell(" ",1,8,hidden = true)),
      List(Cell("1",2,0,hidden = true), Cell("*",2,1,hidden = true), Cell("2",2,2,hidden = true), Cell("*",2,3,hidden = true), Cell("3",2,4,hidden = true), Cell("2",2,5,hidden = true), Cell("1",2,6,hidden = true), Cell(" ",2,7,hidden = true), Cell(" ",2,8,hidden = true)),
      List(Cell("2",3,0,hidden = true), Cell("2",3,1,hidden = true), Cell("2",3,2,hidden = true), Cell("1",3,3,hidden = true), Cell("2",3,4,hidden = true), Cell("*",3,5,hidden = true), Cell("1",3,6,hidden = true), Cell("1",3,7,hidden = true), Cell("1",3,8,hidden = true)),
      List(Cell("*",4,0,hidden = true), Cell("1",4,1,hidden = true), Cell(" ",4,2,hidden = true), Cell(" ",4,3,hidden = true), Cell("2",4,4,hidden = true), Cell("2",4,5,hidden = true), Cell("3",4,6,hidden = true), Cell("*",4,7,hidden = true), Cell("1",4,8,hidden = true)),
      List(Cell("1",5,0,hidden = true), Cell("1",5,1,hidden = true), Cell(" ",5,2,hidden = true), Cell(" ",5,3,hidden = true), Cell("1",5,4,hidden = true), Cell("*",5,5,hidden = true), Cell("2",5,6,hidden = true), Cell("2",5,7,hidden = true), Cell("2",5,8,hidden = true)),
      List(Cell("1",6,0,hidden = true), Cell("1",6,1,hidden = true), Cell("1",6,2,hidden = true), Cell(" ",6,3,hidden = true), Cell("1",6,4,hidden = true), Cell("2",6,5,hidden = true), Cell("2",6,6,hidden = true), Cell("2",6,7,hidden = true), Cell("*",6,8,hidden = true)),
      List(Cell("1",7,0,hidden = true), Cell("*",7,1,hidden = true), Cell("1",7,2,hidden = true), Cell(" ",7,3,hidden = true), Cell(" ",7,4,hidden = true), Cell("1",7,5,hidden = true), Cell("*",7,6,hidden = true), Cell("2",7,7,hidden = true), Cell("1",7,8,hidden = true)),
      List(Cell("1",8,0,hidden = true), Cell("1",8,1,hidden = true), Cell("1",8,2,hidden = true), Cell(" ",8,3,hidden = true), Cell(" ",8,4,hidden = true), Cell("1",8,5,hidden = true), Cell("1",8,6,hidden = true), Cell("1",8,7,hidden = true), Cell(" ",8,8,hidden = true)))

    val panel = Panel(Grid(cells), 9, 9, 10)

    it("should return panel with state as failed") {
      val panelService = new PanelService
      val panelSubmit = PanelSubmit(panel, 4, 0)
      val outputPanelSubmit = panelService.process(panelSubmit)

      outputPanelSubmit.panel.status shouldBe PanelStatus.FAILED
    }

    it("should return panel with clicked cell as hidden false") {
      val panelService = new PanelService
      val panelSubmit = PanelSubmit(panel, 1, 0)
      val outputPanelSubmit = panelService.process(panelSubmit)


      outputPanelSubmit.panel.status shouldBe PanelStatus.IN_PROGRESS
      outputPanelSubmit.panel.grid.cell(1, 0).hidden shouldBe false
    }

    it("should return panel with clicked and adjacent cells as hidden false for 0,0") {
      val panelService = new PanelService
      val panelSubmit = PanelSubmit(panel, 0, 0)
      val outputPanelSubmit = panelService.process(panelSubmit)

      val exposedCells = List(outputPanelSubmit.panel.grid.cell(0, 0),
          outputPanelSubmit.panel.grid.cell(0, 1),
          outputPanelSubmit.panel.grid.cell(0, 2),
          outputPanelSubmit.panel.grid.cell(0, 3),
          outputPanelSubmit.panel.grid.cell(1, 0),
          outputPanelSubmit.panel.grid.cell(1, 1),
          outputPanelSubmit.panel.grid.cell(1, 2),
          outputPanelSubmit.panel.grid.cell(1, 3))

      outputPanelSubmit.panel.status shouldBe PanelStatus.IN_PROGRESS
      outputPanelSubmit.panel.grid.flattenedCells.foreach(cell => {
        if(exposedCells.contains(cell)) cell.hidden shouldBe false else cell.hidden shouldBe true
      })
    }

    it("should return panel with clicked and adjacent cells as hidden false for 4, 2") {
      val panelService = new PanelService
      val panelSubmit = PanelSubmit(panel, 4, 2)
      val outputPanelSubmit = panelService.process(panelSubmit)
      val exposedCells = List(outputPanelSubmit.panel.grid.cell(3, 1),
        outputPanelSubmit.panel.grid.cell(3, 2),
        outputPanelSubmit.panel.grid.cell(3, 3),
        outputPanelSubmit.panel.grid.cell(3, 4),
        outputPanelSubmit.panel.grid.cell(4, 1),
        outputPanelSubmit.panel.grid.cell(4, 2),
        outputPanelSubmit.panel.grid.cell(4, 3),
        outputPanelSubmit.panel.grid.cell(4, 4),
        outputPanelSubmit.panel.grid.cell(5, 1),
        outputPanelSubmit.panel.grid.cell(5, 2),
        outputPanelSubmit.panel.grid.cell(5, 3),
        outputPanelSubmit.panel.grid.cell(5, 4),
        outputPanelSubmit.panel.grid.cell(6, 1),
        outputPanelSubmit.panel.grid.cell(6, 2),
        outputPanelSubmit.panel.grid.cell(6, 3),
        outputPanelSubmit.panel.grid.cell(6, 4),
        outputPanelSubmit.panel.grid.cell(6, 5),
        outputPanelSubmit.panel.grid.cell(7, 2),
        outputPanelSubmit.panel.grid.cell(7, 3),
        outputPanelSubmit.panel.grid.cell(7, 4),
        outputPanelSubmit.panel.grid.cell(7, 5),
        outputPanelSubmit.panel.grid.cell(8, 2),
        outputPanelSubmit.panel.grid.cell(8, 3),
        outputPanelSubmit.panel.grid.cell(8, 4),
        outputPanelSubmit.panel.grid.cell(8, 5))

      outputPanelSubmit.panel.status shouldBe PanelStatus.IN_PROGRESS
      outputPanelSubmit.panel.grid.flattenedCells.foreach(cell => {
        if(exposedCells.contains(cell)) cell.hidden shouldBe false else cell.hidden shouldBe true
      })
    }


    describe("method newPanel") {
      it("should give new panel of given dimension") {
        val panelService = new PanelService
        val panel1 = panelService.newPanel(10, 10, 10)
        val panel2 = panelService.newPanel(20, 20, 20)

        panel1.grid.rows shouldBe 10
        panel2.grid.rows shouldBe 20
      }
    }
  }

}