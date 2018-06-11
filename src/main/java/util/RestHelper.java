package util;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;

import javax.json.JsonArray;

public final class RestHelper {

	private static String aasJwtToken;

	private static String Identity = "JgrTojsMQvZR4WFTh0gp";

	public static String getAasJwtToken() throws UnirestException {
		if(aasJwtToken == null){
			HttpResponse<JsonNode> response = Unirest.get("http://192.168.25.122:8080/AccountAdministrationSystem/api/authenticate/"+Identity).asJson();
			JSONArray array = response.getBody().getArray();
			aasJwtToken = array.getString(0);
		}
		return aasJwtToken;
	}
}