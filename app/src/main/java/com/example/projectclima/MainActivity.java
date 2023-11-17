package com.example.projectclima;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    String key = "2a20d2a71c5a0737f88861d43949d630";



    public class DownloadJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            URL url;

            HttpURLConnection httpURLConnection;

            InputStream inputStream;

            InputStreamReader inputStreamReader;

            String result = "";

            try {

                url = new URL(strings[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                inputStream = httpURLConnection.getInputStream();

                inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data != -1) {

                    result += (char) data;

                    data = inputStreamReader.read();
                }

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            return result;
        }
    }

    public class DownloadIcon extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {

            Bitmap bitmap = null;


            URL url;

            HttpURLConnection httpURLConnection;

            InputStream inputStream;

            try {


                url = new URL(strings[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                inputStream = httpURLConnection.getInputStream();

                bitmap = BitmapFactory.decodeStream(inputStream);



            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return bitmap;
        }
    }

    TextView txtCity,txtTime,txtValueFeelLike,txtValueHumidity,txtVision,txtTemp,title,title2;
    ImageView imageView;
    EditText edt;
    Button btn;
    RelativeLayout rlWeather, rlRoot, rlMain2;

    public void Voltar(View view) {


        edt.setVisibility(View.VISIBLE);
        btn.setVisibility(View.VISIBLE);
        rlMain2.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        rlWeather.setVisibility(View.INVISIBLE);
        title2.setVisibility(View.VISIBLE);
        rlRoot.setBackgroundColor(Color.parseColor("#E6E6E6"));}


    public void Clima(View view) {

        City = edt.getText().toString();

        String url = "https://api.openweathermap.org/data/2.5/weather?q="+ City +"&units=metric&appid="+ key;

        edt.setVisibility(View.INVISIBLE);
        btn.setVisibility(View.INVISIBLE);
        rlMain2.setVisibility(View.INVISIBLE);
        title.setVisibility(View.INVISIBLE);
        rlWeather.setVisibility(View.VISIBLE);
        title2.setVisibility(View.INVISIBLE);
        rlRoot.setBackgroundColor(Color.parseColor("#E6E6E6"));





        DownloadJSON downloadJSON = new DownloadJSON();

        try {
            String result = downloadJSON.execute(url).get();

            JSONObject jsonObject = new JSONObject(result);

            String temp = jsonObject.getJSONObject("main").getString("temp");

            String humidity = jsonObject.getJSONObject("main").getString("humidity");

            String feels_Like = jsonObject.getJSONObject("main").getString("feels_like");

            String visibility = jsonObject.getString("visibility");

            Long time = jsonObject.getLong("dt");

            String sTime = new SimpleDateFormat("dd/M/yyyy", Locale.ENGLISH)
                    .format(new Date(time*1000));

            txtTime.setText(sTime);
            txtCity.setText(City);
            txtVision.setText(visibility+" Metros");
            txtValueFeelLike.setText(feels_Like+"º");
            txtValueHumidity.setText(humidity+"%");
            txtTemp.setText(temp+"º");

            String nameIcon = "10d";

            nameIcon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");

            String urlIcon = "https://openweathermap.org/img/wn/"+ nameIcon +"@2x.png";

            DownloadIcon downloadIcon = new DownloadIcon();

            Bitmap bitmap = downloadIcon.execute(urlIcon).get();

            imageView.setImageBitmap(bitmap);

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCity = findViewById(R.id.txtCity);
        txtTime = findViewById(R.id.txtTime);
        txtValueFeelLike = findViewById(R.id.txtValueFeelLike);
        txtValueHumidity = findViewById(R.id.txtValueHumidity);
        txtVision = findViewById(R.id.txtValorVision);
        txtTemp = findViewById(R.id.txtValue);
        imageView = findViewById(R.id.imgIcon);
        btn = findViewById(R.id.btn);
        edt = findViewById(R.id.edt);
        edt.setText("");
        rlWeather = findViewById(R.id.rlWeather);
        rlRoot = findViewById(R.id.rlRoot);
        rlMain2 = findViewById(R.id.rlMain2);
        title = findViewById(R.id.title);
        title2 = findViewById(R.id.title2);
    }
}