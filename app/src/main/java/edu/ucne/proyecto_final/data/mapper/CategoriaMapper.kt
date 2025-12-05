package edu.ucne.proyecto_final.data.mapper

import edu.ucne.proyecto_final.data.local.entity.CategoriaEntity
import edu.ucne.proyecto_final.data.remote.dto.CategoriaDto
import edu.ucne.proyecto_final.data.local.entity.SyncStatus

fun CategoriaDto.toEntity(): CategoriaEntity =
    CategoriaEntity(
        categoriaId = this.categoriaId,
        nombre = this.nombre,
        descripcion = this.descripcion,
        updatedAt = System.currentTimeMillis(),
        isDeleted = false,
        syncStatus = SyncStatus.SYNCED
    )

fun CategoriaEntity.toDto(): CategoriaDto =
    CategoriaDto(
        categoriaId = this.categoriaId,
        nombre = this.nombre,
        descripcion = this.descripcion
    )
