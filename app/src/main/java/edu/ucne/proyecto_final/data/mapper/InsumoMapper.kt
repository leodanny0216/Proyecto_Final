package edu.ucne.proyecto_final.data.mapper

import edu.ucne.proyecto_final.data.local.entity.InsumoEntity
import edu.ucne.proyecto_final.data.remote.dto.InsumoDto
import edu.ucne.proyecto_final.data.local.entity.SyncStatus

fun InsumoDto.toEntity(): InsumoEntity =
    InsumoEntity(
        insumoId = this.insumoId,
        nombre = this.nombre,
        categoriaId = this.categoriaId,
        categoriaNombre = this.categoriaNombre,
        proveedorId = this.proveedorId,
        proveedorNombre = this.proveedorNombre,
        stockInicial = this.stockInicial,
        updatedAt = System.currentTimeMillis(),
        isDeleted = false,
        syncStatus = SyncStatus.SYNCED
    )

fun InsumoEntity.toDto(): InsumoDto =
    InsumoDto(
        insumoId = this.insumoId,
        nombre = this.nombre,
        categoriaId = this.categoriaId,
        categoriaNombre = this.categoriaNombre,
        proveedorId = this.proveedorId,
        proveedorNombre = this.proveedorNombre,
        stockInicial = this.stockInicial,
        detalles = emptyList()
    )
