/**
 * @author medhapraneethreddy
 * Name : Medha Praneeth Reddy Modulla
 * Andrew Id : mmodulla
 */
package edu.cmu.mmodulla.mealapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static String searchTerm;
    public static TextView result;

    public static ImageView mealsImage;

    /**
     * Method() onCreate
     * Used to set the main view of the android application on startup.
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MainActivity ma = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button submitButton = findViewById(R.id.button);
        result = findViewById(R.id.outputView);
        mealsImage = findViewById(R.id.mealsImage);

        /**
         * The OnClickListener is responsible for tracking the submit button present on the
         * android UI.
         */

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                searchTerm = ((EditText) findViewById(R.id.userInput)).getText().toString().toLowerCase().trim();
                Model m = new Model();
                m.execute();
            }
        });
    }
}