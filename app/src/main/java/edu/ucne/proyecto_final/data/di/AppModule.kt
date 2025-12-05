package edu.ucne.proyecto_final.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.proyecto_final.data.local.database.ProyectoFinalDb
import edu.ucne.proyecto_final.data.local.dao.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): ProyectoFinalDb =
        Room.databaseBuilder(
            context,
            ProyectoFinalDb::class.java,
            "ProyectoFinal.db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideCategoriaDao(db: ProyectoFinalDb): CategoriaDao = db.categoriaDao()

    @Provides
    @Singleton
    fun provideClienteDao(db: ProyectoFinalDb): ClienteDao = db.clienteDao()

    @Provides
    @Singleton
    fun provideClienteDetalleDao(db: ProyectoFinalDb): ClienteDetalleDao = db.clienteDetalleDao()

    @Provides
    @Singleton
    fun provideCompraDao(db: ProyectoFinalDb): CompraDao = db.compraDao()

    @Provides
    @Singleton
    fun provideCompraDetalleDao(db: ProyectoFinalDb): CompraDetalleDao = db.compraDetalleDao()

    @Provides
    @Singleton
    fun provideInsumoDao(db: ProyectoFinalDb): InsumoDao = db.insumoDao()

    @Provides
    @Singleton
    fun provideInsumoDetalleDao(db: ProyectoFinalDb): InsumoDetalleDao = db.insumoDetalleDao()

    @Provides
    @Singleton
    fun provideProveedorDao(db: ProyectoFinalDb): ProveedorDao = db.proveedorDao()

    @Provides
    @Singleton
    fun provideReclamoDao(db: ProyectoFinalDb): ReclamoDao = db.reclamoDao()

    @Provides
    @Singleton
    fun provideUsuarioDao(db: ProyectoFinalDb): UsuarioDao = db.usuarioDao()
}
