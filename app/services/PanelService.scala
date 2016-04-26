package services

import models.{PanelSubmit, Panel}

/**
  * Created by Kunal Herkal on 4/22/16.
  */
class PanelService {

  def process(panelSubmit: PanelSubmit): Panel = {
    panelSubmit.panel.solve(panelSubmit.clickedRowIndex, panelSubmit.clickedColIndex)
  }

  def newPanel(dimension: Int): Panel = {
    Panel(dimension)
  }

}
