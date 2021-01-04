package com.example.meinuniverwalter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import java.util.ArrayList;


public class UntermenueView extends AppCompatActivity {


    TextView textView;
    SharedPreferences sharedPreferences;
    ArrayList<Untermenue> defaultlistView;
    ListView listView;
    public static final String mypreferenceUntermenue = "mypreff";
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_einzelne_faecher);
        sharedPreferences = getSharedPreferences(mypreferenceUntermenue,
                Context.MODE_PRIVATE);

        textView = findViewById(R.id.textView);
        listView = findViewById(R.id.untermenueListView);

        defaultlistView = new ArrayList<>();


        final String uebergabe = getIntent().getStringExtra("pos");



        initializeListView(uebergabe);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(UntermenueView.this, PDFHandler.class);

                i.putExtra("fach", uebergabe);
                i.putExtra("unterMenü",  listView.getItemAtPosition(position).toString());
                startActivity(i);
            }
        });


    }



    public void initializeListView(String fach) {
        Log.i("test", sharedPreferences.getAll().toString());

        if (!(sharedPreferences.contains(fach))) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            defaultlistView.add(new Untermenue("Vorlesung", fach));
            defaultlistView.add(new Untermenue("Übung", fach));
            defaultlistView.add(new Untermenue("Latex", fach));
            defaultlistView.add(new Untermenue("Sonstiges", fach));
            String a = gson.toJson(defaultlistView);
            editor.putString(fach, a);
            editor.apply();
            initializeListView(fach);
        } else {

            ArrayList<String> ausgabeListe = new ArrayList<>();


            String a = sharedPreferences.getString(fach, null);
            Log.i("GsonString entpacken", "Ich bin in else" + a);

            Untermenue[] ff = gson.fromJson(a, Untermenue[].class);

            for (int i = 0; i < ff.length; i++) {
                ausgabeListe.add(ff[i].getName());

            }
            Log.i("ViewTest", String.valueOf(ausgabeListe));
            listView.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, ausgabeListe));
            textView.setText(fach);

        }


    }
}
