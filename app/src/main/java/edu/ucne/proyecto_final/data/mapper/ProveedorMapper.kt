package edu.ucne.proyecto_final.data.mapper

import edu.ucne.proyecto_final.data.local.entity.ProveedorEntity
import edu.ucne.proyecto_final.dto.remote.ProveedorDto
import edu.ucne.proyecto_final.data.local.entity.SyncStatus

fun ProveedorDto.toEntity(): ProveedorEntity =
    ProveedorEntity(
        proveedorId = this.proveedorId,
        nombre = this.nombre,
        telefono = this.telefono,
        email = this.email,
        direccion = this.direccion,
        updatedAt = System.currentTimeMillis(),
        isDeleted = false,
        syncStatus = SyncStatus.SYNCED
    )

fun ProveedorEntity.toDto(): ProveedorDto =
    ProveedorDto(
        proveedorId = this.proveedorId,
        nombre = this.nombre,
        telefono = this.telefono,
        email = this.email,
        direccion = this.direccion
    )
