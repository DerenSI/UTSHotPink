package com.example.utskelompokputih;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // View Definition
    private EditText editTextPaketIdHidden;
    private EditText editTextNama;
    private EditText editTextNoInduk;
    private EditText editTextJenisKelamin;
    private EditText editTextTempatLahir;
    private EditText editTextTanggalLahir;
    private EditText editTextSekolahIdHidden;
    private EditText editTextAlamat;
    private EditText editTextNamaWali;
    private EditText editTextTelp;
    private EditText editTextFoto;

    private EditText editTextNamaSearch;

    private Button buttonFind;
    private Button buttonAdd;
    private Button buttonView;
    private Button buttonAddSekolah;

    private Spinner spinnerPaketId;
    private Spinner spinnerSekolahId;

    private DatePicker datePickerTanggalLahir;

    private RadioGroup radioGroupJenisKelamin;
    private RadioButton radioButtonJenisKelamin;

    // Setup Spinner Paket
    ProgressDialog paketDialog;
    JSONArray JsonArrayPaket = null;
    List<String> valueIdPaket = new ArrayList<String>();
    List<String> valueNamaPaket = new ArrayList<String>();

    // Setup Spinner Sekolah
    ProgressDialog sekolahDialog;
    JSONArray JsonArraySekolah = null;
    List<String> valueIdSekolah = new ArrayList<String>();
    List<String> valueNamaSekolah = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialization of View
        editTextPaketIdHidden = (EditText) findViewById(R.id.editTextPaketIdHidden);
        editTextNama = (EditText) findViewById(R.id.editTextNama);
        editTextNoInduk = (EditText) findViewById(R.id.editTextNoInduk);
        editTextJenisKelamin = (EditText) findViewById(R.id.editTextJenisKelamin);
        editTextTempatLahir = (EditText) findViewById(R.id.editTextTempatLahir);
        editTextTanggalLahir = (EditText) findViewById(R.id.editTextTanggalLahir);
        editTextSekolahIdHidden = (EditText) findViewById(R.id.editTextSekolahIdHidden);
        editTextAlamat = (EditText) findViewById(R.id.editTextAlamat);
        editTextNamaWali = (EditText) findViewById(R.id.editTextNamaWali);
        editTextTelp = (EditText) findViewById(R.id.editTextTelp);
        editTextFoto = (EditText) findViewById(R.id.editTextFoto);

        editTextNamaSearch = (EditText) findViewById(R.id.editTextNamaSearch);

        spinnerPaketId = (Spinner) findViewById(R.id.spinnerPaketId);
        spinnerSekolahId = (Spinner) findViewById(R.id.spinnerSekolahId);

        datePickerTanggalLahir = (DatePicker) findViewById(R.id.datePickerTanggalLahir);
        radioGroupJenisKelamin = (RadioGroup) findViewById((R.id.radioGroupJenisKelamin));

        buttonFind = (Button) findViewById(R.id.buttonFind);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonView = (Button) findViewById(R.id.buttonView);
        buttonAddSekolah = (Button) findViewById(R.id.buttonAddSekolah);

        // Setting up Listener
        buttonFind.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        buttonView.setOnClickListener(this);
        buttonAddSekolah.setOnClickListener(this);

        LoadDataPaket(null);
        LoadDataSekolah(null);
    }

    public void LoadDataPaket(View v){
        new getDataPaket().execute();
    }
    public void LoadDataSekolah(View v){
        new getDataSekolah().execute();
    }

    private class getDataPaket extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            paketDialog = new ProgressDialog(MainActivity.this);
            paketDialog.setMessage("Mohon Tunggu...");
            paketDialog.setCancelable(false);
            paketDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            //Membuat Service "ServiceHandler"
            ServiceHandler sh = new ServiceHandler();

            // Memanggil URL untuk mendapatkan respon data
            String jsonStr = sh.makeServiceCall(konfigurasi.URL_SPINNER_PAKET, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Mendapatkan data Array JSON
                    JsonArrayPaket = jsonObj.getJSONArray("result");

                    ArrayList<ModelPaket> listDataPaket = new ArrayList<ModelPaket>();
                    listDataPaket.clear();

                    //Melakukan perulangan untuk memecah data
                    for (int i = 0; i < JsonArrayPaket.length(); i++) {
                        JSONObject obj = JsonArrayPaket.getJSONObject(i);

                        ModelPaket modelPaket = new ModelPaket();
                        modelPaket.setIdPaket(obj.getString("id"));
                        modelPaket.setNamaPaket(obj.getString("nama"));
                        listDataPaket.add(modelPaket);
                    }

                    valueIdPaket = new ArrayList<String>();
                    valueNamaPaket = new ArrayList<String>();

                    for (int i = 0; i < listDataPaket.size(); i++) {
                        valueIdPaket.add(listDataPaket.get(i).getIdPaket());
                        valueNamaPaket.add(listDataPaket.get(i).getNamaPaket());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (paketDialog.isShowing())
                paketDialog.dismiss();

            // Membuat adapter untuk spinner
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_spinner_item, valueNamaPaket);

            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //Mengaitkan adapter spinner dengan spinner yang ada di layout
            spinnerPaketId.setAdapter(spinnerAdapter);
            spinnerPaketId.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String idPaket = valueIdPaket.get(position);
                    String namaPaket = valueNamaPaket.get(position);
                    editTextPaketIdHidden.setText(idPaket);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

        }
    }

    private class getDataSekolah extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sekolahDialog = new ProgressDialog(MainActivity.this);
            sekolahDialog.setMessage("Mohon Tunggu...");
            sekolahDialog.setCancelable(false);
            sekolahDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            //Membuat Service "ServiceHandler"
            ServiceHandler sh = new ServiceHandler();

            // Memanggil URL untuk mendapatkan respon data
            String jsonStr = sh.makeServiceCall(konfigurasi.URL_SPINNER_SEKOLAH, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Mendapatkan data Array JSON
                    JsonArraySekolah = jsonObj.getJSONArray("result");

                    ArrayList<ModelSekolah> listDataSekolah = new ArrayList<ModelSekolah>();
                    listDataSekolah.clear();

                    //Melakukan perulangan untuk memecah data
                    for (int i = 0; i < JsonArraySekolah.length(); i++) {
                        JSONObject obj = JsonArraySekolah.getJSONObject(i);

                        ModelSekolah modelSekolah = new ModelSekolah();
                        modelSekolah.setIdSekolah(obj.getString("id"));
                        modelSekolah.setNamaSekolah(obj.getString("nama"));
                        listDataSekolah.add(modelSekolah);
                    }

                    valueIdSekolah = new ArrayList<String>();
                    valueNamaSekolah = new ArrayList<String>();

                    for (int i = 0; i < listDataSekolah.size(); i++) {
                        valueIdSekolah.add(listDataSekolah.get(i).getIdSekolah());
                        valueNamaSekolah.add(listDataSekolah.get(i).getNamaSekolah());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (sekolahDialog.isShowing())
                sekolahDialog.dismiss();

            // Membuat adapter untuk spinner
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_spinner_item, valueNamaSekolah);

            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //Mengaitkan adapter spinner dengan spinner yang ada di layout
            spinnerSekolahId.setAdapter(spinnerAdapter);
            spinnerSekolahId.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String idSekolah = valueIdSekolah.get(position);
                    String namaSekolah = valueNamaSekolah.get(position);
                    editTextSekolahIdHidden.setText(idSekolah);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

        }
    }

    //Dibawah ini merupakan perintah untuk Menambahkan Pegawai (CREATE)
    private void addSiswa(){

        final String PaketIdHidden = editTextPaketIdHidden.getText().toString().trim();
        final String NoInduk = editTextNoInduk.getText().toString().trim();
        final String Nama = editTextNama.getText().toString().trim();
//        final String JenisKelamin = editTextJenisKelamin.getText().toString().trim();
        final String TempatLahir = editTextTempatLahir.getText().toString().trim();
//        final String TanggalLahir = editTextTanggalLahir.getText().toString().trim();
        final String SekolahIdHidden = editTextSekolahIdHidden.getText().toString().trim();
        final String Alamat = editTextAlamat.getText().toString().trim();
        final String NamaWali = editTextNamaWali.getText().toString().trim();
        final String Telp = editTextTelp.getText().toString().trim();
        final String Foto = editTextFoto.getText().toString().trim();

        int day = datePickerTanggalLahir.getDayOfMonth();
        String dayString = String.valueOf(day);
        int month = datePickerTanggalLahir.getMonth() + 1;
        String monthString = String.valueOf(month);
        int year = datePickerTanggalLahir.getYear();
        String yearString = String.valueOf(year);
        final String TanggalLahir = yearString + '-' + monthString + '-' + dayString;

        int selectedJK = radioGroupJenisKelamin.getCheckedRadioButtonId();
        radioButtonJenisKelamin = (RadioButton) findViewById(selectedJK);
        String JK = "L";
        if (radioButtonJenisKelamin.getText().equals("Laki-Laki")) {
            JK = "L";
        } else if (radioButtonJenisKelamin.getText().equals("Perempuan")) {
            JK = "P";
        }
        final String JenisKelamin = JK.toString().trim();

        class AddSiswa extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Menambahkan...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(konfigurasi.KEY_SISWA_PAKETID,PaketIdHidden);
                params.put(konfigurasi.KEY_SISWA_NOINDUK,NoInduk);
                params.put(konfigurasi.KEY_SISWA_NAMA,Nama);
                params.put(konfigurasi.KEY_SISWA_JENISKELAMIN,JenisKelamin);
                params.put(konfigurasi.KEY_SISWA_TEMPATLAHIR,TempatLahir);
                params.put(konfigurasi.KEY_SISWA_TANGGALLAHIR,TanggalLahir);
                params.put(konfigurasi.KEY_SISWA_SEKOLAHID,SekolahIdHidden);
                params.put(konfigurasi.KEY_SISWA_ALAMAT,Alamat);
                params.put(konfigurasi.KEY_SISWA_NAMAWALI,NamaWali);
                params.put(konfigurasi.KEY_SISWA_TELP,Telp);
                params.put(konfigurasi.KEY_SISWA_FOTO,Foto);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(konfigurasi.URL_ADD_SISWA, params);
                return res;
            }
        }

        AddSiswa addSiswa = new AddSiswa();
        addSiswa.execute();
    }

    @Override
    public void onClick(View v) {
        if(v == buttonAdd){
            addSiswa();
        }

        if(v == buttonView){
            startActivity(new Intent(this,TampilSemuaSiswa.class));
        }

        if(v == buttonFind){
            Intent intent = new Intent(this, TampilSebagianSiswa.class);
            Bundle b = new Bundle();
            final String keyword = editTextNamaSearch.getText().toString().trim();
            b.putString("key", keyword);
            intent.putExtras(b);
            startActivity(intent);
            finish();
        }

        if(v == buttonAddSekolah){
            startActivity(new Intent(MainActivity.this,SekolahActivity.class));
        }
    }
}