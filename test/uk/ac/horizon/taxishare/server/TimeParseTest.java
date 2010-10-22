package uk.ac.horizon.taxishare.server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.Test;


public class TimeParseTest
{
	@Test
	public void testTimeParsing() throws Exception
	{
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.parse("2010-10-21 13:54:16Z");
		formatter.parse("2010-10-21 14:02:27Z");		
	}
}
