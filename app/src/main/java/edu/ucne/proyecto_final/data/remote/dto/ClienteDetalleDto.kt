package edu.ucne.proyecto_final.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ClienteDetalleDto(
    val clienteDetalleId: Int = 0,
    val clienteId: Int = 0,
    val nombre: String = "",
    val apellido: String = "",
    val numeroTelefono: String = "",
    val correoElectronico: String = "",
    val direccion: String = "",
    val imagenPerfil: String = "",
    val notasAdicionales: String = "",
    val ultimoContacto: String = "",
    val codigoCliente: String = "",
    val nombreCompleto: String = "",
    val telefonoFormateado: String = "",
    val direccionCompleta: String = "",
    val cliente: String = ""
)