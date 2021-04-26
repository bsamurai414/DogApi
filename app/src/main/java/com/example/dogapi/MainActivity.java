package com.example.dogapi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static utils.NetworkUtils.GetJpg;
import static utils.NetworkUtils.generateURL;




public class MainActivity extends AppCompatActivity {
    public String text_view_dog_breed = "";
    ImageView image;
    Button button;
    ProgressDialog mProgressDialog;
    //AutoCompleteTextView dogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = findViewById(R.id.image);
        button = findViewById(R.id.buttonId);

        //set auto complete
        final AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.edit_ip);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.myarray));
        textView.setAdapter(adapter);


        button.setOnClickListener(v -> {
            text_view_dog_breed = textView.getText().toString();
            String generatedURL = generateURL(text_view_dog_breed);
            new DownloadImage().execute(generatedURL);
        });



        //set spinner
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_ip);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(spinner.getSelectedItem().toString());
                textView.dismissDropDown();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textView.setText(spinner.getSelectedItem().toString());
                textView.dismissDropDown();
            }
        });



    }


    @SuppressLint("StaticFieldLeak")
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Картинка загружается...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... params) {


            //Делаем https запрос и сохраняем ответ (в формате json) в переменную buffer
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String JpgString = null;
            Bitmap bitmap = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                    //В JpgString хранится адресс до картинки (В строке line хранится json)
                    JpgString = GetJpg(line);
                }


                try {

                    // Download Image from URL
                    InputStream input = new java.net.URL(JpgString).openStream();
                    Log.i("Log1", input.toString());

                    // Decode Bitmap
                    bitmap = BitmapFactory.decodeStream(input);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return bitmap;




            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.i("L2", "error2" );

            } catch (IOException e) {
                e.printStackTrace();
                Log.i("L111", "error1" );
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("L3", "error3" );

                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Bitmap response) {

            // Set the bitmap into ImageView
            image.setImageBitmap(response);

            // Close progressdialog
            mProgressDialog.dismiss();
        }
    }
}