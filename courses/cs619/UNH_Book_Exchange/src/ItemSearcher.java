import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * This class uses Amazon API to fetch information about books. Due to a bug with the Amazon server, this class searches the 
 * entire Amazon database rather than specifically the book's category in order to find a valid book. During the testing phase it was noticed
 * that not all books were in the book section and as a result their ISBN's and book information could not be properly retrieved.
 * @author Amazon: Edited by Team11
 *
 */
public class ItemSearcher {
	/**
	 * Your AWS Access Key ID, as taken from the AWS Your Account page.
	 */
	 private static final String AWS_ACCESS_KEY_ID = "AKIAJWPWVNMSXWYHAB3Q";
	 
	/**
	* Your AWS Secret Key corresponding to the above ID, as taken from the AWS.
	* Your Account page.
	*/
	 private static final String AWS_SECRET_KEY = "eldmqBAPy5yeJXKLdMljcBvPLOYafgBDLelkbQGe";
	/**
	* Use one of the following end-points, according to the region you are.
	* interested in:
	* 
	* US: ecs.amazonaws.com 
	* CA: ecs.amazonaws.ca 
	* UK: ecs.amazonaws.co.uk 
	* DE: ecs.amazonaws.de 
	* FR: ecs.amazonaws.fr 
	* JP: ecs.amazonaws.jp
	* 
	*/
	private static final String ENDPOINT = "ecs.amazonaws.com";
	
	/**
	 * The signed request helper used to retrieve specific information from the Amazon API.
	 */
	private SignedRequestsHelper helper;
	
	/**
	 * Sets up the SignedRequestHelper to retrieve book information.
	 */
	public ItemSearcher(){
		setUpHelper();
	}
	
	/**
	 * Utility function to fetch the response from the service and extract the
	*  title from the XML.
	 * @param requestUrl
	 * URL of the request.
	 * @param searchType
	 * Search type: either Images or ItemAttributes.
	 * @return
	 * ArrayList of requested items.
	 */
	
	private static ArrayList<String> fetchItems(String requestUrl, String searchType) {
		ArrayList<String> items = new ArrayList<String>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(requestUrl);
			
			
			XPathFactory factory=XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			
			String path = "/ItemSearchResponse/Items/Request/IsValid";
			NodeList nl = (NodeList) xpath.evaluate(path, doc, XPathConstants.NODESET);
			
			if(searchType.equals("Images")){
		      String[] imageType = {"SmallImage", "MediumImage", "LargeImage"};
		      for(int i = 0; i < imageType.length; i++) {
			    path = "/ItemSearchResponse/Items/Item/" + imageType[i] + "/URL";
			    nl = (NodeList) xpath.evaluate(path, doc, XPathConstants.NODESET);
			   
			    items.add(nl.item(0).getTextContent());
			   	
			  }
		    }
			else if(searchType.equals("ItemAttributes")){
				String[] itemAttributeTypes = {"Author", "Title", "Binding", "EAN", "ISBN", "Languages/Language/Name"};
				for(int i = 0; i < itemAttributeTypes.length; i++){
					path = "/ItemSearchResponse/Items/Item/ItemAttributes/" + itemAttributeTypes[i];
				    nl = (NodeList) xpath.evaluate(path, doc, XPathConstants.NODESET);
				     
				    if(i == 0){
				    	String authorList = "";
					    for(int j = 0; j < nl.getLength(); j++){
					    	if(j > 0){
					    		authorList += ", ";
					    	}
					    	authorList += nl.item(j).getTextContent();
					    }
					    items.add(authorList);
				    }
				    else{
				    	items.add(nl.item(0).getTextContent());
				    }
				}
			}
		} catch (Exception e) {
			return null;
		}
		return items;
	}
	
	/**
	 * Specifies to the Amazon API exactly what is being searched for.
	 * @param responseGroup
	 * What is being searched for: either Images or ItemAttributes.
	 * @param keywords
	 * Keyword to specify Amazon search.
	 * @return
	 * ArrayList of requested items returned from fetchItems(String, String).
	 * 
	 */
	public ArrayList<String> specifySearch(String responseGroup, String keywords){
		/*
		* Here is an example in map form, where the request parameters are stored in a map.
		*/
		
		String requestUrl = null;
		
		/* The helper can sign requests in two forms - map form and string form */

		//System.out.println("example:");
		Map<String, String> params = new HashMap<String, String>();
		params.put("Service", "AWSECommerceService");
		params.put("Version", "2012-07-20");
		params.put("Operation", "ItemSearch");
		params.put("SearchIndex", "All");
		params.put("Keywords", keywords);
		params.put("ResponseGroup", responseGroup);
		params.put("AssociateTag", "team4");
		requestUrl = helper.sign(params);
		//System.out.println("Signed Request is \"" + requestUrl + "\"");
		//ArrayList<String> titles = fetchTitles(requestUrl,"Description");
		
		return fetchItems(requestUrl, responseGroup);
	}
	
	
	/**
	 * Set up the SignedRequestHelper class.
	 */
	public void setUpHelper(){
		try {
			helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}	
	}
	
}//End of class ItemSearcher.java
