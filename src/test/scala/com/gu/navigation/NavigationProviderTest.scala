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
      |      "path": "",
      |      "editionalised": true,
      |      "sections" : [
      |        { "title": "world", "path": "world",
  |              "editionOverride": "au",
      |          "sections" : [
      |            { "title": "world", "path": "world" },
      |            { "title": "europe", "path": "world/europe-news" }
      |          ]
      |        },
      |        { "title": "UK news", "path": "uk-news",
      |          "sections": [
      |            { "title": "UK", "path": "uk-news" },
      |            { "title": "UK politics", "path": "politics" },
      |            { "title": "education", "path": "education" }          ]
      |        },
      |        {"title": "obituraries", "path": "tone/obituraries", "mobileOverride" : "tag"}
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
      |        { "title": "world", "path": "world",
      |          "sections" : [
      |            { "title": "world", "path": "world" },
      |            { "title": "europe", "path": "world/europe-news" }
      |          ]
      |        },
      |        { "title": "UK news", "path": "uk-news",
      |          "sections": [
      |            { "title": 12312, "path": "uk-news" },
      |            { "title": "UK politics", "path": "politics" },
      |            { "title": "education", "path": "education" }          ]
      |        },
      |        {"title": "obituraries", "path": "tone/obituraries", "mobileOverride" : "totalGarnett"}
      |      ]
      |    }
      |  ]
      |}
      |
    """.stripMargin

  val navigationProvider = new GarnettNavigationProvider()

  val expectedNav =
    Navigation(List(
      NavigationSection(title = "", path = "",
        sections = Some(
          List(
            NavigationSection(title = "world",path = "world",editionOverride = Some("au") ,
              sections = Some(List(NavigationSection(title = "world", path = "world"),
                                   NavigationSection(title = "europe", path = "world/europe-news")
                              )
                          )
               ),
            NavigationSection(title = "UK news", path = "uk-news",
              sections = Some(List(NavigationSection(title = "UK", path = "uk-news"),
                                   NavigationSection(title = "UK politics", path = "politics"),
                                   NavigationSection(title = "education", path = "education")
                          )
                        )
              ),
            NavigationSection(title = "obituraries", path = "tone/obituraries", mobileOverride = Some("tag") )
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
