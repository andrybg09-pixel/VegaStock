package com.grupo5.vegastock.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    NOMBRE_BD,
    null,
    VERSION_BD
) {

    companion object {
        const val NOMBRE_BD = "vegastock.db"
        const val VERSION_BD = 2

        const val TABLA_USUARIOS = "usuarios"
        const val USUARIO_ID = "id"
        const val USUARIO_NOMBRE_USUARIO = "nombre_usuario"
        const val USUARIO_CONTRASENA = "contrasena"
        const val USUARIO_NOMBRE_COMPLETO = "nombre_completo"
        const val USUARIO_ROL = "rol"

        const val TABLA_PRODUCTOS = "productos"
        const val PRODUCTO_ID = "id"
        const val PRODUCTO_NOMBRE = "nombre"
        const val PRODUCTO_SKU = "sku"
        const val PRODUCTO_CATEGORIA = "categoria"
        const val PRODUCTO_PROVEEDOR = "proveedor"
        const val PRODUCTO_PRECIO_COMPRA = "precio_compra"
        const val PRODUCTO_PRECIO_VENTA = "precio_venta"
        const val PRODUCTO_STOCK_ACTUAL = "stock_actual"
        const val PRODUCTO_STOCK_MINIMO = "stock_minimo"
        const val PRODUCTO_UNIDAD_MEDIDA = "unidad_medida"
        const val PRODUCTO_OBSERVACIONES = "observaciones"
        const val PRODUCTO_FECHA_CREACION = "fecha_creacion"

        const val TABLA_MOVIMIENTOS = "movimientos"
        const val MOVIMIENTO_ID = "id"
        const val MOVIMIENTO_PRODUCTO_ID = "producto_id"
        const val MOVIMIENTO_TIPO = "tipo"
        const val MOVIMIENTO_CANTIDAD = "cantidad"
        const val MOVIMIENTO_MOTIVO = "motivo"
        const val MOVIMIENTO_STOCK_ANTERIOR = "stock_anterior"
        const val MOVIMIENTO_STOCK_NUEVO = "stock_nuevo"
        const val MOVIMIENTO_FECHA = "fecha"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val crearTablaUsuarios = """
            CREATE TABLE $TABLA_USUARIOS (
                $USUARIO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $USUARIO_NOMBRE_USUARIO TEXT NOT NULL UNIQUE,
                $USUARIO_CONTRASENA TEXT NOT NULL,
                $USUARIO_NOMBRE_COMPLETO TEXT NOT NULL,
                $USUARIO_ROL TEXT NOT NULL
            )
        """.trimIndent()

        val crearTablaProductos = """
            CREATE TABLE $TABLA_PRODUCTOS (
                $PRODUCTO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $PRODUCTO_NOMBRE TEXT NOT NULL,
                $PRODUCTO_SKU TEXT NOT NULL UNIQUE,
                $PRODUCTO_CATEGORIA TEXT NOT NULL,
                $PRODUCTO_PROVEEDOR TEXT,
                $PRODUCTO_PRECIO_COMPRA REAL NOT NULL DEFAULT 0,
                $PRODUCTO_PRECIO_VENTA REAL NOT NULL DEFAULT 0,
                $PRODUCTO_STOCK_ACTUAL INTEGER NOT NULL DEFAULT 0,
                $PRODUCTO_STOCK_MINIMO INTEGER NOT NULL DEFAULT 0,
                $PRODUCTO_UNIDAD_MEDIDA TEXT NOT NULL DEFAULT 'Unidad',
                $PRODUCTO_OBSERVACIONES TEXT,
                $PRODUCTO_FECHA_CREACION TEXT NOT NULL
            )
        """.trimIndent()

        val crearTablaMovimientos = """
            CREATE TABLE $TABLA_MOVIMIENTOS (
                $MOVIMIENTO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $MOVIMIENTO_PRODUCTO_ID INTEGER NOT NULL,
                $MOVIMIENTO_TIPO TEXT NOT NULL,
                $MOVIMIENTO_CANTIDAD INTEGER NOT NULL,
                $MOVIMIENTO_MOTIVO TEXT NOT NULL,
                $MOVIMIENTO_STOCK_ANTERIOR INTEGER NOT NULL,
                $MOVIMIENTO_STOCK_NUEVO INTEGER NOT NULL,
                $MOVIMIENTO_FECHA TEXT NOT NULL,
                FOREIGN KEY ($MOVIMIENTO_PRODUCTO_ID)
                    REFERENCES $TABLA_PRODUCTOS ($PRODUCTO_ID)
                    ON DELETE CASCADE
            )
        """.trimIndent()

        db.execSQL(crearTablaUsuarios)
        db.execSQL(crearTablaProductos)
        db.execSQL(crearTablaMovimientos)
    }

    override fun onUpgrade(db: SQLiteDatabase, versionAnterior: Int, versionNueva: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLA_MOVIMIENTOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_PRODUCTOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_USUARIOS")
        onCreate(db)
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }
}