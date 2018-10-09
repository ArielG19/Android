package com.example.got28.ventas;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
        ListView lista;
        TextView probando;

        //creamos nuestras variable para listar
        private List<Cliente> listClient = new ArrayList<Cliente>();
        ArrayAdapter<Cliente> arrayAdapterCliente;

        //agregamos firebase
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;

        //cremos otra variable de tipo Cliente a nivel de la clase para editar
        Cliente clienteSeleccionado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            //inicializamos nuestro btn que agregar
            Agragar = (ImageButton) findViewById(R.id.btnAdd);
            lista = (ListView) findViewById(R.id.listaClientes);
            //probando = (TextView) findViewById(R.id.listadeclientes);

            //este metodo tiene q ir como principal
            inicializarFirebase();

            //aqui van los demas metodos.
            listarDatos();

            lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    clienteSeleccionado = (Cliente) parent.getItemAtPosition(position);
                    //probando.setText(clienteSeleccionado.getNombre());
                    openDialogEdit(clienteSeleccionado);
                }
            });

            //agregamos header al listview
            View headerView = getLayoutInflater().inflate(R.layout.header_layout, null);
            lista.addHeaderView(headerView);

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

    private void openDialogEdit(final Cliente clienteSeleccionado) {
        final Dialog alert;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_layout, null);//asignas el xml que deseas llamar
        builder.setView(dialogView);

        final EditText editNombre = dialogView.findViewById(R.id.edit_nombre);
        final EditText editNumero = dialogView.findViewById(R.id.edit_telefono);

        //ya que extraigas el valor de tu lista, solo con el setText se lo asignas a los EditText
        editNombre.setText(clienteSeleccionado.getNombre());//nombre de tu lista
        editNumero.setText(clienteSeleccionado.getTelefono());//numero de tu lista

        //bontones para guardar o cancelar
        final Button buttonAcept = dialogView.findViewById(R.id.btnAceptar);
        final Button buttonDimiss = dialogView.findViewById(R.id.btnCancelar);

        alert =builder.show();//con esto mustras el dialogo en pantalla

        buttonAcept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creamos nuestra instacia de la bd para almacenar
                //trim sirve para omitir los espacios en blanco
                Cliente cliente = new Cliente();
                cliente.setUid(clienteSeleccionado.getUid());
                cliente.setNombre(editNombre.getText().toString().trim());
                cliente.setTelefono(editNumero.getText().toString().trim());
                databaseReference.child("Cliente").child(cliente.getUid()).setValue(cliente);

                //pintamos un toast
                Toast.makeText(getApplicationContext(),"Actualizado",Toast.LENGTH_LONG).show();
                alert.dismiss();
            }
        });
        buttonDimiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //accion para cancelar
                alert.dismiss();
            }
        });

        alert.setCancelable(false);//indica si se puede cancelar con el boton fisico de atras o al darle click fuera del dialogo
        alert.show();

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

        //pintamos un toast
        Toast.makeText(getApplicationContext(),"Guardado",Toast.LENGTH_LONG).show();
    }


}
