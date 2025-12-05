package edu.ucne.proyecto_final.data.mapper

import edu.ucne.proyecto_final.data.local.entity.ClienteEntity
import edu.ucne.proyecto_final.data.remote.dto.ClienteDto
import edu.ucne.proyecto_final.data.local.entity.SyncStatus

fun ClienteDto.toEntity(): ClienteEntity =
    ClienteEntity(
        clienteId = this.clienteId,
        nombre = this.nombre,
        apellido = this.apellido,
        numeroTelefono = this.numeroTelefono,
        correoElectronico = this.correoElectronico,
        direccion = this.direccion,
        updatedAt = System.currentTimeMillis(),
        isDeleted = false,
        syncStatus = SyncStatus.SYNCED
    )

fun ClienteEntity.toDto(): ClienteDto =
    ClienteDto(
        clienteId = this.clienteId,
        nombre = this.nombre,
        apellido = this.apellido,
        numeroTelefono = this.numeroTelefono,
        correoElectronico = this.correoElectronico,
        direccion = this.direccion,
        detalles = null // puedes mapear detalles aparte
    )
