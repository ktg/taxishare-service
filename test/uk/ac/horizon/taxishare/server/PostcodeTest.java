package uk.ac.horizon.taxishare.server;

import org.junit.Test;

import uk.ac.horizon.taxishare.model.Location;

public class PostcodeTest
{
	@Test
	public void testPostcodeFormat()
	{
		assert (Location.formatPostcode("ng92wb").equals("NG9 2WB"));
		assert (Location.formatPostcode("ng112wb").equals("NG11 2WB"));
		assert (Location.formatPostcode("ng9 2wb").equals("NG9 2WB"));
		assert (Location.formatPostcode("NG9 2WB").equals("NG9 2WB"));
	}

	@Test
	public void testPostcodeMatching()
	{
		assert (Location.isPostcode(Location.formatPostcode("ng92WB")));

		assert (Location.isPostcode("NG9 2WB"));
		assert (Location.isPostcode("NG11 2WB"));
	}
}
