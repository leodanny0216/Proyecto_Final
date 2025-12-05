package edu.ucne.proyecto_final.presentation.navegation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

    @Serializable
    data object StartScreen : Screen()

    @Serializable
    data object LoginScreen : Screen()

    @Serializable
    data object RegisterScreen : Screen()

    @Serializable
    data class Menu(val usuarioId: Int? = null) : Screen()

    @Serializable
    data class Perfil(val usuarioId: Int) : Screen()

    @Serializable
    data object UsuariosScreen : Screen()

    @Serializable
    data class EditarUsuario(val usuarioId: Int) : Screen()

    @Serializable
    data object CrearUsuario : Screen()

    // Categorías
    @Serializable
    data object CategoriaScreen : Screen()
    @Serializable
    data object CategoriaListScreen : Screen()


    // Clientes
    @Serializable
    data object ClienteScreen : Screen()

    // Proveedores
    @Serializable
    data object ProveedorListScreen : Screen()

    @Serializable
    data object ProveedorScreen : Screen()

    @Serializable
    data class EditarProveedor(val proveedorId: Int) : Screen()

    // Compras
    @Serializable
    data object CompraListScreen : Screen()

    @Serializable
    data object CompraScreen : Screen()

    @Serializable
    data class EditarCompra(val compraId: Int) : Screen()

    // Insumos
    @Serializable
    data object InsumoListScreen : Screen()

    @Serializable
    data object InsumoScreen : Screen()

    @Serializable
    data class EditarInsumo(val insumoId: Int) : Screen()

    // Reclamos
    @Serializable
    data object ReclamoListScreen : Screen()

    @Serializable
    data object ReclamoScreen : Screen()

    @Serializable
    data class EditarReclamo(val reclamoId: Int) : Screen()
}