package edu.ucne.proyecto_final.data.mapper

import edu.ucne.proyecto_final.data.local.entity.CompraDetalleEntity
import edu.ucne.proyecto_final.data.remote.dto.CompraDetalleDto
import edu.ucne.proyecto_final.data.local.entity.SyncStatus

fun CompraDetalleDto.toEntity(): CompraDetalleEntity =
    CompraDetalleEntity(
        compraDetalleId = this.compraDetalleId,
        compraId = this.compraId,
        fecha = this.fecha,
        total = this.total,
        updatedAt = System.currentTimeMillis(),
        isDeleted = false,
        syncStatus = SyncStatus.SYNCED
    )

fun CompraDetalleEntity.toDto(): CompraDetalleDto =
    CompraDetalleDto(
        compraDetalleId = this.compraDetalleId,
        compraId = this.compraId,
        fecha = this.fecha,
        total = this.total,
        fechaFormateada = this.fecha,
        totalFormateado = this.total.toString(),
        compra = ""
    )
