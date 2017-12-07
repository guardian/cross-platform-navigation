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

  val invalidNavigationJson =
    """
      |{
      |  "pillars" : [
      |    {
      |      "title": "",
      |      "path": [false, true],
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
      |            { "title": 12312, "path": "/uk-news", "editionalised": false },
      |            { "title": "UK politics", "path": "/politics", "editionalised": false },
      |            { "title": "education", "path": "/education", "editionalised": false }          ]
      |        },
      |        {"title": "obituraries", "path": "/tone/obituraries", "editionalised": false, "mobileOverride" : "totalGarnett"}
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

  "Navigation Provider" should {
    "correctly parse valid input " in {
        val nav = navigationProvider.navigation(singlePillarNavJson)
        nav should beEqualTo(expectedNav)
    }

    "throw an exception if the json is invalid" in {
        navigationProvider.navigation(invalidNavigationJson) must throwA[NavigationParseError]
    }

    "detail the invalid fields when an exception is thrown" in {
       navigationProvider.navigation(invalidNavigationJson) must throwA[NavigationParseError].like {
         case NavigationParseError(msg) =>
           msg must contain("/pillars(0)/path")
           msg must contain("/pillars(0)/sections(1)/sections(0)/title")
       }
    }
  }
}
