package com.example.meinuniverwalter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import java.io.File;
import java.util.ArrayList;


import static com.example.meinuniverwalter.MainActivity.mypreferenceFach;


public class ViewFaecher extends AppCompatActivity implements NewSubject.NewSubjectListener {
    public SharedPreferences sharedPreferences;


    ListView listView;
    Button button;
    int position;
    View view;
    PDFHandler pdfHandler;
    String name;
    String neuGson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_faecher);
        listView = findViewById(R.id.listView);
        button = findViewById(R.id.button_newF);
        sharedPreferences = getSharedPreferences(mypreferenceFach,
                Context.MODE_PRIVATE);


        registerForContextMenu(listView);
        initializeListView1();
        pdfHandler = new PDFHandler();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ViewFaecher.this, UntermenueView.class);
                String übergabe = parent.getItemAtPosition(position).toString();
                i.putExtra("pos", übergabe);
                startActivity(i);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();


                Toast.makeText(getApplicationContext(), "Fach wurde hinzugefügt!", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        position = info.position;
        view = v;
        menu.setHeaderTitle(listView.getItemAtPosition(position).toString());
        getMenuInflater().inflate(R.menu.optionforsubjects, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionMarkThisAs:

                return true;
            case R.id.optionDelete:
                askUserForDelete(view, position);
                return true;
            case R.id.optionAktuell:
                int aktuell = 1;
                changeStatus(listView.getItemAtPosition(position).toString(), aktuell);

                return true;
            case R.id.optionBestanden:
                int bestanden = 3;
                changeStatus(listView.getItemAtPosition(position).toString(), bestanden);
                return true;
            case R.id.optionNichtAktuell:
                int nichtAktuell = 2;
                changeStatus(listView.getItemAtPosition(position).toString(), nichtAktuell);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }


    public void askUserForDelete(View view, int position) {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Datei wirklich löschen?");

        ad.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = listView.getItemAtPosition(position).toString();

                deleteFach(MainActivity.saveDir.toString() + "/" + name);

            }

        });

        ad.setNeutralButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        ad.show();
    }


    public void deleteFach(String path) {
        File file = new File(path);
        deleteFile(file);
    }

    public void deleteFile(File file) {
        String[] aa = file.list();
        for (int i = 0; i < aa.length; i++) {
            File fileUntermenue = new File(file + "/" + aa[i]);
            String[] bb = fileUntermenue.list();
            for (int j = 0; j < bb.length; j++) {
                File filePDF = new File(fileUntermenue + "/" + bb[j]);
                filePDF.delete();
            }

            fileUntermenue.delete();
        }

        file.delete();

        String neuerString = sharedPreferences.getString("FächerGson", null);
        Gson gson = new Gson();
        Fach[] ff = gson.fromJson(neuerString, Fach[].class);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ArrayList<Fach> neuListe = new ArrayList<>();
        for (int i = 0; i < ff.length; i++) {
            if (!(ff[i].getName().equals(name))) {
                neuListe.add(ff[i]);
            }
        }
        neuGson = gson.toJson(neuListe);
        editor.putString("FächerGson", neuGson);
        editor.apply();

        initializeListView1();
        Toast.makeText(getApplicationContext(), "Datei wurde gelöscht.", Toast.LENGTH_LONG).show();

    }


    //openDialog und applyTexts erzeugen ein Eingabefeld sobald ein neues Fach erstellt werden soll

    public void openDialog() {
        NewSubject newSubject = new NewSubject();
        newSubject.show(getSupportFragmentManager(), "example");
    }

    @Override
    public void applyTexts(String fach) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Fach newFach = new Fach(fach, 1);
        ArrayList<Fach> aa = new ArrayList<>();
        if (sharedPreferences.contains("FächerGson")) {
            String neuerString = sharedPreferences.getString("FächerGson", null);
            Fach[] ff = gson.fromJson(neuerString, Fach[].class);
            for (int i = 0; i < ff.length; i++) {
                aa.add(new Fach(ff[i].getName(), ff[i].getStatus()));
            }
            aa.add(0, newFach);
            neuGson = gson.toJson(aa);
            editor.putString("FächerGson", neuGson);
            editor.apply();
            initializeListView1();

        } else {
            aa.add(newFach);
            neuGson = gson.toJson(aa);
            editor.putString("FächerGson", neuGson);
            editor.apply();
            initializeListView1();

        }
    }


    public void initializeListView1() {
        Gson gson = new Gson();
        View v;
        ArrayList<String> viewFaecher = new ArrayList<>();
        if (sharedPreferences.contains("FächerGson")) {
            String neuerString = sharedPreferences.getString("FächerGson", null);
            Fach[] ff = gson.fromJson(neuerString, Fach[].class);
            for (int i = 0; i < ff.length; i++) {
                viewFaecher.add(ff[i].getName());


                int s = ff[i].getStatus();
            }

            listView.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, viewFaecher));

        }

    }

    public void changeStatus(String name, int status) {
        Gson gson = new Gson();
        String neuGson;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ArrayList<Fach> aa = new ArrayList<>();

        String neuerString = sharedPreferences.getString("FächerGson", null);
        Fach[] ff = gson.fromJson(neuerString, Fach[].class);
        for (int i = 0; i < ff.length; i++) {
            if (ff[i].getName().equals(name)) {
                ff[i].setStatus(status);
            }
            aa.add(new Fach(ff[i].getName(), ff[i].getStatus()));
        }
        aa.sort(Fach::compareTo);

        neuGson = gson.toJson(aa);
        editor.putString("FächerGson", neuGson);
        editor.apply();
        initializeListView1();

    }


}



