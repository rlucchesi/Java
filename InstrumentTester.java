/**
 * A test program created to verify that the Instrument class methods have been implemented correctly.
 * Two functions were implemented to help throughout the tests: instrument_Tester() and calculatePriceTester(). A counter was implemented to help keep track of the results.
 * @author Rafael Lucchesi
 * Version: 2017-01-18
 */
public class InstrumentTester {
	private static int testCounter = 1;
	
	/**
	 * The functions instrument_Tester() and calculatePriceTester() are called with different parameters to test the Instrument class, as described in the inline comments.
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * instrument_Tester() with several different arguments.
		 */
		// Test 1 --> First object - typical values used. True is expected for every test.
		Instrument a = new Instrument("English Horn", "Fox", "555", 100);
		instrument_Tester(a, "english horn", "Fox", "555");

		// Test 2 --> Second object - Extra blank spaces in all Strings. False is expected for every test.
		Instrument b = new Instrument("  Brazilian      Cuica    ", "   Cacildis     ", "     171   ", 200);
		instrument_Tester(b, "  Brazilian      Cuica    ", "   Cacildis     ", "     171   ");

		// Test 3 --> Third object - Empty String in the type argument. False is expected for the type test.
		Instrument c = new Instrument("", "Maharaja Musicals", "194/H", 400);
		instrument_Tester(c, "", "Maharaja Musicals", "194/H");

			// Test 4 --> Third object - Instrument.GENERIC_TYPE used as type argument. True is expected for every test.
			instrument_Tester(c, Instrument.GENERIC_TYPE, "Maharaja Musicals", "194/H");

		// Test 5 --> Fourth object - Empty String in the brand argument. False is expected for the BRAND N MODEL test.
		Instrument d = new Instrument("Sitar", "", "194/H", 400);
		instrument_Tester(d, "sitar", "", "194/H");

			// Test 6 --> Fourth object - Instrument.NOT_AVAILABLE used as brand argument. True is expected for every test.
			instrument_Tester(d, "sitar", Instrument.NOT_AVAILABLE, "194/H");

		// Test 7 --> Fifth object - Empty String used as object's modelNumber argument. True is expected for every test because if the model is an empty string, only the object's brand is used by the Instrument class's function getBrandNModel().
		Instrument e = new Instrument("Sitar", "Maharaja Musicals", "", 400);
		instrument_Tester(e, "sitar", "Maharaja Musicals", "");

		// Test 8 --> Sixth object - No-argument Constructor test. True is expected in for every test.
		Instrument f = new Instrument();
		instrument_Tester(f, Instrument.GENERIC_TYPE, Instrument.NOT_AVAILABLE, Instrument.NOT_AVAILABLE);

		// Test 9 --> Seventh object - Null used as object's type argument. The this.type variable is initialized to Instrument.GENERIC_TYPE. True is expected for every test. 
		Instrument g = new Instrument(null, "Calcutta Musical Depot", "282", 500);
		instrument_Tester(g, Instrument.GENERIC_TYPE, "Calcutta Musical Depot", "282");
				
		// Test 10 --> Eighth object - Null used as object's brand argument. The this.brand variable is initialized to Instrument.NOT_AVAILABLE. True is expected for every test.
		Instrument h = new Instrument("Harmonium", null, "282", 500);
		instrument_Tester(h, "harmonium", Instrument.NOT_AVAILABLE, "282");
				
		// Test 11 --> Ninth object - Null used as object's modelNumber argument. The this.modelNumber variable is initialized to Instrument.NOT_AVAILABLE. True is expected for every test.
		Instrument i = new Instrument("Harmonium", "Calcutta Musical Depot", null, 400);
		instrument_Tester(i, "harmonium", "Calcutta Musical Depot", Instrument.NOT_AVAILABLE);
				
				

		/**
		 * calculatePriceTester() with several different arguments.
		 */
		// Test 12 --> No markup or discount was set. True is expected.
		calculatePriceTester(a, 100, 0, 0);

		// Test 13 --> Testing the calculated price with 20% markup only. True is expected.
		a.setMarkupPercentage(20);
		calculatePriceTester(a, 100, 20, 0);

		// Test 14 --> Testing the calculated price with -20% markup only. False is expected.
		a.setMarkupPercentage(-20);
		calculatePriceTester(a, 100, -20, 0);

		// Test 15 --> Testing the calculated price with 150% markup only. False is expected.
		a.setMarkupPercentage(150);
		calculatePriceTester(a, 100, 150, 0);

		// Test 16 --> Testing the calculated price with 10% discount only. True is expected.
		a.setMarkupPercentage(0);
		a.setDiscountPercentage(10);
		calculatePriceTester(a, 100, 0, 10);

		// Test 17 --> Testing the calculated price with -25% discount only. False is expected.
		a.setDiscountPercentage(-25);
		calculatePriceTester(a, 100, 0, -25);

		// Test 18 --> Testing the calculated price with 105% discount only. False is expected.
		a.setDiscountPercentage(105);
		calculatePriceTester(a, 100, 0, 105);

		// Test 19 --> Testing the calculated price with 30% markup 50% discount. True is expected.
		a.setMarkupPercentage(30);
		a.setDiscountPercentage(50);
		calculatePriceTester(a, 100, 30, 50);
		
		/**
		 * Several tests with the price of an object created by the non-argument constructor.
		 * The calculatePrice() of a object created by a non-argument Constructor only returns -1.0. False should be the output for all tests except 17.
		 */
		// Test 20 --> No markup or discount was set. True is expected.
		calculatePriceTester(f, Instrument.NO_PRICE, 0, 0);
		
		// Test 21 --> Testing the calculated price with 20% markup only. False is expected.
		f.setMarkupPercentage(20);
		calculatePriceTester(f, Instrument.NO_PRICE, 20, 0);

		// Test 22 --> Testing the calculated price with -20% markup only. False is expected.
		f.setMarkupPercentage(-20);
		calculatePriceTester(f, Instrument.NO_PRICE, -20, 0);

		// Test 23 --> Testing the calculated price with 150% markup only. False is expected.
		f.setMarkupPercentage(150);
		calculatePriceTester(f, Instrument.NO_PRICE, 150, 0);

		// Test 24 --> Testing the calculated price with 10% discount only. False is expected.
		f.setMarkupPercentage(0);
		f.setDiscountPercentage(10);
		calculatePriceTester(f, Instrument.NO_PRICE, 0, 10);

		// Test 25 --> Testing the calculated price with -25% discount only. False is expected.
		f.setDiscountPercentage(-25);
		calculatePriceTester(f, Instrument.NO_PRICE, 0, -25);

		// Test 26 --> Testing the calculated price with 105% discount only. False is expected.
		f.setDiscountPercentage(105);
		calculatePriceTester(f, Instrument.NO_PRICE, 0, 105);

		// Test 27 --> Testing the calculated price with 30% markup 50% discount. False is expected.
		f.setMarkupPercentage(30);
		f.setDiscountPercentage(50);
		calculatePriceTester(f, Instrument.NO_PRICE, 30, 50);
	}
	
	/**
	 * Tests the following Instrument class methods: getItemId(), getType(), and getBrandNModel().
 	 * The output should be true if the field matches.
	 * @param input The object to be tested.
	 * @param type The type expected to be stored in the object.
	 * @param brand The brand expected to be stored in the object.
	 * @param Model The model expected to be stored in the object.
	 */
	private static void instrument_Tester (Instrument input, String type, String brand, String model) {
		boolean brandModel;
		
		if (input.getBrandNModel().equals(Instrument.NOT_AVAILABLE) || input.getBrandNModel().equals(brand)) {				// No-argument constructor has been used || The instrument_Tester's model argument was empty when creating the object
			brandModel = brand.equals(input.getBrandNModel());																											// According to the assignment, only the brand should be displayed in such cases.
		} else {
			brandModel = ((brand + " " + model).equals(input.getBrandNModel()));											// Regular use of Instrument class constructor.
		}
		
		System.out.println("Instrument Tester: Test " + testCounter++ + "\r\nObject's properties: \t[ItemId: " + input.getItemId() + ", Type: " + input.getType() + ", Brand N Model: " + input.getBrandNModel() + "]");
		System.out.println("TYPE TEST: Compares the object's \"type\" with the argument \"type\": " + (type.equals(input.getType())));
		System.out.println("BRAND N MODEL TEST: Compares the object's \"brand and model\" with the arguments \"brand and model\": " + brandModel);
		System.out.println();
	}
	
	/**
	 * Tests the method calculatePrice() from the Instrument class.
 	 * The output should be true if the valued returned by calculatePrice() is equal to the price calculated using the arguments.
	 * @param input The object to be tested.
	 * @param initialPrice The initial price assigned for the object.
	 * @param markup The markup percentage to be used in the test.
	 * @param discount The discount percentage to be used in the test.
	 */
	private static void calculatePriceTester (Instrument input, double initialPrice, double markup, double discount) {
		double calculatedPrice;
		
		calculatedPrice = initialPrice + (initialPrice * markup / 100);
		calculatedPrice = calculatedPrice - (calculatedPrice * discount / 100);
		
		System.out.println("Markup and Discount: Test " + testCounter++ + "\r\nObject's properties: \t[ItemId: " + input.getItemId() + ", Type: " + input.getType() + ", Brand N Model: " + input.getBrandNModel() + "]");
		System.out.println("Object's calculated price: " + input.calculatePrice() + ", arguments calculated price: " + calculatedPrice);
		System.out.println("Is object's calculated price equal to the arguments calculated price: " + (input.calculatePrice() == calculatedPrice));
		System.out.println();
	}

}
