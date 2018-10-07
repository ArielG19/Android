package com.example.got28.ventas;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.got28.ventas.model.Cliente;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//implementamos las class Dialog
public class MainActivity extends AppCompatActivity implements Dialog_layout.Dialog_layoutListener {
    ImageButton Agragar;
    TextView mostrando;
    ListView lista;
    //creamos nuestras variable para listar
    private List<Cliente> listClient = new ArrayList<Cliente>();
    ArrayAdapter<Cliente> arrayAdapterCliente;
    //agregamos firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //inicializamos nuestro btn que agregar
        Agragar = (ImageButton) findViewById(R.id.btnAdd);
        lista = (ListView) findViewById(R.id.listaClientes);
        //mostrando = (TextView)findViewById(R.id.prueba);
        //este metodo tiene q ir como principal
        inicializarFirebase();
        //aqui van los demas metodos.
        listarDatos();
        //agregamos el evento click que llamara nuestro dialog
        Agragar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openDialog();
            }
        });
    }

    private void listarDatos() {
        databaseReference.child("Cliente").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                listClient.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Cliente cliente = objSnapshot.getValue(Cliente.class);
                    listClient.add(cliente);
                    //llenamos nuestro adapter
                    arrayAdapterCliente = new ArrayAdapter<Cliente>(MainActivity.this, android.R.layout.simple_list_item_1,listClient);
                    //arrayAdapterCliente = new ArrayAdapter<Cliente>(MainActivity.this, android.R.layout.simple_list_item_2,listClient);
                    lista.setAdapter(arrayAdapterCliente);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError dataEroor){

            }

        });
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
    //despues de tener nuestro metodos agregamos nuestro layout_dialog que contendra el form
    private void openDialog() {
        Dialog_layout dialog_layout = new Dialog_layout();
        dialog_layout.show(getSupportFragmentManager(),"Modal");
    }

    @Override
    public void applyTexts(String nombre, String telefono) {
        //creamos nuestra instacia de la bd para almacenar
        Cliente cliente = new Cliente();
        cliente.setUid(UUID.randomUUID().toString());
        cliente.setNombre(nombre);
        cliente.setTelefono(telefono);
        //en nuestra bd se creara un nodo llamado cliente y tendra compo principal el id y
        //  lo agregamos lo que contenga srtValue(cliente)
        databaseReference.child("Cliente").child(cliente.getUid()).setValue(cliente);
        //mostrando.setText(telefono);
        Toast.makeText(getApplicationContext(),"Guardado",Toast.LENGTH_LONG).show();

    }

}
