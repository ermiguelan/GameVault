package com.example.gamevault.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import java.util.Objects;

@Entity
public class Usuario {

    @PrimaryKey
    @NonNull
    private String uid;

    private String nombreUsuario;
    private String correo;

    public Usuario(@NonNull String uid) {
        this.uid = uid;
    }

    public Usuario() {

    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(uid, usuario.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uid);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "uid='" + uid + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", correo='" + correo + '\'' +
                '}';
    }
}

