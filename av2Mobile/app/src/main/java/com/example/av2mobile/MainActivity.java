package com.example.av2mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public Button btBRLtoUSD, btUSDtoBRL;
    public TextView txtConsulta;
    public EditText inputVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btBRLtoUSD = (Button) findViewById(R.id.btBRLtoUSD);
        btUSDtoBRL = (Button) findViewById(R.id.btUSDtoBRL);
        txtConsulta = (TextView) findViewById(R.id.txtConsulta);
        inputVal = (EditText) findViewById(R.id.inputVal);

        //REAL PARA DOLAR
        btBRLtoUSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(inputVal.getText().toString())){
                    MyTask task = new MyTask();
                    String urlApi = "https://economia.awesomeapi.com.br/last/BRL-USD";
                    task.execute(urlApi);
                }
            }
        });

        //DOLAR PARA REAL
        btUSDtoBRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(inputVal.getText().toString())){
                    MyTask task = new MyTask();
                    String urlApi = "https://economia.awesomeapi.com.br/last/USD-BRL";
                    task.execute(urlApi);
                }
            }
        });

    }

    class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputstreamReader = null;
            BufferedReader reader = null;
            StringBuffer buffer;

            try {
                URL url = new URL(stringUrl);

                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

                inputStream = conexao.getInputStream();

                inputstreamReader = new InputStreamReader(inputStream);

                reader = new BufferedReader(inputstreamReader);

                buffer = new StringBuffer();

                String linha="";

                while ((linha = reader.readLine()) != null) {
                    buffer.append(linha);
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return buffer.toString();
        }
        @Override
        protected void onPostExecute (String s){
            super.onPostExecute(s);

            String cotacao = "";

            try{
                JSONObject jsonObject = new JSONObject(s);
                cotacao = jsonObject.getJSONObject("BRLUSD").getString("high").toString();

            }catch (JSONException e){
                Toast.makeText(MainActivity.this, "erro", Toast.LENGTH_SHORT).show();
            }

            txtConsulta.setText(s + "\n-> "+cotacao);
        }
    }
}

