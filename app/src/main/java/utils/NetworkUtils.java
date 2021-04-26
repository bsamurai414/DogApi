package utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {
    private static final String DOG_API_BASE_URL = "https://dog.ceo";
    private static final String DOG_BREED_URL = "/api/breed/";
    private static final String DOG_RANDOM_IMAGES = "/images/random";

    public static String generateURL(String userId) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("dog.ceo")
                .appendPath("api")
                .appendPath("breed")
                .appendPath(userId)
                .appendPath("images")
                .appendPath("random");
        String myUrl = builder.build().toString();
        return myUrl;
    }


    public static String GetJpg (String json) {
        String jpg = null;

        try {
            JSONObject jsonResponse = new JSONObject(json);

            jpg = jsonResponse.getString("message");
            Log.i("Log10", jpg);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("Loglog", "error24");
        }

        return jpg;
    }
}
