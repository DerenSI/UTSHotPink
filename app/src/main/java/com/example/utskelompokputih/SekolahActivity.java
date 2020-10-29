package com.example.utskelompokputih;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class SekolahActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextNamaSekolah;

    private Button buttonSekolahAdd;
    private Button buttonSiswaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sekolah);

        //Inisialisasi dari View
        editTextNamaSekolah = (EditText) findViewById((R.id.editTextNamaSekolah));

        buttonSekolahAdd = (Button) findViewById(R.id.buttonSekolahAdd);
        buttonSiswaView = (Button) findViewById(R.id.buttonSiswaView);

        //Setting listeners to button
        buttonSekolahAdd.setOnClickListener(this);
        buttonSiswaView.setOnClickListener(this);
    }


    //Dibawah ini merupakan perintah untuk Menambahkan Pegawai (CREATE)
    private void addSekolah(){

        final String namaSekolah = editTextNamaSekolah.getText().toString().trim();

        class AddSekolah extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SekolahActivity.this,"Menambahkan...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(SekolahActivity.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(konfigurasi.KEY_SEKOLAH_NAMA,namaSekolah);
                System.out.print(params);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(konfigurasi.URL_ADD_SEKOLAH, params);
                return res;
            }
        }

        AddSekolah addSekolah = new AddSekolah();
        addSekolah.execute();
    }

    @Override
    public void onClick(View v) {
        if(v == buttonSekolahAdd){
            addSekolah();
        }

        if(v == buttonSiswaView){
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}
