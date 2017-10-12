/**
 * Implement a class called Instrument to represent a musical instrument. The instrument has a type, a brand,
 * a model number, a unique item id and a manufacturer's suggested retail price (msrp).
 * It may also have a markup and a discount on the msrp.
 * Provide the public methods: getType, getItemId, getBrandNModel, setMarkupPercentage, setDiscountPercentage and CalculatePrice.
 * Constructors: Provide a no-argument constructor. Make a design decision as to how you are initializing values.
 * 
 * @author Rafael Lucchesi
 * Version: 2017-01-18
 */
public class Instrument {
	public static final String GENERIC_TYPE = "generic instrument";
	public static final String NOT_AVAILABLE = "not available";
	public static final double NO_PRICE = -1.00;
		
	private static int itemNumber = 100;
	
	private String type;
	private String brand;
	private String modelNumber;
	private String itemId;
	private double msrp;
	private double markup;
	private double discount;
	
	/**
	 * Initializes a newly created Instrument object with generic entries.
	 * Justification: The type, brand and modelNumber were initialized with final Strings.
	 * 				  The price is initialized to -1.0 to discriminate that its msrp is unknown.
	 */
	public Instrument () {
		this.type = normalizeString(GENERIC_TYPE);
		this.brand = extraBlankEliminator(NOT_AVAILABLE);
		this.modelNumber = extraBlankEliminator(NOT_AVAILABLE);
		this.itemId = itemIdGenerator(this.type);
		this.msrp = NO_PRICE;
	}
	
	/**
	 * Constructs a new Instrument whose type, brand, model number and price (msrp) are specified as arguments.
	 * When the arguments passed to the constructor are “null” or an empty string, the final variables are used.
	 * @param type The instrument being entered.
	 * @param brand The name of the manufacturer.
	 * @param modelNumber The instrument has a model number (which may contain any type of characters).
	 * @param msrp The manufacturer's suggested retail price.
	 */
	public Instrument (String type, String brand, String modelNumber, double msrp) {
		if (type == null || type.equals("")) {
			this.type = normalizeString(GENERIC_TYPE);
		} else {
			this.type = normalizeString(type);
		}
		if (brand == null || brand.equals("")) {
			this.brand = extraBlankEliminator(NOT_AVAILABLE);
		} else {
			this.brand = extraBlankEliminator(brand);
		}
		if (modelNumber == null || modelNumber.equals("")) {
			this.modelNumber = extraBlankEliminator(NOT_AVAILABLE);
		} else {
			this.modelNumber = modelNumber;
		}
		if (msrp < 0.0) {
			this.msrp = NO_PRICE;
		} else {
			this.msrp = msrp;
		}

		this.itemId = itemIdGenerator(this.type);
	}
	
	/**
	 * Returns the type (name) of the instrument.
	 * @return The name of the instrument.
	 */
	public String getType () {
		return this.type;
	}
	
	/**
	 * Returns the the instrument's identification String 
	 * @return The instrument's identification
	 */
	public String getItemId () {
		return this.itemId;
	}
	
	/**
	 * Returns the name of the manufacturer and model number of the instrument as a single string with one blank in between.
	 * @return The brand and model as a single string
	 */
	public String getBrandNModel () {
		String output;
		if (this.modelNumber.equals(extraBlankEliminator(NOT_AVAILABLE))) {
			output = extraBlankEliminator(this.brand);
		} else {
			output = extraBlankEliminator(this.brand) + " " + extraBlankEliminator(this.modelNumber);
		}
		return output;
	}
	
	/**
	 * Sets the Markup Percentage for the instrument.
	 * Precondition: The percentage must be between 0 and 100.
	 * @param percentage The markup percentage to be calculated, or 0 if the argument is out of the expected domain.
	 */
	public void setMarkupPercentage (double percentage) {
		if (percentage >= 0 && percentage <= 100) {
			this.markup = percentage / 100;
		} else {
			this.markup = 0;
		}
	}
	
	/**
	 * Sets the Discount Percentage for the instrument.
	 * Precondition: The percentage must be between 0 and 100.
	 * @param percentage The discount percentage to be calculated, or 0 if the argument is out of the expected domain. 
	 */
	public void setDiscountPercentage (double percentage) {
		if (percentage >= 0 && percentage <= 100) {
			this.discount = percentage / 100;
		} else {
			this.discount = 0;
		}
	}
	
	
	/**
	 * Calculates the price of the instrument, applying first the markup and then the discount to the msrp.
	 * @return The calculated Price if the variable msrp is valid, or NO_PRICE if the price was not set when the object was created.
	 */
	public double calculatePrice () {
		if (this.msrp >= 0) {
			double outputMarkup, outputDiscount;
			outputMarkup = this.msrp + this.msrp * this.markup;
			outputDiscount = outputMarkup - outputMarkup * this.discount;
			return outputDiscount;
		} else {
			return NO_PRICE;
		}
	}
	
	
	/**
	 * Normalizes the argument by removing all the extra white spaces and then translating all characters to lower case.
	 * The removal of the extra white spaces is done by the method extraBlankEliminator
	 * @param input The string to be normalized
	 * @return The normalized String
	 */
	private static String normalizeString (String input) {
		String output;
		output = extraBlankEliminator(input);
		output = output.toLowerCase();
		return output;
	}
	
	/**
	 * Removes all the extra blanks from the argument String.
	 * The returning string has no leading blanks, no trailing blanks and no more than one blank in between words.
	 * @param input The String to be used.
	 * @return The String after having all extra blanks removed.
	 */
	private static String extraBlankEliminator (String input) {
		String output = input.trim();
		for (int i = 0; i < output.length(); i++) {
			if (output.charAt(i) == ' ' && output.charAt(i + 1) == ' ') {
				output = output.substring(0, i) + output.substring(i + 1);
				i--;
			}
		}
		return output;
	}

	/**
	 * Generates the ‘item id’, which consists of the first letter of the type (after the type has been normalized) concatenated with the current ‘item number’.
	 * @param type Instrument's type
	 * @return The item Identification number
	 */
	private static String itemIdGenerator (String type) {
		return "" + type.charAt(0) + itemNumber++;
	}
}