package com.example.utskelompokputih;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.os.strictmode.UntaggedSocketViolation;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class TampilSiswa extends AppCompatActivity implements View.OnClickListener {
    // View Definition
    private EditText editTextId;
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

    private Button buttonUpdate;
    private Button buttonDelete;
    private Button buttonKembali;
    private Button buttonTambah;
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

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_siswa);

        Intent intent = getIntent();

        id = intent.getStringExtra(konfigurasi.SISWA_ID);

        // Initialization of View
        editTextId = (EditText) findViewById(R.id.editTextId);
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

        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        buttonKembali = (Button) findViewById(R.id.buttonKembali);
        buttonTambah = (Button) findViewById(R.id.buttonTambah);
        buttonAddSekolah = (Button) findViewById(R.id.buttonAddSekolah);

        spinnerPaketId = (Spinner) findViewById(R.id.spinnerPaketId);
        spinnerSekolahId = (Spinner) findViewById(R.id.spinnerSekolahId);

        datePickerTanggalLahir = (DatePicker) findViewById(R.id.datePickerTanggalLahir);
        radioGroupJenisKelamin = (RadioGroup) findViewById((R.id.radioGroupJenisKelamin));

        LoadDataPaket(null);
        LoadDataSekolah(null);

        buttonUpdate.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        buttonKembali.setOnClickListener(this);
        buttonTambah.setOnClickListener(this);
        buttonAddSekolah.setOnClickListener(this);

        editTextId.setText(id);

        getSiswa();
    }

    // Mengisi Spinner dengan value dari Database
//    private int getIndex(Spinner spinner, String myString){
//        System.out.println("Params ID: " + myString);
//        for (int i=0;i<spinner.getCount();i++){
//            System.out.println("Item:" + spinner.getItemIdAtPosition(i));
//            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
//                return i;
//            }
//        }
//        return 0;
//    }

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
            paketDialog = new ProgressDialog(TampilSiswa.this);
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
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(TampilSiswa.this,
                    android.R.layout.simple_spinner_item, valueNamaPaket);

            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //Mengaitkan adapter spinner dengan spinner yang ada di layout
            spinnerPaketId.setAdapter(spinnerAdapter);
            spinnerPaketId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            sekolahDialog = new ProgressDialog(TampilSiswa.this);
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
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(TampilSiswa.this,
                    android.R.layout.simple_spinner_item, valueNamaSekolah);

            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //Mengaitkan adapter spinner dengan spinner yang ada di layout
            spinnerSekolahId.setAdapter(spinnerAdapter);
            spinnerSekolahId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void getSiswa(){
        class GetSiswa extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilSiswa.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showSiswa(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_SISWA,id);
                System.out.println(s);
                return s;
            }
        }
        GetSiswa getSiswa = new GetSiswa();
        getSiswa.execute();
    }

    private void showSiswa(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String PaketIdHidden = c.getString(konfigurasi.TAG_SISWA_PAKETID);
            String NoInduk = c.getString(konfigurasi.TAG_SISWA_NOINDUK);
            String NamaSiswa = c.getString(konfigurasi.TAG_SISWA_NAMA);
            String JenisKelamin = c.getString(konfigurasi.TAG_SISWA_JENISKELAMIN);
            String TempatLahir = c.getString(konfigurasi.TAG_SISWA_TEMPATLAHIR);
            String TanggalLahir = c.getString(konfigurasi.TAG_SISWA_TANGGALLAHIR);
            String SekolahIdHidden = c.getString(konfigurasi.TAG_SISWA_SEKOLAHID);
            String Alamat = c.getString(konfigurasi.TAG_SISWA_ALAMAT);
            String NamaWali = c.getString(konfigurasi.TAG_SISWA_NAMAWALI);
            String Telp = c.getString(konfigurasi.TAG_SISWA_TELP);
            String Foto = c.getString(konfigurasi.TAG_SISWA_FOTO);

            Integer year = Integer.parseInt(TanggalLahir.substring(0,4));
            Integer month = Integer.parseInt(TanggalLahir.substring(5,7)) - 1;
            Integer day = Integer.parseInt(TanggalLahir.substring(8,10));

            datePickerTanggalLahir.updateDate(
                    (int) year,
                    (int) month,
                    (int) day
            );

            if (JenisKelamin.equals("L")) {
                radioGroupJenisKelamin.check(R.id.radioL);
            } else if (JenisKelamin.equals("P")) {
                radioGroupJenisKelamin.check(R.id.radioP);
            }

            editTextPaketIdHidden.setText(PaketIdHidden);
            editTextNama.setText(NoInduk);
            editTextNoInduk.setText(NamaSiswa);
            editTextJenisKelamin.setText(JenisKelamin);
            editTextTempatLahir.setText(TempatLahir);
            editTextTanggalLahir.setText(TanggalLahir);
            editTextSekolahIdHidden.setText(SekolahIdHidden);
            editTextAlamat.setText(Alamat);
            editTextNamaWali.setText(NamaWali);
            editTextTelp.setText(Telp);
            editTextFoto.setText(Foto);

            // Auto fill jenis kelamin when load

//            System.out.println("start");
//            spinnerPaketId.setSelection(getIndex(spinnerPaketId, PaketIdHidden));
//            spinnerSekolahId.setSelection(getIndex(spinnerSekolahId, SekolahIdHidden));
//            System.out.println("done");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateSiswa(){
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

        class UpdateSiswa extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilSiswa.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(TampilSiswa.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(konfigurasi.KEY_SISWA_ID,id);
                hashMap.put(konfigurasi.KEY_SISWA_PAKETID,PaketIdHidden);
                hashMap.put(konfigurasi.KEY_SISWA_NOINDUK,NoInduk);
                hashMap.put(konfigurasi.KEY_SISWA_NAMA,Nama);
                hashMap.put(konfigurasi.KEY_SISWA_JENISKELAMIN,JenisKelamin);
                hashMap.put(konfigurasi.KEY_SISWA_TEMPATLAHIR,TempatLahir);
                hashMap.put(konfigurasi.KEY_SISWA_TANGGALLAHIR,TanggalLahir);
                hashMap.put(konfigurasi.KEY_SISWA_SEKOLAHID,SekolahIdHidden);
                hashMap.put(konfigurasi.KEY_SISWA_ALAMAT,Alamat);
                hashMap.put(konfigurasi.KEY_SISWA_NAMAWALI,NamaWali);
                hashMap.put(konfigurasi.KEY_SISWA_TELP,Telp);
                hashMap.put(konfigurasi.KEY_SISWA_FOTO,Foto);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(konfigurasi.URL_UPDATE_SISWA,hashMap);

                return s;
            }
        }

        UpdateSiswa updateSiswa = new UpdateSiswa();
        updateSiswa.execute();
    }

    private void deleteSiswa(){
        class DeleteSiswa extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilSiswa.this, "Updating...", "Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(TampilSiswa.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_DELETE_SISWA, id);
                return s;
            }
        }

        DeleteSiswa deleteSiswa = new DeleteSiswa();
        deleteSiswa.execute();
    }

    private void confirmDeleteSiswa(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Kamu Yakin Ingin Menghapus Siswa ini?");

        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteSiswa();
                        startActivity(new Intent(TampilSiswa.this,TampilSemuaSiswa.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v == buttonUpdate){
            updateSiswa();
        }

        if(v == buttonDelete){
            confirmDeleteSiswa();
        }

        if(v == buttonKembali){
            startActivity(new Intent(TampilSiswa.this,TampilSemuaSiswa.class));
        }

        if(v == buttonTambah){
            startActivity(new Intent(TampilSiswa.this,MainActivity.class));
        }

        if(v == buttonAddSekolah){
            startActivity(new Intent(TampilSiswa.this,SekolahActivity.class));
        }
    }
}
