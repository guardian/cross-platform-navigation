package com.gu.navigation

import com.gu.navigation.model.{Navigation, NavigationSection}
import org.specs2.mutable.Specification
import play.api.libs.json.JsonValidationError

class NavigationProviderTest extends Specification {

  val singlePillarNavJson =
    """
      |{
      |  "pillars" : [
      |    {
      |      "title": "",
      |      "path": "/",
      |      "editionalised": true,
      |      "sections" : [
      |        { "title": "world", "path": "/world", "editionalised": false,
      |          "sections" : [
      |            { "title": "world", "path": "/world", "editionalised": false },
      |            { "title": "europe", "path": "/world/europe-news", "editionalised": false }
      |          ]
      |        },
      |        { "title": "UK news", "path": "/uk-news", "editionalised": false,
      |          "sections": [
      |            { "title": "UK", "path": "/uk-news", "editionalised": false },
      |            { "title": "UK politics", "path": "/politics", "editionalised": false },
      |            { "title": "education", "path": "/education", "editionalised": false }          ]
      |        },
      |        {"title": "obituraries", "path": "/tone/obituraries", "editionalised": false}
      |      ]
      |    }
      |  ]
      |}
      |
    """.stripMargin
  val navigationProvider = new GarnettNavigationProvider()

  val expectedNav =
    Navigation(List(
      NavigationSection(title = "", path ="/", editionalised = true,
        sections = Some(
          List(
            NavigationSection(title = "world",path = "/world", editionalised = false,
              sections = Some(List(NavigationSection(title = "world", path = "/world", editionalised = false),
                                   NavigationSection(title = "europe", path = "/world/europe-news", editionalised = false)
                              )
                          )
               ),
            NavigationSection(title = "UK news", path = "/uk-news", editionalised = false,
              sections = Some(List(NavigationSection(title = "UK", path = "/uk-news", editionalised = false),
                                   NavigationSection(title = "UK politics", path = "/politics", editionalised = false),
                                   NavigationSection(title = "education", path = "/education",editionalised = false)
                          )
                        )
              ),
            NavigationSection(title = "obituraries", path = "/tone/obituraries",false)
          )
        )
      )
    )
  )

  //val l = List(("/pillars(0)/path",List(JsonValidationError(List(error.expected.jsstring),WrappedArray()))))


  "Navigation Provider" should {
    "correctly parse valid input " in {
      val nav = navigationProvider.navigation(singlePillarNavJson)
      nav should beEqualTo(expectedNav)
    }
  }
}
