package com.grupo5.vegastock.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.grupo5.vegastock.data.DatabaseHelper
import com.grupo5.vegastock.model.Producto

class ProductoRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun insertar(producto: Producto): Long {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put(DatabaseHelper.PRODUCTO_NOMBRE, producto.nombre)
            put(DatabaseHelper.PRODUCTO_SKU, producto.sku)
            put(DatabaseHelper.PRODUCTO_CATEGORIA, producto.categoria)
            put(DatabaseHelper.PRODUCTO_PROVEEDOR, producto.proveedor)
            put(DatabaseHelper.PRODUCTO_PRECIO_COMPRA, producto.precioCompra)
            put(DatabaseHelper.PRODUCTO_PRECIO_VENTA, producto.precioVenta)
            put(DatabaseHelper.PRODUCTO_STOCK_ACTUAL, producto.stockActual)
            put(DatabaseHelper.PRODUCTO_STOCK_MINIMO, producto.stockMinimo)
            put(DatabaseHelper.PRODUCTO_UNIDAD_MEDIDA, producto.unidadMedida)
            put(DatabaseHelper.PRODUCTO_OBSERVACIONES, producto.observaciones)
            put(DatabaseHelper.PRODUCTO_FECHA_CREACION, producto.fechaCreacion)
        }
        return db.insert(DatabaseHelper.TABLA_PRODUCTOS, null, valores)
    }

    fun obtenerTodos(): List<Producto> {
        val lista = mutableListOf<Producto>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLA_PRODUCTOS,
            null, null, null, null, null,
            "${DatabaseHelper.PRODUCTO_NOMBRE} ASC"
        )
        while (cursor.moveToNext()) {
            lista.add(cursorAProducto(cursor))
        }
        cursor.close()
        return lista
    }

    fun obtenerPorId(id: Int): Producto? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLA_PRODUCTOS,
            null,
            "${DatabaseHelper.PRODUCTO_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        var producto: Producto? = null
        if (cursor.moveToFirst()) {
            producto = cursorAProducto(cursor)
        }
        cursor.close()
        return producto
    }

    fun existeSku(sku: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLA_PRODUCTOS,
            arrayOf(DatabaseHelper.PRODUCTO_ID),
            "${DatabaseHelper.PRODUCTO_SKU} = ?",
            arrayOf(sku),
            null, null, null
        )
        val existe = cursor.count > 0
        cursor.close()
        return existe
    }

    fun contarProductos(): Int {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM ${DatabaseHelper.TABLA_PRODUCTOS}", null)
        cursor.moveToFirst()
        val total = cursor.getInt(0)
        cursor.close()
        return total
    }

    fun contarCriticos(): Int {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM ${DatabaseHelper.TABLA_PRODUCTOS} " +
                    "WHERE ${DatabaseHelper.PRODUCTO_STOCK_ACTUAL} <= ${DatabaseHelper.PRODUCTO_STOCK_MINIMO}",
            null
        )
        cursor.moveToFirst()
        val total = cursor.getInt(0)
        cursor.close()
        return total
    }

    private fun cursorAProducto(cursor: Cursor): Producto {
        return Producto(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.PRODUCTO_ID)),
            nombre = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.PRODUCTO_NOMBRE)),
            sku = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.PRODUCTO_SKU)),
            categoria = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.PRODUCTO_CATEGORIA)),
            proveedor = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.PRODUCTO_PROVEEDOR)) ?: "",
            precioCompra = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.PRODUCTO_PRECIO_COMPRA)),
            precioVenta = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.PRODUCTO_PRECIO_VENTA)),
            stockActual = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.PRODUCTO_STOCK_ACTUAL)),
            stockMinimo = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.PRODUCTO_STOCK_MINIMO)),
            unidadMedida = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.PRODUCTO_UNIDAD_MEDIDA)),
            observaciones = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.PRODUCTO_OBSERVACIONES)) ?: "",
            fechaCreacion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.PRODUCTO_FECHA_CREACION))
        )
    }
}