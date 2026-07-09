package com.grupo5.vegastock.model

data class Usuario(
    val id: Int = 0,
    val nombreUsuario: String,
    val contrasena: String,
    val nombreCompleto: String,
    val rol: String = "Administrador"
)