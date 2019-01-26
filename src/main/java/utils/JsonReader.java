package utils;

import java.io.BufferedReader;
import java.io.FileReader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonReader {
	private JsonObject jObj = null;

	public JsonReader(String path) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(path));
		JsonParser parser = new JsonParser();
		jObj = parser.parse(br).getAsJsonObject();
	}

	public JsonObject getRoot() {
		return jObj;
	}
}
