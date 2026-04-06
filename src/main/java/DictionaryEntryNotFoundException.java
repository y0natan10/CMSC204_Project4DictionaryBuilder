//Yonatan Rubin
//M21105076

/**
 * short exception class for in case of a word not being found in a dictionary
 */
public class DictionaryEntryNotFoundException extends Exception {

	/**
	 * the yeet function
	 *
	 * @param message
	 */
	public DictionaryEntryNotFoundException(String message) {
		super(message);
	}
}
