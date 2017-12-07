Cross Platform Navigation
=========================

A library to provide define and provide scala representations of the navigation across [theguardian.com](https://theguardian.com)
and the guardian's cohort of [mobile apps](https://theguardian.com/mobile)

####Scala Usage

Add the following to your build.sbt file, setting the version number to the latest [version]

````scala
libraryDepencies += "com.gu" %% "cross-platform navigation" % "z,y"
````

####Updating the navigation

The directory [json](https://github.com/guardian/cross-platform-navigation/tree/nb-MSS-166-create-navigation/json) contains the definition for each of the guardians 4 editions as json files. Altering these files and releasing the project via riff-raff causes the updated files to be provisioned to s3. Your application can then retrieve (and cache) the json as you wish and use it at descri8ved above 

####Json Format

####Publishing