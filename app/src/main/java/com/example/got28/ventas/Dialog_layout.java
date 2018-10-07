package com.example.got28.ventas;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Got28 on 06/10/2018.
 * Agregamos superclass AppCompatDialogFragment
 */

public class Dialog_layout extends AppCompatDialogFragment {
    //agremamos los editText
    private EditText editNombre, editTelefono;
    //para el ultimo metodo
    private Dialog_layoutListener listener;
    //ctr + o para agregar el siguiente metodo
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);

        //agregamos los btn guardar o cancelar
        builder.setView(view)
                .setTitle("Agregar Cliente")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //aqui almacenamos los datos en la siguiente variable
                        String nombre = editNombre.getText().toString();
                        String telefono = editTelefono.getText().toString();
                        //esto es cuando ya implemtantamos en el mainactivity
                        listener.applyTexts(nombre,telefono);
                    }
                });
        //inicializamos nuestros edittext de la siguiente manera para obtener los datos
        editNombre = view.findViewById(R.id.nombreCliente);
        editTelefono = view.findViewById(R.id.telefonoCliente);

        return builder.create();

    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            listener = (Dialog_layoutListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString()+ "must implement");
        }
    }
    //metodo para mandar los datos al mainActivitity class
    public  interface Dialog_layoutListener{
        void applyTexts(String nombre, String telefono);
    }

}
