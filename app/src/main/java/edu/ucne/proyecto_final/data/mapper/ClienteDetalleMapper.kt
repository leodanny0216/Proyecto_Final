package edu.ucne.proyecto_final.data.mapper

import edu.ucne.proyecto_final.data.local.entity.ClienteDetalleEntity
import edu.ucne.proyecto_final.data.remote.dto.ClienteDetalleDto
import edu.ucne.proyecto_final.data.local.entity.SyncStatus

fun ClienteDetalleDto.toEntity(): ClienteDetalleEntity =
    ClienteDetalleEntity(
        clienteDetalleId = this.clienteDetalleId,
        clienteId = this.clienteId,
        nombreCompleto = this.nombreCompleto,
        numeroTelefono = this.telefonoFormateado,
        direccionCompleta = this.direccionCompleta,
        notasAdicionales = this.notasAdicionales,
        updatedAt = System.currentTimeMillis(),
        isDeleted = false,
        syncStatus = SyncStatus.SYNCED
    )

fun ClienteDetalleEntity.toDto(): ClienteDetalleDto =
    ClienteDetalleDto(
        clienteDetalleId = this.clienteDetalleId,
        clienteId = this.clienteId,
        nombreCompleto = this.nombreCompleto,
        numeroTelefono = this.numeroTelefono,
        direccionCompleta = this.direccionCompleta,
        notasAdicionales = this.notasAdicionales,
        ultimoContacto = "",
        codigoCliente = "",
        nombre = "",
        apellido = "",
        correoElectronico = "",
        imagenPerfil = "",
        cliente = ""
    )
