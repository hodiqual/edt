package edt;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
 
public class HttpUrlConnectionExample {
	
	private List<String> cookies;
	  private HttpsURLConnection conn;
	  private Proxy proxy;
	  private HostnameVerifier allHostsValid;
 
  public HttpUrlConnectionExample() throws Exception {
		proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(InetAddress.getByName("*******"), 8888));
	  //eleve-enac\hodiqual
		 Authenticator authenticator = new Authenticator() {

		        public PasswordAuthentication getPasswordAuthentication() {
		            return (new PasswordAuthentication("eleve-enac\\****",
		                    "*****".toCharArray()));
		        }
		    };
		    Authenticator.setDefault(authenticator);
		  
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}
					public void checkClientTrusted(X509Certificate[] certs, String authType) {
					}
					public void checkServerTrusted(X509Certificate[] certs, String authType) {
					}
				}
			};

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}


 
  private final String USER_AGENT = "Mozilla/5.0 (X11; Linux i586; rv:31.0) Gecko/20100101 Firefox/31.0";
 
  public static void main(String[] args) throws Exception {
	 
	String moustique = "https://moustique2.interne.enac/ade/";
	String urlConnection 		= "" + moustique + "custom/modules/plannings/direct_planning.jsp?login=enseignant&password=&projectId=2";
	String urlSelectPromo 		= "" + moustique + "standard/gui/tree.jsp?selectId=2530&reset=true&forceLoad=false&scroll=0";
	String urlSelectWeek  		= "" + moustique + "custom/modules/plannings/bounds.jsp?clearTree=false&week=17&reset=true";
	String urlSelectCalendar 	= "" + moustique + "custom/modules/plannings/imagemap.jsp?week=17&clearTree=false&reset=true&width=978&height=824";
	HttpUrlConnectionExample http = new HttpUrlConnectionExample();

	// make sure cookies is turn on
	CookieHandler.setDefault(new CookieManager());
 
	// 1. Send a "GET" request, so that you can extract the form's data.
	System.out.println(http.GetPageContent(urlConnection));
	System.out.println(http.GetPageContent(urlSelectPromo));
	System.out.println(http.GetPageContent(urlSelectWeek));
	String pageToParse = http.GetPageContent(urlSelectCalendar);
	System.out.println(pageToParse);
	
	//String postParams = http.getFormParams(page, "hodiqual", "Bafib0ul");
 
	// 2. Construct above post's content and then send a POST request for
	// authentication
	//http.sendPost("https://e-campus.enac.fr/moodle/login/index.php?authldap_skipntlmsso=1&lang=fr", postParams);
 
	// 3. success then go to moustique with the right SIZE.

	//String result2 = http.GetPageContent("https://moustique2.interne.enac/ade/custom/modules/plannings/direct_planning.jsp");
	//System.out.println(result2);
	
	// 4. Retrieve link for all week 
	
	// 5. For each week retrieve all eventId
	
	// 6. For each eventId retrieve all its information and create googleCalendarObject
	
	// 7. Compare and dend all change to googleCalendar 
	//(color, retrieve new/updated/deleted ->make diff) Batch Or Not
	
	// 8. Send mail to iessa14 of the report
  }
 
  private void sendPost(String url, String postParams) throws Exception {
 
	URL obj = new URL(url);
	conn = (HttpsURLConnection) obj.openConnection(proxy);
 
	// Acts like a browser
	conn.setUseCaches(false);
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Host", "e-campus.enac.fr");
	conn.setRequestProperty("User-Agent", USER_AGENT);
	conn.setRequestProperty("Accept",
		"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	conn.setRequestProperty("Accept-Language", "fr-FR,fr;q=0.5");
	for (String cookie : this.cookies) {
		conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
	}
	conn.setRequestProperty("Connection", "keep-alive");
	conn.setRequestProperty("Referer", "https://e-campus.enac.fr/moodle/");
	conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));
 
	conn.setDoOutput(true);
	conn.setDoInput(true);
 
	// Send post request
	DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
	wr.writeBytes(postParams);
	wr.flush();
	wr.close();
 
	int responseCode = conn.getResponseCode();
	System.out.println("\nSending 'POST' request to URL : " + url);
	System.out.println("Post parameters : " + postParams);
	System.out.println("Response Code : " + responseCode);
 
	BufferedReader in = 
             new BufferedReader(new InputStreamReader(conn.getInputStream()));
	String inputLine;
	StringBuffer response = new StringBuffer();
 
	while ((inputLine = in.readLine()) != null) {
		response.append(inputLine);
	}
	in.close();
	// System.out.println(response.toString());
 
  }
 
  private String GetPageContent(String url) throws Exception {
 
	URL obj = new URL(url);
	conn = (HttpsURLConnection) obj.openConnection();
	// default is GET
	conn.setRequestMethod("GET");
 
	conn.setUseCaches(false);
	// act like a browser
	conn.setRequestProperty("User-Agent", USER_AGENT);
	conn.setRequestProperty("Accept",
		"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	conn.setRequestProperty("Accept-Language", "fr-FR,fr;q=0.5");
	if (cookies != null) {
		for (String cookie : this.cookies) {
			conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
		}
	}

	System.out.println("\nSending 'GET' request to URL : " + url);

	int responseCode = conn.getResponseCode();	
	System.out.println("Response Code : " + responseCode);
 
	BufferedReader in = 
            new BufferedReader(new InputStreamReader(conn.getInputStream()));
	String inputLine;
	StringBuffer response = new StringBuffer();
 
	while ((inputLine = in.readLine()) != null) {
		response.append(inputLine);
	}
	in.close();
 
	// Get the response cookies
	setCookies(conn.getHeaderFields().get("Set-Cookie"));
 
	return response.toString();
 
  }
 
  public String getFormParams(String html, String username, String password)
		throws UnsupportedEncodingException {
	
	/*String urlParameters = "username=hodiqual&password=Bafib0ul";
		return urlParameters;*/
		
 
	System.out.println("Extracting form's data...");
 
	Document doc = Jsoup.parse(html);
 
	// Google form id
	Element loginform = doc.getElementById("loginform");
	Elements inputElements = loginform.getElementsByTag("input");
	List<String> paramList = new ArrayList<String>();
	for (Element inputElement : inputElements) {
		String key = inputElement.attr("name");
		String value = inputElement.attr("value");
 
		if (key.equals("username"))
			value = username;
		else if (key.equals("password"))
			value = password;
		paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
	}
 
	// build parameters list
	StringBuilder result = new StringBuilder();
	for (String param : paramList) {
		if (result.length() == 0) {
			result.append(param);
		} else {
			result.append("&" + param);
		}
	}
	return result.toString(); 
  }
 
  public List<String> getCookies() {
	return cookies;
  }
 
  public void setCookies(List<String> cookies) {
	this.cookies = cookies;
  }
 
}