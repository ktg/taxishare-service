package bzb.gwt.taxishare.server;

import bzb.gwt.taxishare.client.GreetingService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {
	
	public String greetServer(String input) {
		// order taxistatus by arrival time
		// only pass on taxistatus unconfirmed | confirmed, not left
		
		/*String testStatus = new String("{\"TaxiStatus\" : [");
		for (int i = 0; i < 2; i++) {
			testStatus += "{" +
			"\"key\" : {" +
			"\"requestID\" : " + i +
			"}," +
			"\"required\" : {" +
			"\"ownerID\" : 1," +
			"\"destinationName\" : \"Ben's house\"," +
			"\"destinationPostcode\" : \"ng162qa\"," +
			"\"requestTime\" : 1285697400," +
			"\"arrivalTime\" : 1285700100," +
			"\"status\" : \"confirmed\"" +
			"}," +
			"\"optional\" : {" +
			"\"company\" : \"dg\"," +
			"\"pickupTime\" : 1285698600," +
			"\"predictedCost\" : 12," +
			"\"totalSpace\" : 5," +
			"\"spaceLeft\" : 3" +
			"}" +
			"},";
		}
		testStatus += "], \"Destination\" : [";
		for (int i = 0; i < 3; i++) {
			testStatus += "{" +
			"\"key\" : {" +
			"\"destinationID\" : " + i +
			"}," +
			"\"required\" : {" +
			"\"name\" : \"dest" + i + "\"," +
			"\"postcode\" : \"NG162QA\"}},";
		}
		testStatus += "], \"Instance\" : {" +
			"\"key\" : {" +
			"\"instanceID\" : " + input + 
			"}," +
			"\"required\" : {" +
			"\"destinationID\" : " + 1 + "," +
			"\"name\" : \"emcc\"," +
			"\"phone\" : \"07786202578\"," +
			"\"postcode\" : \"ng72rj\"}}}";
		System.out.println(testStatus);*/
		//String testStatus = "{\"TaxiStatus\": [{\"key\": {\"requestID\": 0}, \"required\": {\"ownerID\": 1, \"destinationName\": \"Train Station\", \"destinationPostcode\": \"ng23aq\", \"requestTime\": 1285697400, \"arrivalTime\": 1285700100, \"status\": \"confirmed\"}, \"optional\": {\"company\": \"DG\", \"pickupTime\": 1285698600, \"predictedCost\": 12, \n\"totalSpace\": 5, \"spaceLeft\": 3}},{\"key\": {\"requestID\": 1}, \"required\": {\"ownerID\": 2, \"destinationName\": \"Jubilee Campus\", \"destinationPostcode\": \"ng81bb\", \"requestTime\": 1285697520, \t\t\t\t\"arrivalTime\": 1285698960, \"status\": \"unconfirmed\"}, \"optional\": {\"company\": \"Cable\", \"pickupTime\": 1285698100, \"predictedCost\": 12, \"totalSpace\": 4, \"spaceLeft\": 3}},{\"key\": {\"requestID\": 2}, \"required\": {\"ownerID\": 3, \"destinationName\": \"Crowne Plaza\", \"destinationPostcode\": \"ng15rh\", \"requestTime\": 1285697520, \"arrivalTime\": 1285698960, \"status\": \"confirmed\"}, \"optional\": {\"company\": \"DG\", \"pickupTime\": 1285698100, \"predictedCost\": 7, \"totalSpace\": 4, \"spaceLeft\": 0}}], \"Destination\": [{\"key\": {\"destinationID\": 0}, \"required\": {\"name\": \"Train station\", \"postcode\": \"ng23aq\"}},{\"key\": {\"destinationID\": 1}, \"required\": {\"name\": \"Crowne Plaza\", \"postcode\": \"ng15rh\"}},{\"key\": {\"destinationID\": 2}, \"required\": {\"name\": \"Jubilee Campus\", \"postcode\": \"ng81bb\"}}], \"Instance\": {\"key\": {\"instanceID\": 0}, \"required\": {\"destinationID\": 3, \"name\": \"emcc\", \"phone\": \"07786202578\", \"postcode\": \"ng72rj\"}}}";
		
		String testStatus = "{\"id\":31,\"location\":{\"id\":27,\"name\":\"EMCC\",\"postcode\":\"NG7 2RJ\"},\"destinations\":[{\"id\":28,\"name\":\"Home\",\"postcode\":\"NG9 2WB\"},{\"id\":29,\"name\":\"Station\",\"postcode\":\"NG2 3AQ\"}],\"taxis\":[{\"id\":11,\"destination\":{\"id\":28,\"name\":\"Home\",\"postcode\":\"NG9 2WB\"},\"people\":[{\"id\":30,\"name\":\"Kevin\",\"number\":\"07796698175\"}],\"requestTime\":\"20-Oct-2010 15:41:14\",\"totalSpace\":4,\"predictedCost\":20.0,\"status\":\"unconfirmed\"},{\"id\":12,\"destination\":{\"id\":29,\"name\":\"Station\",\"postcode\":\"NG2 3AQ\"},\"people\":[],\"requestTime\":\"20-Oct-2010 15:41:14\",\"totalSpace\":4,\"predictedCost\":20.0,\"status\":\"unconfirmed\"}],\"enabled\":true,\"number\":\"07798898175\"}";
		return testStatus;
	}
}
