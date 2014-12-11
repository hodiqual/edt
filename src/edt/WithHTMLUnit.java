package edt;

import java.util.List;
import java.util.Scanner;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;

public class WithHTMLUnit {

	public WithHTMLUnit() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception {
		
	    //final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_24, "*****", 8888);

		
		 final WebClient webClient = new WebClient();
		 
		 webClient.getOptions().setUseInsecureSSL(true);
		 
	    //set proxy username and password 
	    //final DefaultCredentialsProvider credentialsProvider = (DefaultCredentialsProvider) webClient.getCredentialsProvider();
	    //credentialsProvider.addCredentials("eleve-enac\\****", "*****");
	    
	   
		 
		 String moustique = "https://moustique2.interne.enac/ade/";
			String urlConnection 		= "" + moustique + "custom/modules/plannings/direct_planning.jsp?login=enseignant&password=&projectId=2";
			
		 HtmlPage mainPage = webClient.getPage(urlConnection);
		 
		 //mainPage.executeJavaScript("javascriptcheck(2530,'true')");
		 HtmlPage treePage = (HtmlPage) mainPage.getFrameByName("tree").getEnclosedPage();
		 treePage = treePage.getAnchorByHref("javascript:openCategory('trainee')").click();
		 //System.out.println(mainPage1.getBody().asXml());		 
		 treePage = treePage.getAnchorByHref("javascript:openBranch(1405)").click();
		 treePage = treePage.getAnchorByHref("javascript:openBranch(2861)").click();
		 treePage = treePage.getAnchorByHref("javascript:openBranch(2529)").click();
		 treePage = treePage.getAnchorByHref("javascript:check(2530, 'false');").click();
		 System.out.println(treePage.getBody().asXml());
		 
		 
		 HtmlPage pianoPage = (HtmlPage) mainPage.getFrameByName("pianoWeeks").getEnclosedPage();
		// pianoPage = pianoPage.getAnchorByName("w16").click();
		 
		/*ScriptResult resultTree = treePage.executeJavaScript("check(2530,'true')");
		resultTree.getNewPage();*/
		 
		 int week = 16;
		 
		 ScriptResult resultPiano = pianoPage.executeJavaScript("push("+week+",'true')");
		 resultPiano.getNewPage();

		 //String urlSelectPromo 		= "" + moustique + "standard/gui/tree.jsp?selectId=2530&reset=true&forceLoad=false&scroll=0";
		 //webClient.getPage(urlSelectPromo);
		 //String urlSelectWeek  		= "" + moustique + "custom/modules/plannings/bounds.jsp?clearTree=false&week=15&reset=true";
		 //webClient.getPage(urlSelectWeek);		
		 
		

		String urlSelectCalendar 	= "" + moustique + "custom/modules/plannings/imagemap.jsp?week=" + week + "&clearTree=false&reset=true&width=795&height=480";
		HtmlPage calendarPage = webClient.getPage(urlSelectCalendar);
		System.out.println(calendarPage.getBody().asXml());
		
		DomNodeList<DomElement> list = calendarPage.getElementsByTagName("area");
		for (DomElement domElement : list) {
			System.out.println("EXTRACT: " + domElement.getAttribute("href"));
			Scanner scanId = new Scanner(domElement.getAttribute("href"));
			scanId.useDelimiter(",");
			scanId.next();scanId.next();scanId.next();
			System.out.println("Id: " + scanId.nextInt());
			//System.out.println("EXTRACT: " + domElement.getAttribute("coords"));
			Scanner scanCoords = new Scanner(domElement.getAttribute("coords"));
			scanCoords.useDelimiter(",");
			int xmin = scanCoords.nextInt(); 
			scanCoords.nextInt();
			int xmax = scanCoords.nextInt(); 
			int delta = xmax-xmin;
			int TailleHeure = 64;
			int err = 5;
			int duree = 0;
			if( TailleHeure-err < delta && delta < TailleHeure+err )
				duree = 1;
			else if( 2*TailleHeure-err < delta && delta < 2*TailleHeure+err )
				duree = 2;
			else if( 3*TailleHeure-err < delta && delta < 3*TailleHeure+err )
				duree = 3;
			else if( 4*TailleHeure-err < delta && delta < 4*TailleHeure+err )
				duree = 4;

			System.out.println("\tduree: " + duree + "h");
			
		}
		
		int eventId = 16496;
		String urlEvent = "" + moustique + "custom/modules/plannings/eventInfo.jsp?week=-1&day=-1&slot=0&"
				+ "eventId=" + eventId + "&activityId=-1&resourceId=-1&sessionId=-1&repetition=-1&order=slot&availableZone=-1";

		
		HtmlPage eventPage = webClient.getPage(urlEvent);
		
		System.out.println(eventPage.asXml());
		

		HtmlElement eventPageBody = eventPage.getBody();
		for (HtmlElement bodyElement : eventPageBody.getHtmlElementDescendants()) {
			if( bodyElement instanceof HtmlTable )
			{
				System.out.println(urlEvent);
				System.out.println(bodyElement.asXml());
				HtmlTable table = (HtmlTable) bodyElement;
				table.getCellAt(1, 0);
				HtmlTableCell cell = table.getCellAt(2, 0);
				System.out.println(cell.asText());
				cell = table.getCellAt(2, 1);
				System.out.println(cell.asText());
				cell = table.getCellAt(2, 2);
				System.out.println(cell.asText());
				cell = table.getCellAt(2, 3);
				System.out.println(cell.asText());
				cell = table.getCellAt(2, 4);
				System.out.println(cell.asText());
				cell = table.getCellAt(2, 5);
				System.out.println(cell.asText());
				cell = table.getCellAt(2, 6);
				System.out.println(cell.asText());
				cell = table.getCellAt(2, 7);
				System.out.println(cell.asText());
				cell = table.getCellAt(2, 8);
				System.out.println(cell.asText());
				
			}
		}
		
		
		//System.out.println(table.asXml());
		
		 webClient.closeAllWindows();
		 
	}

}
