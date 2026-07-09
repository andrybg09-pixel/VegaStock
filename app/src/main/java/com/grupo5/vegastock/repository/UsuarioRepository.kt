package com.grupo5.vegastock.repository

import android.content.Context
import com.grupo5.vegastock.data.DatabaseHelper
import com.grupo5.vegastock.model.Usuario

class UsuarioRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun validarLogin(nombreUsuario: String, contrasena: String): Usuario? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLA_USUARIOS,
            null,
            "${DatabaseHelper.USUARIO_NOMBRE_USUARIO} = ? AND ${DatabaseHelper.USUARIO_CONTRASENA} = ?",
            arrayOf(nombreUsuario, contrasena),
            null, null, null
        )

        var usuario: Usuario? = null
        if (cursor.moveToFirst()) {
            usuario = Usuario(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.USUARIO_ID)),
                nombreUsuario = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.USUARIO_NOMBRE_USUARIO)),
                contrasena = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.USUARIO_CONTRASENA)),
                nombreCompleto = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.USUARIO_NOMBRE_COMPLETO)),
                rol = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.USUARIO_ROL))
            )
        }
        cursor.close()
        return usuario
    }

    fun contarUsuarios(): Int {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM ${DatabaseHelper.TABLA_USUARIOS}", null)
        cursor.moveToFirst()
        val total = cursor.getInt(0)
        cursor.close()
        return total
    }
}