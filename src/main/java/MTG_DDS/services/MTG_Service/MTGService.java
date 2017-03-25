package MTG_DDS.services.MTG_Service;

import com.google.gson.*;
import io.magicthegathering.javasdk.exception.HttpRequestFailedException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MTGService {
	protected final static String ENDPOINT = "https://api.magicthegathering.io/v1";
	protected static OkHttpClient CLIENT = new OkHttpClient();

	protected static <TYPE> List<TYPE> getList(String path, String key,
											   Class<TYPE> expectedClass) {
		Gson deserializer = new GsonBuilder().create();
		List<TYPE> toReturn = new ArrayList<TYPE>();
		JsonObject jsonObject = getJsonObject(path, deserializer);

		for (JsonElement jsonElement :
				jsonObject.get(key).getAsJsonArray()) {
			toReturn.add(deserializer.fromJson(
					jsonElement, expectedClass));
		}

		return toReturn;
	}

	/**
	 * Get JSON from delegated @param path
	 *
	 * @param path
	 * @param deserializer
	 * @return
	 */
	private static JsonObject getJsonObject(String path, Gson deserializer) {
		String url = String.format("%s/%s", ENDPOINT, path);
		Request request = new Request.Builder().url(url).build();
		Response response;
		try {
			response = CLIENT.newCall(request).execute();
			JsonObject jsonObject = deserializer.fromJson(response.body()
					.string(), JsonObject.class);
			return jsonObject;
		} catch (IOException e) {
			throw new HttpRequestFailedException(e);
		}

	}

	public List<String> getAllCards(){
		Gson deserializer = new GsonBuilder().create();
		JsonObject jsonObject = getJsonObject("cards", deserializer);
		JsonArray names = jsonObject.getAsJsonArray("cards");
		List<String> images = new ArrayList<String>();
		names.forEach(name -> images.add(name.getAsJsonObject().get("imageUrl").getAsString()));
		return images;
	}

}
