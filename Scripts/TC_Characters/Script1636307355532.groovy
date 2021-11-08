import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import groovy.json.JsonSlurper as JsonSlurper
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil

GlobalVariable.request = 'https://rickandmortyapi.com/api/character?name=Shmlangela'

characterss = WS.sendRequest(findTestObject('RickandMortyAPI', [('query') : GlobalVariable.request]))

WS.verifyResponseStatusCode(characterss, 200)

JsonSlurper slurper = new JsonSlurper()

Map parsedjson = slurper.parseText(characterss.getResponseBodyContent())

def array1 = parsedjson.get('results')

String userName = ''

for (def member : array1) {
    if (member.name == 'Shmlangela Shmlobinson-Shmlower') {
        userName = member.name

        println("CHARACTER "+userName+" FOUND")
		
		//Find Location
        String location = member.get('location').get('url')
		//Find episode
		List<String> listEpisode = new ArrayList<>(member.get('episode'))
		
		String userUrl = member.get('url')
		
        println('Character Location : ' + location)

        GlobalVariable.request = location

        userLocation = WS.sendRequest(findTestObject('RickandMortyAPI', [('query') : GlobalVariable.request]))

        WS.verifyResponseStatusCode(userLocation, 200)

        JsonSlurper slurperUserLoc = new JsonSlurper()

        Map parseUserLocation = slurperUserLoc.parseText(userLocation.getResponseBodyContent())

		//Verify Name, Type and Dimension
		WS.verifyElementPropertyValue(userLocation, 'name', 'Interdimensional Cable')
		
		WS.verifyElementPropertyValue(userLocation, 'type', 'TV')
		
		WS.verifyElementPropertyValue(userLocation, 'dimension', 'unknown')	
		
		//Episode
		for (def memberEp : listEpisode) {
			
			GlobalVariable.userEp = memberEp
			
			userEpisode = WS.sendRequest(findTestObject('RickandMortyAPI', [('query') : GlobalVariable.userEp]))
			
			WS.verifyResponseStatusCode(userEpisode, 200)
			
			JsonSlurper slurperr = new JsonSlurper()
			
			Map parsedjson2 = slurperr.parseText(userEpisode.getResponseBodyContent())
			
			def arrayCharacters = parsedjson2.get("characters")
	
			List<String> listCharacters = new ArrayList<>(arrayCharacters)
			
			for (def charEpisode : listCharacters) {
				if (charEpisode == userUrl) {
					println("USER EXIST IN THE CHARACTER LIST")
					break
				}
			}
		}
    }
}