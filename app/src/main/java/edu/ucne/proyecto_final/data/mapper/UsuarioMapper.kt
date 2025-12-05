package edu.ucne.proyecto_final.data.mapper

import edu.ucne.proyecto_final.data.local.entity.UsuarioEntity
import edu.ucne.proyecto_final.dto.remote.UsuarioDto
import edu.ucne.proyecto_final.data.local.entity.SyncStatus

fun UsuarioDto.toEntity(): UsuarioEntity =
    UsuarioEntity(
        usuarioId = this.usuarioId,
        userName = this.userName,
        password = this.password,
        updatedAt = System.currentTimeMillis(),
        isDeleted = false,
        syncStatus = SyncStatus.SYNCED
    )

fun UsuarioEntity.toDto(): UsuarioDto =
    UsuarioDto(
        usuarioId = this.usuarioId,
        userName = this.userName,
        password = this.password
    )
