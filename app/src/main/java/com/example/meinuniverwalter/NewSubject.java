package com.example.meinuniverwalter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class NewSubject extends AppCompatDialogFragment {

    private EditText fach;
    public String stringFach;
    private NewSubjectListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_new_subject,null);

        builder.setView(view)
                .setTitle("Erstelle neues Fach")
                .setNegativeButton("Zurück", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stringFach = fach.getText().toString();
                        listener.applyTexts(stringFach);

                    }
                });

        fach = view.findViewById(R.id.textView2);

        return builder.create();

    }
    public interface NewSubjectListener{
        void applyTexts(String fach);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (NewSubjectListener) context;
        }catch (Exception e){
            throw new ClassCastException(context.toString() + "must implement NewSubjectListender");
        }
    }
}
