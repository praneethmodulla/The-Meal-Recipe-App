/**
 * @author medhapraneethreddy
 * Name : Medha Praneeth Reddy Modulla
 * Andrew Id : mmodulla
 */
package edu.cmu.mmodulla.mealapplication;

import android.os.AsyncTask;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

import static edu.cmu.mmodulla.mealapplication.MainActivity.searchTerm;
public class Model extends AsyncTask<Void, Void, Void> {
    boolean nullTerm = false;
    String url;
    Bitmap bmp;
    String imageURL;
    String inputLine;
    StringBuffer response = new StringBuffer();
    String outputData;
    String finalOutputData = "";
    ImageView outputImage;

    /**
     * Method() doInBackground
     * doInBackground is the helper thread. We first make a connection with the URL.
     * Once we get a response we get imageURL of the meal and fetch the image.
     * Post this we display the data received in the screen of the application.
     * @param voids
     * @return
     */

    @Override
    protected Void doInBackground(Void... voids) {

        /**
         *  API endpoint for Project 4 Task 2.
         */

        url = "https://medhapraneethmodulla-symmetrical-space-7v7rvpjgwx9cr499-8080.preview.app.github.dev/";

        try {
            if (searchTerm.trim().isEmpty()) {
                nullTerm = true;
            } else {
                URL obj = new URL(url + "addResult?s=" + searchTerm.trim());
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                if (response.toString().equals("{\"meals\":null}") || response.toString().isEmpty() || response.toString().equals(null)) {
                    nullTerm = true;
                } else {
                    JsonObject jsonObject = new Gson().fromJson(response.toString(), JsonObject.class);
                    JsonElement jsonElement = jsonObject.get("meals");
                    JsonArray jsonArray = jsonElement.getAsJsonArray();

                    JsonObject jsonObj = jsonArray.get(0).getAsJsonObject();

                    imageURL =  jsonObj.get("strMealThumb").toString();
                    imageURL = imageURL.substring(1, imageURL.length()-1);
                    String mealName = jsonObj.get("strMeal").toString();
                    mealName = mealName.substring(1, mealName.length()-1);
                    String category = jsonObj.get("strCategory").toString();
                    category = category.substring(1, category.length()-1);
                    String instructions = jsonObj.get("strInstructions").toString();
                    instructions = instructions.substring(1, instructions.length()-1);
                    URL imageOutputURL = new URL(imageURL);
                    URLConnection conn = imageOutputURL.openConnection();
                    conn.connect();
                    BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                    bmp = BitmapFactory.decodeStream(bis);
                    outputData = "\n" +
                            mealName + " (" + category + ")\n\n" +
                            instructions + "\n\n";
                    finalOutputData += outputData;
                }
            }

            /**
             * Exception handling
             */

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method() onPostExecute
     * This method operates on the UI thread and its purpose is to display and refresh the
     * Android user interface according to the received response.
     * When all the necessary data has been retrieved, the method then proceeds to present the final result,
     * which includes both text and images, on the Android UI.
     * @param aVoid
     */

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (nullTerm) {
            MainActivity.result.setText("\nSorry we don't have any image & instructions for that meal. Please search for another meal.\n ");
            MainActivity.mealsImage.setImageBitmap(null);
            MainActivity.mealsImage.setVisibility(View.VISIBLE);
        } else {
            MainActivity.result.setText(this.finalOutputData);
            MainActivity.mealsImage.setImageBitmap(bmp);
            MainActivity.mealsImage.setVisibility(View.VISIBLE);
        }
    }
}
