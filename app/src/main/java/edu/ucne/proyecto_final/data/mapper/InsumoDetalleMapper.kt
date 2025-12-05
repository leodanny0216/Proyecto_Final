package edu.ucne.proyecto_final.data.mapper

import edu.ucne.proyecto_final.data.local.entity.InsumoDetalleEntity
import edu.ucne.proyecto_final.data.remote.dto.InsumoDetalleDto
import edu.ucne.proyecto_final.data.local.entity.SyncStatus

fun InsumoDetalleDto.toEntity(): InsumoDetalleEntity =
    InsumoDetalleEntity(
        insumoDetalleId = this.insumoDetalleId,
        insumoId = this.insumoId,
        nombre = this.nombre,
        descripcion = this.descripcion,
        cantidad = this.cantidad,
        precioUnidad = this.precioUnidad,
        fechaAdquisicion = this.fechaAdquisicion,
        proveedorId = this.proveedorId,
        proveedorNombre = this.proveedorNombre,
        categoriaId = this.categoriaId,
        categoriaNombre = this.categoriaNombre,
        valorTotal = this.valorTotal,
        updatedAt = System.currentTimeMillis(),
        isDeleted = false,
        syncStatus = SyncStatus.SYNCED
    )

fun InsumoDetalleEntity.toDto(): InsumoDetalleDto =
    InsumoDetalleDto(
        insumoDetalleId = this.insumoDetalleId,
        insumoId = this.insumoId,
        nombre = this.nombre,
        descripcion = this.descripcion,
        cantidad = this.cantidad,
        precioUnidad = this.precioUnidad,
        fechaAdquisicion = this.fechaAdquisicion,
        proveedorId = this.proveedorId,
        proveedorNombre = this.proveedorNombre,
        categoriaId = this.categoriaId,
        categoriaNombre = this.categoriaNombre,
        valorTotal = this.valorTotal,
        fechaAdquisicionFormateada = this.fechaAdquisicion,
        valorTotalFormateado = this.valorTotal.toString(),
        insumo = null
    )
