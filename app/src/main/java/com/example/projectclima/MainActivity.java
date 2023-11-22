package com.example.projectclima;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    String City = "Palhoça";
    //Your Key
    String Key = "2a20d2a71c5a0737f88861d43949d630";

    String url1 = "https://samples.openweathermap.org/data/2.5/weather?q=Palhoça&appid=439d4b804bc8187953eb36d2a8c26a02";



    TextView txtCity,txtTime,txtValue,txtValueFeelLike,txtValueHumidity,txtValueVision, title2, title;

    String nameIcon = "10d";

    EditText editText;

    Button btnLoading;

    Button btnLocal;

    ImageView imgIcon;

    RelativeLayout relativeLayoutMain;
    RelativeLayout relativeLayout;
    RelativeLayout rlMain2;


    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;

            URL url;

            HttpURLConnection httpURLConnection;

            InputStream inputStream;

            try {
                Log.i("LINK",strings[0]);
                url = new URL(strings[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                inputStream = httpURLConnection.getInputStream();

                bitmap = BitmapFactory.decodeStream(inputStream);


            } catch (MalformedURLException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "URL mal formatada", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }
    }

    public class DownloadTask extends AsyncTask<String, Void , String> {
        @Override
        protected String doInBackground(String... strings) {

            String result = "";

            URL url;

            HttpURLConnection httpURLConnection;

            InputStream inputStream;

            InputStreamReader inputStreamReader;

            try {

                url = new URL(strings[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                inputStream = httpURLConnection.getInputStream();

                inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while(data != -1) {

                    result += (char) data;

                    data = inputStreamReader.read();

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "URL mal formatada", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }
    }

    public void Voltar(View view) {


        editText.setVisibility(View.VISIBLE);
        btnLoading.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.INVISIBLE);
        title2.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        btnLocal.setVisibility(View.VISIBLE);
        rlMain2.setVisibility(View.VISIBLE);
        relativeLayoutMain.setBackgroundColor(Color.parseColor("#E6E6E6"));}

    public void Clima(View view) {

        City = editText.getText().toString();

        City = City.substring(0, 1).toUpperCase() + City.substring(1).toLowerCase();

        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + City +"&units=metric&appid=" + Key;

        DownloadTask downloadTask = new DownloadTask();

        try {

            String result = "abc";

            result = downloadTask.execute(url).get();

            Log.i("Result:",result);

            JSONObject jsonObject = new JSONObject(result);


            editText.setVisibility(View.INVISIBLE);
            btnLoading.setVisibility(View.INVISIBLE);
            relativeLayout.setVisibility(View.VISIBLE);
            title2.setVisibility(View.INVISIBLE);
            btnLocal.setVisibility(View.INVISIBLE);
            title.setVisibility(View.INVISIBLE);
            rlMain2.setVisibility(View.INVISIBLE);
            relativeLayoutMain.setBackgroundColor(Color.parseColor("#E6E6E6"));

            JSONObject main = jsonObject.getJSONObject("main");

            String temp = main.getString("temp");

            String humidity = main.getString("humidity");

            String feel_like = main.getString("feels_like");

            String visibility = jsonObject.getString("visibility");

            nameIcon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");

            Log.i("Name Icon",nameIcon);

            Long time = jsonObject.getLong("dt");

            String sTime = new SimpleDateFormat("dd-M-yyyy", Locale.ENGLISH)
                    .format(new Date(time * 1000));

            txtTime.setText(sTime);

            txtCity.setText(City);

            txtValue.setText(temp + "°");

            txtValueVision.setText(visibility + " Metros");

            txtValueHumidity.setText(humidity + "%");

            txtValueFeelLike.setText(feel_like + "°");

            DownloadImage downloadImage = new DownloadImage();

            String urlIcon = " https://openweathermap.org/img/wn/"+ nameIcon +"@2x.png";

            Bitmap bitmap = downloadImage.execute(urlIcon).get();

            imgIcon.setImageBitmap(bitmap);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Cidade não encontrada", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void Local(View view) {


        String url2 = "https://api.openweathermap.org/data/2.5/weather?q=São José&units=metric&appid=2a20d2a71c5a0737f88861d43949d630";

        DownloadTask downloadTask = new DownloadTask();

        try {

            String result = "abc";

            result = downloadTask.execute(url2).get();

            Log.i("Result:",result);

            JSONObject jsonObject = new JSONObject(result);


            editText.setVisibility(View.INVISIBLE);
            btnLoading.setVisibility(View.INVISIBLE);
            btnLocal.setVisibility(View.INVISIBLE);
            relativeLayout.setVisibility(View.VISIBLE);
            title2.setVisibility(View.INVISIBLE);
            title.setVisibility(View.INVISIBLE);
            rlMain2.setVisibility(View.INVISIBLE);
            relativeLayoutMain.setBackgroundColor(Color.parseColor("#E6E6E6"));

            JSONObject main = jsonObject.getJSONObject("main");

            String temp = main.getString("temp");

            String humidity = main.getString("humidity");

            String feel_like = main.getString("feels_like");

            String visibility = jsonObject.getString("visibility");

            nameIcon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");

            Log.i("Name Icon",nameIcon);

            Long time = jsonObject.getLong("dt");

            String sTime = new SimpleDateFormat("dd-M-yyyy", Locale.ENGLISH)
                    .format(new Date(time * 1000));

            txtTime.setText(sTime);

            txtCity.setText("Palhoça");

            txtValue.setText(temp + "°");

            txtValueVision.setText(visibility + " Metros");

            txtValueHumidity.setText(humidity + "%");

            txtValueFeelLike.setText(feel_like + "°");

            DownloadImage downloadImage = new DownloadImage();

            String urlIcon = " https://openweathermap.org/img/wn/"+ nameIcon +"@2x.png";

            Bitmap bitmap = downloadImage.execute(urlIcon).get();

            imgIcon.setImageBitmap(bitmap);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Cidade não encontrada", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.edt_input);

        txtCity = findViewById(R.id.txtCity);

        txtTime = findViewById(R.id.txtTime);

        txtValue = findViewById(R.id.txtValue);

        txtValueFeelLike = findViewById(R.id.txtValueFeelLike);

        txtValueHumidity = findViewById(R.id.txtValueHumidity);

        txtValueVision = findViewById(R.id.txtValueVision);

        imgIcon = findViewById(R.id.imgIcon);

        btnLoading = findViewById(R.id.btnLoading);

        btnLocal = findViewById(R.id.btnLocal);

        relativeLayout = findViewById(R.id.rlWeather);

        title2 = findViewById(R.id.title2);

        title = findViewById(R.id.title);

        rlMain2 = findViewById(R.id.rlMain2);

        relativeLayoutMain = findViewById(R.id.rlMain_Ac);
    }
}