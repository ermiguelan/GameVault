package com.example.gamevault;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    private TextView txtRegistrate;
    private TextInputEditText txtCorreo;
    private TextInputEditText txtContrasenia;
    private Button btnInicio;
    private Button btnGoogle;
    private static final int RC_SIGN_IN = 100;
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //Inicializamos las variables
        txtCorreo= findViewById(R.id.txtCorreo);
        txtContrasenia= findViewById(R.id.txtContrasenia);
        btnInicio= findViewById(R.id.btnInicio);
        btnGoogle = findViewById(R.id.btnGoogle);
        auth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(
                        GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("815792927605-l9jda4nk05827cj24u1eq5b42ef06lcg.apps.googleusercontent.com")
                        .requestEmail()
                        .build();

        googleSignInClient =
                GoogleSignIn.getClient(this, gso);

        txtRegistrate= (TextView) findViewById(R.id.txtRegistrate);
        //Si el usuario esta conectado que lo lleve a la pantalla principal directamente
        if(auth.getCurrentUser() != null){
            InicioPantalla();
        }
        //Me lleva a la pantalla de registrarse
        txtRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, Registro.class);
                startActivity(intent);
            }
        });
        //inicio de sesion con google
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent =
                        googleSignInClient.getSignInIntent();

                startActivityForResult(
                        signInIntent,
                        RC_SIGN_IN
                );
            }
        });

        //inicio sesión con corrreo y contraseña
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo= txtCorreo.getText().toString();
                String contrasenia= txtContrasenia.getText().toString();
                if(correo.isEmpty()){
                    txtCorreo.setError("Rellena este campo");
                    return;
                }
                if(contrasenia.isEmpty()){
                    txtContrasenia.setError("Rellena este campo");
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                    txtCorreo.setError("Correo electrónico no válido");
                    return;
                }
                auth.signInWithEmailAndPassword(correo, contrasenia).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            MyToast("Inicio de sesión correcto");
                            InicioPantalla();

                        }else{

                            MyToast("Correo o contraseña incorrectos");

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

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                GoogleSignInAccount account =
                        task.getResult(ApiException.class);

                firebaseAuthWithGoogle(
                        account.getIdToken()
                );

            } catch (ApiException e) {

                MyToast("Error Google");

            }
        }
    }
    private void InicioPantalla() {
        Intent intent = new Intent(
                MainActivity.this,
                Pantalla_Inicio.class);

        startActivity(intent);
        finish();
    }
    private void firebaseAuthWithGoogle(String idToken){

        AuthCredential credential =
                GoogleAuthProvider.getCredential(
                        idToken,
                        null
                );

        auth.signInWithCredential(credential)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){

                                    MyToast("Inicio de sesión correcto");
                                    Intent intent = new Intent(
                                            MainActivity.this,
                                            Pantalla_Inicio.class);

                                    startActivity(intent);
                                    finish();
                                }else{

                                    MyToast("Error Firebase");

                                }

                            }
                        });
    }
    private void MyToast(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }
}
