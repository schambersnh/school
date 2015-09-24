import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * The BookRequestInfo class uses the Amazon API to fetch book information given an ISBN number. It is important to have a stable Internet connection 
 * when requesting book information from Amazon. The BookRequestInfo class is only in charge of storing information about a book that is handed to it by ItemSearcher.
 * @author Team11
 *
 */
public class BookRequestInfo {
	
	/**
	 * The ItemSearcher used to request information from the Amazon API.
	 */
	private ItemSearcher itemSearch;
	
	/**
	 * An image of the retrieved book.
	 */
	private ImageIcon smallImage, mediumImage, largeImage;
	
	/**
	 * The author of the retrieved book.
	 */
	private String author;
	
	/**
	 * The binding of the retrieved book (hard cover or paperback).
	 */
	private String binding;
	
	/**
	 * A unique ISBN number associated with the retrieved book.
	 */
	private String ISBN10, ISBN13;
	
	/**
	 * The title of the retrieved book.
	 */
	private String title;
	
	/**
	 * The language the retrieved book is written in.
	 */
	private String language;
    
	/**
	 * Boolean indicating if the BookRequestInfo is safe to sue.
	 */
    private boolean isValid = true;

	/**
	 * Creates the BookRequestInfo, populating all necessary book fields.
	 * @param isbn
	 * The ISBN of the book that information is being gathered from.
	 */
	public BookRequestInfo(String isbn)
	{
		itemSearch = new ItemSearcher();
		retrieveImages(isbn);
		retrieveItemAttributes(isbn);
		
	}

	/**
	 * 
	 * @return
	 * Small image of the book.
	 */
	public ImageIcon getSmallImage()
	{
		return smallImage;
	}
	
	/**
	 * 
	 * @return
	 * Medium image of the book.
	 */
	public ImageIcon getMediumImage()
	{
		return mediumImage;
	}

	/**
	 * 
	 * @return
	 * Large image of the book.
	 */
	public ImageIcon getLargeImage()
	{
		return largeImage;
	}

	/**
	 * 
	 * @return
	 * Author of the book.
	 */
	public String getAuthor(){
		return author;
	}
	
	/**
	 * 
	 * @return
	 * Binding of the book.
	 */
	public String getBinding(){
		return binding;
	}
	
	/**
	 * 
	 * @return
	 * ISBN10 of the book.
	 */
	public String getISBN10(){
		return ISBN10;
	}
	
	/**
	 * 
	 * @return
	 * ISBN13 of the book.
	 */
	public String getISBN13(){
		return ISBN13;
	}

	/**
	 * 
	 * @return
	 * Title of the book.
	 */
	public String getTitle(){
		return title;
	}
	
	/**
	 * 
	 * @return
	 * Language of the book.
	 */
	public String getLanguage(){
		return language;
	}
	
	/**
	 * This method retrieves all three images of the book being searched.
	 * @param myISBN
	 * The ISBN of the book that information is being gathered from.
	 */
	public void retrieveImages(String myISBN){
		ArrayList<String> images = itemSearch.specifySearch("Images", myISBN);
		if(images == null){
			isValid = false;
		} else
			try {
				setImages(images);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
	}

	public void retrieveItemAttributes(String myISBN){
		ArrayList<String> attributes = itemSearch.specifySearch("ItemAttributes", myISBN);
		if(attributes == null){
			//System.err.println("HEY 2");
			isValid = false;
		}
		else
			setItemAttributes(attributes);
	}
	
	/**
	 * Converts the URL given by the Amazon API to an ImageIcon so it can be set in a JLabel.
	 * @param url
	 * The URL of the image returned by the Amazon API.
	 * @return
	 * An ImageIcon that can be set in a JLabel.
	 */
	public ImageIcon convertToImageIcon(String url)
	{
		try {
			URL u = new URL(url);
			return new ImageIcon(u);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @return
	 * <pre>
	 * 		true if the BookRequestInfo was fed a valid ISBN
	 * </pre>
	 */
	public boolean isValid(){
		return isValid;
	}
	
	/**
	 * Sets all of the book images for later access.
	 * @param images
	 * ArrayList of book images<br>
	 * <pre>
	 *      0-Small Image
	 *      1-Medium Image
	 *      2-LargeImage
	 * </pre>
	 */
	public void setImages(ArrayList<String> images) throws MalformedURLException
	{
		smallImage = convertToImageIcon(images.get(0));
		mediumImage = convertToImageIcon(images.get(1));
		largeImage = convertToImageIcon(images.get(2));
		
		if(largeImage.getIconHeight() > GUIFactory.resizedNumberHeight(500)){
			largeImage = GUIFactory.resizeImage(new URL(images.get(2)));
		}
	}

	/**
	 * Sets all of the book attributes for later access.
	 * @param attributes
	 * ArrayList of book attributes.<br>
	 * <pre>
	 *      0-Author
	 *      1-Title
	 *      2-Binding
	 *      3-ISBN13
	 *      4-ISBN
	 *      5-Language
	 * </pre>
	 */
	public void setItemAttributes(ArrayList<String> attributes)
	{
		author = attributes.get(0);
		title = attributes.get(1);
		binding = attributes.get(2);
		ISBN13 = attributes.get(3);
		ISBN10 = attributes.get(4);
		language = attributes.get(5);
	}
} //End of class BookRequestInfo.java
