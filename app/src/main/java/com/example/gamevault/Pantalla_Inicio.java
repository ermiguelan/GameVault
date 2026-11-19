package com.example.gamevault;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.gamevault.room.GameVaultDataBase;
import com.example.gamevault.room.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Pantalla_Inicio extends AppCompatActivity {
    private Button cerrarSesion;
    private TextView txtNombreUsuario;
    private TextView txtCorreoUsuario;
    private FirebaseAuth auth;
    private FirebaseFirestore firestone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_inicio);
        cerrarSesion= findViewById(R.id.btnCerrarSesion);
        auth = FirebaseAuth.getInstance();
        firestone = FirebaseFirestore.getInstance();
        txtNombreUsuario = findViewById(R.id.txtNombreUsuario);
        txtCorreoUsuario = findViewById(R.id.txtCorreoUsuario);

        //Obtengo usuario actual
        String uid = auth.getCurrentUser().getUid();


        //Inicio la base de datos Room
        GameVaultDataBase db = Room.databaseBuilder(
                        getApplicationContext(),
                        GameVaultDataBase.class,
                        "gamevault_db"
                ).allowMainThreadQueries()
                .build();

        //Leo firestone
        firestone.collection("usuarios")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    String nombre =
                            documentSnapshot.getString("nombreUsuario");

                    String correo =
                            documentSnapshot.getString("correo");
                    db.usuarioDao().borrarTodos();
                    Usuario usuario = new Usuario();

                    usuario.setUid(uid);
                    usuario.setNombreUsuario(nombre);
                    usuario.setCorreo(correo);

                    db.usuarioDao().insertar(usuario);

                    txtNombreUsuario.setText(
                            "Usuario: " + usuario.getNombreUsuario());

                    txtCorreoUsuario.setText(
                            "Correo: " + usuario.getCorreo());

                });


        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Pantalla_Inicio.this);
                builder.setTitle("Cerrar sesión");
                builder.setMessage("¿Deseas cerrar sesión?");

                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        auth.signOut();

                        Intent intent = new Intent(
                                Pantalla_Inicio.this,
                                MainActivity.class);

                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("No", null);

                builder.show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}