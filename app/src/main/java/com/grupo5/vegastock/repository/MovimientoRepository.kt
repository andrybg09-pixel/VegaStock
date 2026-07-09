package com.grupo5.vegastock.repository

import android.content.ContentValues
import android.content.Context
import com.grupo5.vegastock.data.DatabaseHelper
import com.grupo5.vegastock.model.Movimiento
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MovimientoRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun registrarMovimiento(
        productoId: Int,
        tipo: String,
        cantidad: Int,
        motivo: String
    ): Resultado {

        if (cantidad <= 0) {
            return Resultado.Error("La cantidad debe ser mayor a cero")
        }

        val db = dbHelper.writableDatabase

        val cursorStock = db.query(
            DatabaseHelper.TABLA_PRODUCTOS,
            arrayOf(DatabaseHelper.PRODUCTO_STOCK_ACTUAL),
            "${DatabaseHelper.PRODUCTO_ID} = ?",
            arrayOf(productoId.toString()),
            null, null, null
        )

        if (!cursorStock.moveToFirst()) {
            cursorStock.close()
            return Resultado.Error("El producto no existe")
        }

        val stockAnterior = cursorStock.getInt(0)
        cursorStock.close()

        val stockNuevo = if (tipo == Movimiento.TIPO_ENTRADA) {
            stockAnterior + cantidad
        } else {
            stockAnterior - cantidad
        }

        if (stockNuevo < 0) {
            return Resultado.Error(
                "No hay suficiente stock. Disponible: $stockAnterior unidades"
            )
        }

        val formato = SimpleDateFormat("dd MMM yyyy", Locale("es", "CR"))
        val fechaHoy = formato.format(Date())

        db.beginTransaction()
        try {
            val valoresMovimiento = ContentValues().apply {
                put(DatabaseHelper.MOVIMIENTO_PRODUCTO_ID, productoId)
                put(DatabaseHelper.MOVIMIENTO_TIPO, tipo)
                put(DatabaseHelper.MOVIMIENTO_CANTIDAD, cantidad)
                put(DatabaseHelper.MOVIMIENTO_MOTIVO, motivo)
                put(DatabaseHelper.MOVIMIENTO_STOCK_ANTERIOR, stockAnterior)
                put(DatabaseHelper.MOVIMIENTO_STOCK_NUEVO, stockNuevo)
                put(DatabaseHelper.MOVIMIENTO_FECHA, fechaHoy)
            }
            db.insertOrThrow(DatabaseHelper.TABLA_MOVIMIENTOS, null, valoresMovimiento)

            val valoresProducto = ContentValues().apply {
                put(DatabaseHelper.PRODUCTO_STOCK_ACTUAL, stockNuevo)
            }
            val filasAfectadas = db.update(
                DatabaseHelper.TABLA_PRODUCTOS,
                valoresProducto,
                "${DatabaseHelper.PRODUCTO_ID} = ?",
                arrayOf(productoId.toString())
            )

            if (filasAfectadas != 1) {
                throw Exception("No se pudo actualizar el stock")
            }

            db.setTransactionSuccessful()
            return Resultado.Exito(stockNuevo)

        } catch (e: Exception) {
            return Resultado.Error("No se pudo registrar el movimiento")
        } finally {
            db.endTransaction()
        }
    }

    fun obtenerPorProducto(productoId: Int): List<Movimiento> {
        val lista = mutableListOf<Movimiento>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLA_MOVIMIENTOS,
            null,
            "${DatabaseHelper.MOVIMIENTO_PRODUCTO_ID} = ?",
            arrayOf(productoId.toString()),
            null, null,
            "${DatabaseHelper.MOVIMIENTO_ID} DESC"
        )
        while (cursor.moveToNext()) {
            lista.add(
                Movimiento(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.MOVIMIENTO_ID)),
                    productoId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.MOVIMIENTO_PRODUCTO_ID)),
                    tipo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.MOVIMIENTO_TIPO)),
                    cantidad = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.MOVIMIENTO_CANTIDAD)),
                    motivo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.MOVIMIENTO_MOTIVO)),
                    stockAnterior = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.MOVIMIENTO_STOCK_ANTERIOR)),
                    stockNuevo = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.MOVIMIENTO_STOCK_NUEVO)),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.MOVIMIENTO_FECHA))
                )
            )
        }
        cursor.close()
        return lista
    }

    fun contarMovimientos(): Int {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM ${DatabaseHelper.TABLA_MOVIMIENTOS}", null)
        cursor.moveToFirst()
        val total = cursor.getInt(0)
        cursor.close()
        return total
    }

    sealed class Resultado {
        data class Exito(val stockNuevo: Int) : Resultado()
        data class Error(val mensaje: String) : Resultado()
    }
}