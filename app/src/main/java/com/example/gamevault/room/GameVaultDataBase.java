package com.example.gamevault.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {Usuario.class},
        version = 1
)
public abstract class GameVaultDataBase
        extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();
}
