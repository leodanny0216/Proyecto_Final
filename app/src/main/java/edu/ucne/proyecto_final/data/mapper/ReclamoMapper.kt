package edu.ucne.proyecto_final.data.mapper

import edu.ucne.proyecto_final.data.local.entity.ReclamoEntity
import edu.ucne.proyecto_final.data.remote.dto.ReclamoDto
import edu.ucne.proyecto_final.data.local.entity.SyncStatus

fun ReclamoDto.toEntity(): ReclamoEntity =
    ReclamoEntity(
        reclamoId = this.reclamoId,
        tipoReclamoId = this.tipoReclamoId,
        tipoReclamo = this.tipoReclamo,
        fechaIncidente = this.fechaIncidente,
        descripcion = this.descripcion,
        updatedAt = System.currentTimeMillis(),
        isDeleted = false,
        syncStatus = SyncStatus.SYNCED
    )

fun ReclamoEntity.toDto(): ReclamoDto =
    ReclamoDto(
        reclamoId = this.reclamoId,
        tipoReclamoId = this.tipoReclamoId,
        tipoReclamo = this.tipoReclamo,
        fechaIncidente = this.fechaIncidente,
        descripcion = this.descripcion,
        evidencias = emptyList()
    )
