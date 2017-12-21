package com.gu.navigation

import com.gu.navigation.lib.ResourcesHelper
import org.specs2.mutable.Specification

class NavigationIntegrationTest extends Specification with ResourcesHelper {

  val navigationProvider = new GarnettNavigationProvider()

  "Navigation" should  {
    "parse the uk menu ok" in {
       val json = slurpOrDie("navigation-conf/uk.json")
       navigationProvider.navigation(json) must not (throwA[NavigationParseError])
    }


    "parse the us menu ok" in {
       val json = slurpOrDie("navigation-conf/us.json")
      navigationProvider.navigation(json) must not (throwA[NavigationParseError])

    }

    "parse the au navigation" in {
       val json = slurpOrDie("navigation-conf/au.json")
      navigationProvider.navigation(json) must not (throwA[NavigationParseError])
    }

    "parse the international edition" in {
      val json = slurpOrDie("navigation-conf/international.json")
      navigationProvider.navigation(json) must not (throwA[NavigationParseError])
    }
  }
}
