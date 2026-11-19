package com.example.gamevault.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UsuarioDao {

    @Insert
    void insertar(Usuario usuario);

    @Query("SELECT * FROM Usuario")
    List<Usuario> obtenerTodos();
    @Query("DELETE FROM Usuario")
    void borrarTodos();
}
