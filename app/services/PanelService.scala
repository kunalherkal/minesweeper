package services

import models.{PanelSubmit, Panel}

/**
  * Created by Kunal Herkal on 4/22/16.
  */
class PanelService {

  def process(panelSubmit: PanelSubmit): Panel = {
    val clickedRow = panelSubmit.clickedRowIndex
    val clickedCol = panelSubmit.clickedColIndex
    val panel = panelSubmit.panel
    panel.processClick(clickedRow, clickedCol)
  }

  def newPanel(dimension: Int): Panel = Panel(dimension)
}
