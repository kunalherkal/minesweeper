package models

/**
  * Created by Kunal Herkal on 4/22/16.
  */
sealed trait Status

case object Complete extends Status
case object Filed extends Status
case object InProgress extends Status
