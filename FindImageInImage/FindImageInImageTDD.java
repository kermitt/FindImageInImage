
package find;

public class FindImageInImageTDD {

	private static FindImageInImageMain compare = new FindImageInImageMain();

	public static void main(String[] args) {

		FindImageInImageTDD tdd = new FindImageInImageTDD();

		tdd.doesContextContainTarget("big_context_image.PNG",
				"target_ought_to_be_found.PNG", true); // should be found
		
		tdd.doesContextContainTarget("RegenceWelcome.PNG",
				"target_ought_NOT_be_found.PNG", false); // should _not_ be found

	}

	private void doesContextContainTarget(String context, String target,
			boolean expected) {
		boolean actual = compare.isInside(context, target);

		String pass_or_fail = expected == actual ? "PASS" : "FAIL";
		String msg = "doesContextContainTarget "; 
		msg += " ( expected result is " + expected;
		msg += " and the actual is " + actual + ")";
		
		System.out.println( pass_or_fail + "   " + msg );

	}
}
