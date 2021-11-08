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

GlobalVariable.request = 'https://rickandmortyapi.com/api/character?species=Human&gender=Female&status=Alive'

alivefemalechars = WS.sendRequest(findTestObject('RickandMortyAPI', [('query') : GlobalVariable.request]))

WS.verifyResponseStatusCode(alivefemalechars, 200)

JsonSlurper slurper = new JsonSlurper()

Map parsedjson = slurper.parseText(alivefemalechars.getResponseBodyContent())

String totalAliveFemaleChars = parsedjson.get('info').get('count')
String totalPages = parsedjson.get('info').get('pages')

println('Total Alive Female Characters : ' + totalAliveFemaleChars)

int sizePage = Integer.parseInt(totalPages)

for (int x=1; x<=sizePage;x++) {
	alivefemaleC = WS.sendRequest(findTestObject('RickandMortyAPI', [('query') : GlobalVariable.request]))
	println("Page: "+x)
	println("API: "+GlobalVariable.request)
	
	WS.verifyResponseStatusCode(alivefemaleC, 200)
	
	JsonSlurper slurperS = new JsonSlurper()
	
	Map parsedjsonS = slurperS.parseText(alivefemaleC.getResponseBodyContent())
	String[] resultArray = parsedjsonS.get('results').toArray()
	def sizeResult = resultArray.size()
	
	for (i = 0; i < sizeResult; i++) {
		String name = parsedjsonS.get('results').get(i).get('name')
		String id = parsedjsonS.get('results').get(i).get('id')
		String status = parsedjsonS.get('results').get(i).get('status')
		String species = parsedjsonS.get('results').get(i).get('species')
		String gender = parsedjsonS.get('results').get(i).get('gender')
	
		printf("Id :%s, Name:%s, Status:%s, Species:%s, Gender:%s", id, name, status, species, gender)
	}
	String nextPage = parsedjsonS.get('info').get('next')
	if(nextPage != null) {
		GlobalVariable.request=nextPage
	}
}