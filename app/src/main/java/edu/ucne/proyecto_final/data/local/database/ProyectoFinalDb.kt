package edu.ucne.proyecto_final.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.proyecto_final.data.local.dao.*
import edu.ucne.proyecto_final.data.local.entity.*

@Database(
    entities = [
        CategoriaEntity::class,
        ClienteEntity::class,
        ClienteDetalleEntity::class,
        CompraEntity::class,
        CompraDetalleEntity::class,
        InsumoEntity::class,
        InsumoDetalleEntity::class,
        ProveedorEntity::class,
        ReclamoEntity::class,
        UsuarioEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ProyectoFinalDb : RoomDatabase() {
    abstract fun categoriaDao(): CategoriaDao
    abstract fun clienteDao(): ClienteDao
    abstract fun clienteDetalleDao(): ClienteDetalleDao
    abstract fun compraDao(): CompraDao
    abstract fun compraDetalleDao(): CompraDetalleDao
    abstract fun insumoDao(): InsumoDao
    abstract fun insumoDetalleDao(): InsumoDetalleDao
    abstract fun proveedorDao(): ProveedorDao
    abstract fun reclamoDao(): ReclamoDao
    abstract fun usuarioDao(): UsuarioDao
}
