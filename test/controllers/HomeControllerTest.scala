package controllers

import org.scalatest.{Matchers, FunSpec}
import org.scalatestplus.play.OneAppPerTest
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers._

/**
  * Created by khn3193 on 4/26/16.
  */
class HomeControllerTest extends FunSpec with Matchers with OneAppPerTest  {


  describe("HomeController with route /") {

    it("should render the index page") {
      val result = route(app, FakeRequest(GET, "/")).get

      status(result) shouldBe OK
      contentType(result) shouldBe Some("text/html")
      contentAsString(result) should include ("Welcome to Play")
    }
  }

  describe("HomeController with route /panel/get") {

    it("should get new minesweeper panel") {
      val result = route(app, FakeRequest(GET, "/panel/get")).get

      status(result) shouldBe OK
      contentType(result) shouldBe Some("application/json")
    }
  }

  describe("HomeController with route /panel/submit") {

    it("should give error as unsupported media type") {
      val result = route(app, FakeRequest(POST, "/panel/submit")).get

      status(result) shouldBe UNSUPPORTED_MEDIA_TYPE
    }
  }

  it("should give result with status Ok") {
    val requestJson = """{
                        |   "clickedRowIndex":0,
                        |   "clickedColIndex":0,
                        |   "panel":{
                        |      "dimension":9,
                        |      "grid":{"cells":[
                        |         [
                        |            {
                        |               "value":"2",
                        |               "rowIndex":0,
                        |               "colIndex":0,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"2",
                        |               "rowIndex":0,
                        |               "colIndex":1,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":0,
                        |               "colIndex":2,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":0,
                        |               "colIndex":3,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":0,
                        |               "colIndex":4,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":0,
                        |               "colIndex":5,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":0,
                        |               "colIndex":6,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"*",
                        |               "rowIndex":0,
                        |               "colIndex":7,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"*",
                        |               "rowIndex":0,
                        |               "colIndex":8,
                        |               "hidden":true
                        |            }
                        |         ],
                        |         [
                        |            {
                        |               "value":"*",
                        |               "rowIndex":1,
                        |               "colIndex":0,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"*",
                        |               "rowIndex":1,
                        |               "colIndex":1,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":1,
                        |               "colIndex":2,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":1,
                        |               "colIndex":3,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":1,
                        |               "colIndex":4,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":1,
                        |               "colIndex":5,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":1,
                        |               "colIndex":6,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"2",
                        |               "rowIndex":1,
                        |               "colIndex":7,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"2",
                        |               "rowIndex":1,
                        |               "colIndex":8,
                        |               "hidden":true
                        |            }
                        |         ],
                        |         [
                        |            {
                        |               "value":"2",
                        |               "rowIndex":2,
                        |               "colIndex":0,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"2",
                        |               "rowIndex":2,
                        |               "colIndex":1,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":2,
                        |               "colIndex":2,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":2,
                        |               "colIndex":3,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":2,
                        |               "colIndex":4,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":2,
                        |               "colIndex":5,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":2,
                        |               "colIndex":6,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":2,
                        |               "colIndex":7,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":2,
                        |               "colIndex":8,
                        |               "hidden":true
                        |            }
                        |         ],
                        |         [
                        |            {
                        |               "value":"1",
                        |               "rowIndex":3,
                        |               "colIndex":0,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":3,
                        |               "colIndex":1,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":3,
                        |               "colIndex":2,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":3,
                        |               "colIndex":3,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":3,
                        |               "colIndex":4,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":3,
                        |               "colIndex":5,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":3,
                        |               "colIndex":6,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":3,
                        |               "colIndex":7,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":3,
                        |               "colIndex":8,
                        |               "hidden":true
                        |            }
                        |         ],
                        |         [
                        |            {
                        |               "value":"1",
                        |               "rowIndex":4,
                        |               "colIndex":0,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"*",
                        |               "rowIndex":4,
                        |               "colIndex":1,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"2",
                        |               "rowIndex":4,
                        |               "colIndex":2,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":4,
                        |               "colIndex":3,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":4,
                        |               "colIndex":4,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":4,
                        |               "colIndex":5,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":4,
                        |               "colIndex":6,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":4,
                        |               "colIndex":7,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":4,
                        |               "colIndex":8,
                        |               "hidden":true
                        |            }
                        |         ],
                        |         [
                        |            {
                        |               "value":"1",
                        |               "rowIndex":5,
                        |               "colIndex":0,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"2",
                        |               "rowIndex":5,
                        |               "colIndex":1,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"*",
                        |               "rowIndex":5,
                        |               "colIndex":2,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":5,
                        |               "colIndex":3,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":5,
                        |               "colIndex":4,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":5,
                        |               "colIndex":5,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":5,
                        |               "colIndex":6,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":5,
                        |               "colIndex":7,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":5,
                        |               "colIndex":8,
                        |               "hidden":true
                        |            }
                        |         ],
                        |         [
                        |            {
                        |               "value":"1",
                        |               "rowIndex":6,
                        |               "colIndex":0,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"2",
                        |               "rowIndex":6,
                        |               "colIndex":1,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"2",
                        |               "rowIndex":6,
                        |               "colIndex":2,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"2",
                        |               "rowIndex":6,
                        |               "colIndex":3,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":6,
                        |               "colIndex":4,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":6,
                        |               "colIndex":5,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":6,
                        |               "colIndex":6,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":6,
                        |               "colIndex":7,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":6,
                        |               "colIndex":8,
                        |               "hidden":true
                        |            }
                        |         ],
                        |         [
                        |            {
                        |               "value":"*",
                        |               "rowIndex":7,
                        |               "colIndex":0,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":7,
                        |               "colIndex":1,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":7,
                        |               "colIndex":2,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"*",
                        |               "rowIndex":7,
                        |               "colIndex":3,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"2",
                        |               "rowIndex":7,
                        |               "colIndex":4,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":7,
                        |               "colIndex":5,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":7,
                        |               "colIndex":6,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":7,
                        |               "colIndex":7,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"*",
                        |               "rowIndex":7,
                        |               "colIndex":8,
                        |               "hidden":true
                        |            }
                        |         ],
                        |         [
                        |            {
                        |               "value":"1",
                        |               "rowIndex":8,
                        |               "colIndex":0,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":8,
                        |               "colIndex":1,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":8,
                        |               "colIndex":2,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"2",
                        |               "rowIndex":8,
                        |               "colIndex":3,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"*",
                        |               "rowIndex":8,
                        |               "colIndex":4,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":8,
                        |               "colIndex":5,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":8,
                        |               "colIndex":6,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":8,
                        |               "colIndex":7,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":8,
                        |               "colIndex":8,
                        |               "hidden":true
                        |            }
                        |         ]
                        |      ]},
                        |      "status":"InProgress"
                        |   }
                        |}"""
    val request = FakeRequest(POST, "/panel/submit").withJsonBody(Json.parse(requestJson.stripMargin))

    val result = route(app, request).get
    status(result) shouldBe OK
    contentType(result) shouldBe Some("application/json")
  }

  it("should give result with status BadRequest") {
    val requestJson = """{
                        |   "clickeeedRowIndex":0,
                        |   "clickeeedColIndex":0,
                        |   "panele":{
                        |      "dimension":9,
                        |      "grid":[
                        |         [
                        |            {
                        |               "value":"2",
                        |               "rowIndex":0,
                        |               "colIndex":0,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"2",
                        |               "rowIndex":0,
                        |               "colIndex":1,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":0,
                        |               "colIndex":2,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":0,
                        |               "colIndex":3,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":0,
                        |               "colIndex":4,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":0,
                        |               "colIndex":5,
                        |               "hidden":true
                        |            },
                        |            {
                        |                 "value":"1",
                        |               "rowIndex":8,
                        |               "colIndex":2,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"2",
                        |               "rowIndex":8,
                        |               "colIndex":3,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"*",
                        |               "rowIndex":8,
                        |               "colIndex":4,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":8,
                        |               "colIndex":5,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":" ",
                        |               "rowIndex":8,
                        |               "colIndex":6,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":8,
                        |               "colIndex":7,
                        |               "hidden":true
                        |            },
                        |            {
                        |               "value":"1",
                        |               "rowIndex":8,
                        |               "colIndex":8,
                        |               "hidden":true
                        |            }
                        |         ]
                        |      ],
                        |      "status":"InProgress"
                        |   }
                        |}"""
    val request = FakeRequest(POST, "/panel/submit").withJsonBody(Json.parse(requestJson.stripMargin))

    val result = route(app, request).get
    status(result) shouldBe BAD_REQUEST
  }
}
