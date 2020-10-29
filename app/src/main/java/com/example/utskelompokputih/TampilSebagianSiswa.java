package com.example.utskelompokputih;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TampilSebagianSiswa extends AppCompatActivity implements ListView.OnItemClickListener {
    private ListView listView;
    private String JSON_STRING;
    private String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_semua_siswa);

        Intent intent = getIntent();
//        keyword = intent.getStringExtra(konfigurasi.KEY_EMP_NAMA);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        getJSON();
    }

    private void showSiswa(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(konfigurasi.TAG_SISWA_ID);
                String nama = jo.getString(konfigurasi.TAG_SISWA_NAMA);

                HashMap<String,String> siswa = new HashMap<>();
                siswa.put(konfigurasi.TAG_SISWA_ID,id);
                siswa.put(konfigurasi.TAG_SISWA_NAMA,nama);
                list.add(siswa);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Gagal Mencari Siswa");
        }

        ListAdapter adapter = new SimpleAdapter(
                TampilSebagianSiswa.this, list, R.layout.list_item,
                new String[]{konfigurasi.TAG_SISWA_ID,konfigurasi.TAG_SISWA_NAMA},
                new int[]{R.id.id, R.id.nama});

        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilSebagianSiswa.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showSiswa();
            }

            @Override
            protected String doInBackground(Void... params) {
                Bundle b = getIntent().getExtras();
                String keyword;
                if (b != null);
                keyword = b.getString("key");

                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_SISWA_BY_KEYWORD,keyword);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, TampilSiswa.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String siswaId = map.get(konfigurasi.TAG_SISWA_ID).toString();
        intent.putExtra(konfigurasi.SISWA_ID,siswaId);
        startActivity(intent);
    }
}
