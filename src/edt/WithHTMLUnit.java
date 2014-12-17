package edt;

import java.util.List;
import java.util.Scanner;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;

public class WithHTMLUnit {

	final WebClient webClient = new WebClient();
	final HtmlPage  mainPage;
	HtmlPage  treePage;
	final HtmlPage  pianoPage;
	
	final String moustique = "https://moustique2.interne.enac/ade/";

	public WithHTMLUnit() throws Exception  {
	//final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_24, "*****", 8888);
		 webClient.setHTMLParserListener(null);
		 webClient.setCssErrorHandler(new SilentCssErrorHandler());
		 webClient.getOptions().setUseInsecureSSL(true);
		 webClient.setIncorrectnessListener(new IncorrectnessListener()
		 	{
				@Override
				public void notify(String arg0, Object arg1) {
					//nothing
				}
		 		
		 	});
		 
	    //set proxy username and password 
	    //final DefaultCredentialsProvider credentialsProvider = (DefaultCredentialsProvider) webClient.getCredentialsProvider();
	    //credentialsProvider.addCredentials("eleve-enac\\****", "*****");
	    
		
			String urlConnection 		= "" + moustique + "custom/modules/plannings/direct_planning.jsp?login=enseignant&password=&projectId=2";
			
		 mainPage = webClient.getPage(urlConnection);
		 
		 //mainPage.executeJavaScript("javascriptcheck(2530,'true')");
		 treePage = (HtmlPage) mainPage.getFrameByName("tree").getEnclosedPage();
		 treePage = treePage.getAnchorByHref("javascript:openCategory('trainee')").click();
		 //System.out.println(mainPage1.getBody().asXml());		 
		 treePage = treePage.getAnchorByHref("javascript:openBranch(1405)").click();
		 treePage = treePage.getAnchorByHref("javascript:openBranch(2861)").click();
		 treePage = treePage.getAnchorByHref("javascript:openBranch(2529)").click();
		 treePage = treePage.getAnchorByHref("javascript:check(2530, 'false');").click();
		 //System.out.println(treePage.getBody().asXml());
		 
		 
		 pianoPage = (HtmlPage) mainPage.getFrameByName("pianoWeeks").getEnclosedPage();		 
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		webClient.closeAllWindows();
	}
	
	
	
	private HtmlPage getEventCalendarPage(int weekId) throws Exception  
	{
		System.out.println("ICCCCCCCCCCCCCCCCCIIIIIIIIIIIIIIIIIIIIIIII");
		 ScriptResult resultPiano = pianoPage.executeJavaScript("push("+weekId+",'true')");
		 resultPiano.getNewPage();	
			System.out.println("OU LLLLLLLLAAAAAAAAAAAAAAAAAAAAAAAALLLLLLLLAAAAAAAAAA");
		 
		String urlSelectCalendar 	= "" + moustique + "custom/modules/plannings/imagemap.jsp?week=" + weekId + "&clearTree=false&reset=true&width=795&height=480";
		System.out.println("urlSelectCalendar");
		return webClient.getPage(urlSelectCalendar);
	}
	
	private void getEventDetails(int eventId) throws Exception
	{
	
		String urlEvent = "" + moustique + "custom/modules/plannings/eventInfo.jsp?week=-1&day=-1&slot=0&"
				+ "eventId=" + eventId + "&activityId=-1&resourceId=-1&sessionId=-1&repetition=-1&order=slot&availableZone=-1";

		
		HtmlPage eventPage = webClient.getPage(urlEvent);
		
		//System.out.println(eventPage.asXml());

		HtmlElement eventPageBody = eventPage.getBody();
		for (HtmlElement bodyElement : eventPageBody.getHtmlElementDescendants()) {
			if( bodyElement instanceof HtmlTable )
			{
				//System.out.println(urlEvent);
				//System.out.println(bodyElement.asXml());
				HtmlTable table = (HtmlTable) bodyElement;
				System.out.println("\t" + table.getCellAt(2, 0).asText());
				System.out.println("\t" + table.getCellAt(2, 1).asText());
				System.out.println("\t" + table.getCellAt(2, 2).asText());
				System.out.println("\t" + table.getCellAt(2, 3).asText());
				System.out.println("\t" + table.getCellAt(2, 4).asText());
				System.out.println("\t" + table.getCellAt(2, 5).asText());
				System.out.println("\t" + table.getCellAt(2, 6).asText());
				System.out.println("\t" + table.getCellAt(2, 7).asText());
				System.out.println("\t" + table.getCellAt(2, 8).asText());
				
			}
		}
		
	}
	
	public void getEventsByWeek(int weekId) throws Exception{
		DomNodeList<DomElement> list = getEventCalendarPage(weekId).getElementsByTagName("area");
		System.out.println("getEventsByWeek");
		for (DomElement domElement : list) {
			//System.out.println("EXTRACT: " + domElement.getAttribute("href"));
			Scanner scanId = new Scanner(domElement.getAttribute("href"));
			scanId.useDelimiter(",");
			scanId.next();scanId.next();scanId.next();
			int eventId = scanId.nextInt();
			scanId.close();
			
			Scanner scanCoords = new Scanner(domElement.getAttribute("coords"));
			scanCoords.useDelimiter(",");
			int xmin = scanCoords.nextInt(); 
			scanCoords.nextInt();
			int xmax = scanCoords.nextInt(); 
			scanCoords.close();
			
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
							
			System.out.println("\n\nWeekId: " + weekId + " Id: " + eventId + " duree: " + duree + "h");
			getEventDetails(eventId);
		}
	}
	
	
	

	public static void main(String[] args) throws Exception {
	
   		WithHTMLUnit controller = new WithHTMLUnit();
		controller.getEventsByWeek(16); 
		controller.getEventsByWeek(17);
		controller.getEventsByWeek(18);

		// pianoPage = pianoPage.getAnchorByName("w16").click();
		 
		/*ScriptResult resultTree = treePage.executeJavaScript("check(2530,'true')");
		resultTree.getNewPage();*/

		 

				 
	}

}
