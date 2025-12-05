package edu.ucne.proyecto_final.data.mapper

import edu.ucne.proyecto_final.data.local.entity.CompraEntity
import edu.ucne.proyecto_final.data.remote.dto.CompraDto
import edu.ucne.proyecto_final.data.local.entity.SyncStatus

fun CompraDto.toEntity(): CompraEntity =
    CompraEntity(
        compraId = this.compraId,
        articulo = this.articulo,
        cantidad = this.cantidad,
        proveedorId = this.proveedorId,
        proveedorNombre = this.proveedorNombre,
        fecha = this.fecha,
        updatedAt = System.currentTimeMillis(),
        isDeleted = false,
        syncStatus = SyncStatus.SYNCED
    )

fun CompraEntity.toDto(): CompraDto =
    CompraDto(
        compraId = this.compraId,
        articulo = this.articulo,
        cantidad = this.cantidad,
        proveedorId = this.proveedorId,
        proveedorNombre = this.proveedorNombre,
        fecha = this.fecha,
        detalles = emptyList(),
        proveedor = null
    )
