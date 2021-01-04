package com.example.meinuniverwalter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.Random;


public  class  PDFHandler extends AppCompatActivity implements ChangeNameDialog.ChangeListener {
    ListView listView;
    TextView textView;
    ImageButton buttonAddPDF;

    Intent myFileIntent;
    public boolean answer = false;

    View view;
    int position;


    SharedPreferences sharedPreferences;
    public static final String mypreferencePDF = "myprefff";

    String fach;
    String untermenue;
    ArrayList<String> keyAuswahl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);

        textView = findViewById(R.id.textViewPDF);
        listView = findViewById(R.id.listViewPDF);
        buttonAddPDF = findViewById(R.id.buttonAddPDF);

        sharedPreferences = getSharedPreferences(mypreferencePDF,
                Context.MODE_PRIVATE);


        untermenue = getIntent().getStringExtra("unterMenü");
        fach = getIntent().getStringExtra("fach");
        keyAuswahl = new ArrayList<>();

        initializeListView();


        buttonAddPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                myFileIntent.setType("*/*");
                startActivityForResult(myFileIntent, 10);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(PDFHandler.this, ViewPDF.class);


                i.putExtra("saveDir", getUnterMenuDir().toPath().resolve(listView.getItemAtPosition(position).toString()).toAbsolutePath().toString());

                Log.i("Datei öffenen", "öffne Datei");
                startActivity(i);

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                askUserForDelete(view, position);

                return true;
            }
        });


    }


    public void askUserForDelete(View view, int position) {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Datei wirklich löschen?");

        ad.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePDF(getUnterMenuDir().toPath().resolve(listView.getItemAtPosition(position).toString()).toAbsolutePath().toString());
            }

        });

        ad.setNeutralButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        ad.show();
    }

    public File getUnterMenuDir() {
        File fachDir = new File(MainActivity.saveDir, fach);
        File menuDir = new File(fachDir, untermenue);

        if (!menuDir.exists()) {
            menuDir.mkdirs();
        }
        return menuDir;
    }

    public File[] getAllFiles(File dir) {
        return dir.listFiles();
    }

    public String[] convertFilesToName(File[] files) {
        String[] names = new String[0];
        try {
            names = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                names[i] = files[i].getName();
            }
        } catch (NullPointerException e) {

        }
        return names;
    }


    private static void TODO() {
        throw new RuntimeException("Not Implemented!");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 10:

                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);

                        if (inputStream == null) {
                            throw new RuntimeException("Error in opening file");
                        }

                        String name = Utils.getRandomString(10);


                        if (!name.endsWith(".pdf")) {
                            name += ".pdf";
                        }


                        File menuDir = getUnterMenuDir();

                        File file = new File(menuDir, name);

                        OutputStream outputStream = new FileOutputStream(file);

                        Utils.copyFileUsingStream(inputStream, outputStream);


                        String path = file.getAbsolutePath();
                        initializeListView();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Log.i("saveDir----------------------------", uri.getPath());


                }
        }


    }


    public void initializeListView() {
        ArrayList<String> ausgabeListe = new ArrayList<>(Arrays.asList(convertFilesToName(getAllFiles(getUnterMenuDir()))));


        Log.i("ViewTest", String.valueOf(ausgabeListe));
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, ausgabeListe));
        textView.setText(fach);
    }


    public void deletePDF(String path) {
        File file = new File(path);
        deleteFile(file);
    }

    public void deleteFile(File file) {
        file.delete();
        initializeListView();
        Toast.makeText(getApplicationContext(), "Datei wurde gelöscht.", Toast.LENGTH_LONG).show();

    }


    /**
     * @return
     * @deprecated
     */
    public int keyMaker() {
        int zufallszahl;
        Random random = new Random();
        zufallszahl = random.nextInt(Integer.MAX_VALUE);
        return zufallszahl;

    }

    public void openDialog() {
        NewSubject newSubject = new NewSubject();
        newSubject.show(getSupportFragmentManager(), "example");
    }


    @Override
    public String applyTexts(String fach) {


        return fach;
    }
}
