package uk.ac.horizon.taxishare.server;

import org.junit.Test;

import uk.ac.horizon.taxishare.model.Destination;

public class PostcodeTest
{
	@Test
	public void testPostcodeFormat()
	{
		assert (Destination.formatPostcode("ng92wb").equals("NG9 2WB"));
		assert (Destination.formatPostcode("ng112wb").equals("NG11 2WB"));
		assert (Destination.formatPostcode("ng9 2wb").equals("NG9 2WB"));
		assert (Destination.formatPostcode("NG9 2WB").equals("NG9 2WB"));
	}

	@Test
	public void testPostcodeMatching()
	{
		assert (Destination.isPostcode(Destination.formatPostcode("ng92WB")));

		assert (Destination.isPostcode("NG9 2WB"));
		assert (Destination.isPostcode("NG11 2WB"));
	}
}
