
relativeSourceMaps := true

mainClass in Compile := Some("sbtb.koolaid.ScalaJs")

//persistLauncher in Compile := true

//bootSnippet := "sbtb.koolaid.sjs.MortgageCalculator().main(document.getElementById('content'), document.getElementById('result'));"

//refreshBrowsers <<= refreshBrowsers.triggeredBy(fastOptJS in Compile)
