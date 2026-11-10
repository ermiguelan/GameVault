package com.example.gamevault;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registro extends AppCompatActivity {
    private EditText txtUsuario;
    private EditText txtCorreo;
    private EditText txtContrasenia;
    private EditText txtConfContrasenia;
    private Button btnRegistrarse;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        auth = FirebaseAuth.getInstance();
        txtUsuario= findViewById(R.id.txtUsuario);
        txtCorreo= findViewById(R.id.txtCorreoR);
        txtContrasenia= findViewById(R.id.txtContraseniaR);
        txtConfContrasenia= findViewById(R.id.txtconfContraseña);
        btnRegistrarse= findViewById(R.id.btnRegistrarse);

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario= txtUsuario.getText().toString();
                String correo = txtCorreo.getText().toString();
                String contrasenia = txtContrasenia.getText().toString();
                String confirmar = txtConfContrasenia.getText().toString();

                if(usuario.isEmpty() || correo.isEmpty() || contrasenia.isEmpty() || confirmar.isEmpty()){
                    MyToast("Debes rellenar todos los campos");
                    return;
                }
                if(contrasenia.length() < 6){
                    MyToast("La contraseña debe tener al menos 6 caracteres");
                    return;
                }
                if(!contrasenia.equals(confirmar)){
                    MyToast("Contraseña no coincide");
                    return;
                }
                auth.createUserWithEmailAndPassword(correo, contrasenia).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            MyToast("Te has registrado correctamente");
                            finish();
                        }else{
                            MyToast("Error al registrar usuario");
                        }
                    }
                });
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void MyToast(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }
}