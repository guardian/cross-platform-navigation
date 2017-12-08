Cross Platform Navigation
=========================

A library to provide define and provide scala representations of the navigation across [theguardian.com](https://theguardian.com)
and the guardian's cohort of [mobile apps](https://theguardian.com/mobile)

##Scala Usage

Add the following to your build.sbt file, setting the version number to the latest [version]

````scala
libraryDepencies += "com.gu" %% "cross-platform navigation" % "z,y"
````

Use whatever method you wish to retrieve the desired json file to s3 and pass the content as a string to get a [Navigation](https://github.com/guardian/cross-platform-navigation/blob/nb-MSS-166-create-navigation/src/main/scala/com/gu/navigation/model/NavigationSection.scala#L39) object. This will contain the content and structure of the navigation.
You can den do something like

````scala
    val navigationProvider = new GarnettNavigationProvider
    navigationProvider.

````

####Updating the navigation

The directory [json](https://github.com/guardian/cross-platform-navigation/tree/nb-MSS-166-create-navigation/json) contains the definition for each of the guardians 4 editions as json files. Altering these files and releasing the project via riff-raff causes the updated files to be provisioned to s3. Your application can then retrieve (and cache) the json as you wish and use it at described above 

####Json Format

There is a json file for each edition of [theguardian.com](https://www.theguardian.com). Each one describes the navigation structure as a series of 'pillars', each of which contains one or more sections( which can, themselves contain further sections). Here is a cut down example containing two cut-down pllars

Here, there are only two pillars, with a cut down set of sections to indicate how the json is structured. Each section contains to flag values, along with a title and path. The ````editionalised````flag indicated that the path shuld be suffixed with the correct edition. Here, the 'politics' section in the 'News' pillar should have a path that resolves as '/uk/politics' for the uk edition. The ````mobileOverride``` value is to indicate to [Mapi](https://mobile.guardianapis.com/uk/navigation) (old nav) whether this section should be a link to a front or a tag list
````json
{
  "pillars" : [
    {
      "title": "",
      "path": "/",
      "editionalised": true,
      "sections": [
        {
          "title": "UK",
          "path": "/uk-news",
          "editionalised": false,
          "sections": [
            {"title": "UK", "path": "/uk-news", "editionalised": false},
            {"title": "UK politics", "path": "/politics", "editionalised": true}
          ]
        }
      ]
    },     
    {
      "title": "sport",
      "path": "/sport",
      "editionalised" : true,
      "sections": [
        { "title": "football", "path": "/football", "editionalised": false,
          "sections": [
            { "title": "football", "path": "/football", "editionalised": false },
            { "title": "live scores", "path": "/football/live", "editionalised": false },
            { "title": "tables", "path": "/football/tables", "editionalised": false },
            { "title": "competitions", "path": "/football/competitions", "editionalised": false },
            { "title": "results", "path": "/football/results", "editionalised": false },
            { "title": "fixtures", "path": "/football/fixtures", "editionalised": false },
            { "title": "clubs", "path": "/football/teams", "editionalised": false }
          ]
        },
        { "title": "rugby union", "path": "/sport/rugby-union", "mobileOverride" : true, "editionalised": false },
      ]
    }
  ]
}
````

####Publishing and Deploying

Update the scala functionality and publish to maven, making sure you update the version and the changelog. 

Deploying the project via riff-raff will upload the latest editionalised json files to s3, via teamcity. This process checks the validity of the json in order to build successfully