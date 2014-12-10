package edt;

import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

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
		 
		 ScriptResult resultPiano = pianoPage.executeJavaScript("push(15,'true')");
		 resultPiano.getNewPage();

		 //String urlSelectPromo 		= "" + moustique + "standard/gui/tree.jsp?selectId=2530&reset=true&forceLoad=false&scroll=0";
		 //webClient.getPage(urlSelectPromo);
		 //String urlSelectWeek  		= "" + moustique + "custom/modules/plannings/bounds.jsp?clearTree=false&week=15&reset=true";
		 //webClient.getPage(urlSelectWeek);		 

		String urlSelectCalendar 	= "" + moustique + "custom/modules/plannings/imagemap.jsp?week=15&clearTree=false&reset=true&width=978&height=824";
		HtmlPage calendarPage = webClient.getPage(urlSelectCalendar);
		System.out.println(calendarPage.getBody().asXml());
		
		 webClient.closeAllWindows();
		 
	}

}
