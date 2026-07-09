package com.grupo5.vegastock.seed

import android.content.Context
import android.content.ContentValues
import com.grupo5.vegastock.data.DatabaseHelper
import com.grupo5.vegastock.model.Producto
import com.grupo5.vegastock.repository.ProductoRepository
import com.grupo5.vegastock.repository.UsuarioRepository

object DataSeeder {

    fun sembrar(context: Context) {
        sembrarUsuarios(context)
        sembrarProductos(context)
    }

    private fun sembrarUsuarios(context: Context) {
        val usuarioRepo = UsuarioRepository(context)
        if (usuarioRepo.contarUsuarios() > 0) return

        val db = DatabaseHelper(context).writableDatabase
        val valores = ContentValues().apply {
            put(DatabaseHelper.USUARIO_NOMBRE_USUARIO, "admin")
            put(DatabaseHelper.USUARIO_CONTRASENA, "admin123")
            put(DatabaseHelper.USUARIO_NOMBRE_COMPLETO, "Administrador")
            put(DatabaseHelper.USUARIO_ROL, "Administrador")
        }
        db.insert(DatabaseHelper.TABLA_USUARIOS, null, valores)
    }

    private fun sembrarProductos(context: Context) {
        val productoRepo = ProductoRepository(context)
        if (productoRepo.contarProductos() > 0) return

        val fecha = "01 Ene 2026"

        val productos = listOf(
            Producto(
                nombre = "Café Molido Premium",
                sku = "CAF-001",
                categoria = "Abarrotes",
                proveedor = "Proveedor Principal",
                precioCompra = 2600.0,
                precioVenta = 3500.0,
                stockActual = 25,
                stockMinimo = 10,
                unidadMedida = "Unidad",
                fechaCreacion = fecha
            ),
            Producto(
                nombre = "Azúcar 1kg",
                sku = "AZU-001",
                categoria = "Abarrotes",
                proveedor = "Distribuidora Santa Cruz",
                precioCompra = 850.0,
                precioVenta = 1200.0,
                stockActual = 5,
                stockMinimo = 10,
                unidadMedida = "Kilogramo",
                fechaCreacion = fecha
            ),
            Producto(
                nombre = "Leche Entera",
                sku = "LAC-001",
                categoria = "Lácteos",
                proveedor = "Dos Pinos",
                precioCompra = 700.0,
                precioVenta = 950.0,
                stockActual = 12,
                stockMinimo = 6,
                unidadMedida = "Litro",
                fechaCreacion = fecha
            ),
            Producto(
                nombre = "Aceite Vegetal",
                sku = "ACE-001",
                categoria = "Abarrotes",
                proveedor = "Proveedor Principal",
                precioCompra = 1500.0,
                precioVenta = 2100.0,
                stockActual = 7,
                stockMinimo = 8,
                unidadMedida = "Litro",
                fechaCreacion = fecha
            ),
            Producto(
                nombre = "Detergente Líquido",
                sku = "LIM-001",
                categoria = "Limpieza",
                proveedor = "Distribuidora Santa Cruz",
                precioCompra = 1300.0,
                precioVenta = 1850.0,
                stockActual = 18,
                stockMinimo = 5,
                unidadMedida = "Unidad",
                fechaCreacion = fecha
            ),
            Producto(
                nombre = "Refresco Natural",
                sku = "BEB-001",
                categoria = "Bebidas",
                proveedor = "Coca-Cola FEMSA",
                precioCompra = 450.0,
                precioVenta = 700.0,
                stockActual = 3,
                stockMinimo = 12,
                unidadMedida = "Unidad",
                fechaCreacion = fecha
            ),
            Producto(
                nombre = "Pan Integral",
                sku = "PAN-001",
                categoria = "Panadería",
                proveedor = "Bimbo",
                precioCompra = 900.0,
                precioVenta = 1300.0,
                stockActual = 4,
                stockMinimo = 15,
                unidadMedida = "Unidad",
                fechaCreacion = fecha
            ),
            Producto(
                nombre = "Queso Blanco",
                sku = "LAC-002",
                categoria = "Lácteos",
                proveedor = "Dos Pinos",
                precioCompra = 2200.0,
                precioVenta = 3100.0,
                stockActual = 5,
                stockMinimo = 6,
                unidadMedida = "Kilogramo",
                fechaCreacion = fecha
            )
        )

        productos.forEach { productoRepo.insertar(it) }
    }
}