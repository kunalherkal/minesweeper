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

  def newPanel(rows: Int, columns: Int, mines: Int): Panel = Panel(rows, columns, mines)
}
