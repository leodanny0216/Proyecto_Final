package edu.ucne.proyecto_final.presentation.navegation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.ucne.proyecto_final.presentation.usuario.*
import edu.ucne.proyecto_final.presentation.categoria.*
import edu.ucne.proyecto_final.presentation.cliente.*
import edu.ucne.proyecto_final.presentation.proveedor.*
import edu.ucne.proyecto_final.presentation.compra.*
import edu.ucne.proyecto_final.presentation.insumo.*
import edu.ucne.proyecto_final.presentation.reclamo.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.StartScreen
    ) {

        // ==================== USUARIO ====================
        composable<Screen.StartScreen> {
            StartScreen(
                onSplashComplete = {
                    navController.navigate(Screen.LoginScreen) {
                        popUpTo(Screen.StartScreen) { inclusive = true }
                    }
                }
            )
        }

        composable<Screen.LoginScreen> {
            val usuarioViewModel: UsuarioViewModel = hiltViewModel()
            LoginScreen(
                viewModel = usuarioViewModel,
                onLoginClick = { usuarioId ->
                    navController.navigate(Screen.Menu(usuarioId)) {
                        popUpTo(Screen.LoginScreen) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.RegisterScreen)
                },
                goBack = { navController.navigateUp() }
            )
        }

        composable<Screen.RegisterScreen> {
            val usuarioViewModel: UsuarioViewModel = hiltViewModel()
            RegisterScreen(
                viewModel = usuarioViewModel,
                onRegisterClick = {
                    navController.navigate(Screen.LoginScreen) {
                        popUpTo(Screen.RegisterScreen) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.LoginScreen) {
                        popUpTo(Screen.RegisterScreen) { inclusive = true }
                    }
                },
                goBack = { navController.navigateUp() }
            )
        }

        composable<Screen.Menu> { backStackEntry ->
            val usuarioId = backStackEntry.arguments?.getString("usuarioId")?.toIntOrNull()
            MenuScreen(
                usuarioId = usuarioId,
                onMenuItemClick = { route ->
                    when (route) {
                        "usuarios" -> navController.navigate(Screen.UsuariosScreen)
                        "categorias" -> navController.navigate(Screen.CategoriaScreen)
                        "clientes" -> navController.navigate(Screen.ClienteScreen)
                        "proveedores" -> navController.navigate(Screen.ProveedorListScreen)
                        "compras" -> navController.navigate(Screen.CompraListScreen)
                        "insumos" -> navController.navigate(Screen.InsumoListScreen)
                        "reclamos" -> navController.navigate(Screen.ReclamoListScreen)
                    }
                },
                onNavigateToPerfil = { userId ->
                    navController.navigate(Screen.Perfil(userId))
                },
                onLogoutClick = {
                    navController.navigate(Screen.LoginScreen) {
                        popUpTo(Screen.StartScreen) { inclusive = true }
                    }
                }
            )
        }

        composable<Screen.Perfil> { backStackEntry ->
            val usuarioId = backStackEntry.arguments?.getString("usuarioId")?.toIntOrNull() ?: 0
            val usuarioViewModel: UsuarioViewModel = hiltViewModel()
            PerfilScreen(
                usuarioId = usuarioId,
                viewModel = usuarioViewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }
        composable<Screen.CategoriaScreen> {
            CategoriaScreen()
        }
        composable<Screen.CategoriaListScreen> {
            CategoriaListScreen(
                onEditCategoria = { categoriaId ->
                    navController.navigate("EditarCategoria/$categoriaId")
                }
            )
        }

        composable<Screen.UsuariosScreen> {
            val usuarioViewModel: UsuarioViewModel = hiltViewModel()
            UsuariosScreen(
                viewModel = usuarioViewModel,
                onNavigateToCrearUsuario = {
                    navController.navigate(Screen.CrearUsuario)
                },
                onNavigateToEditarUsuario = { usuarioId ->
                    navController.navigate(Screen.EditarUsuario(usuarioId))
                },
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable<Screen.CrearUsuario> {
            val usuarioViewModel: UsuarioViewModel = hiltViewModel()
            CrearUsuarioScreen(
                viewModel = usuarioViewModel,
                onUsuarioCreado = {
                    navController.navigate(Screen.UsuariosScreen) {
                        popUpTo(Screen.UsuariosScreen) { inclusive = true }
                    }
                },
                onCancelar = { navController.navigateUp() }
            )
        }

        composable<Screen.EditarUsuario> { backStackEntry ->
            val usuarioId = backStackEntry.arguments?.getString("usuarioId")?.toIntOrNull() ?: 0
            val usuarioViewModel: UsuarioViewModel = hiltViewModel()
            EditarUsuarioScreen(
                usuarioId = usuarioId,
                viewModel = usuarioViewModel,
                onUsuarioActualizado = {
                    navController.navigate(Screen.UsuariosScreen) {
                        popUpTo(Screen.UsuariosScreen) { inclusive = true }
                    }
                },
                onCancelar = { navController.navigateUp() },
                onEliminarUsuario = {
                    navController.navigate(Screen.UsuariosScreen) {
                        popUpTo(Screen.UsuariosScreen) { inclusive = true }
                    }
                }
            )
        }

        // ==================== CATEGORÍA ====================
        composable<Screen.CategoriaScreen> {
            CategoriaScreen()
        }

        // ==================== CLIENTE ====================
        composable<Screen.ClienteScreen> {
            ClienteScreen()
        }

        // ==================== PROVEEDOR ====================
        composable<Screen.ProveedorListScreen> {
            val proveedorViewModel: ProveedorViewModel = hiltViewModel()
            ProveedorListScreen(
                viewModel = proveedorViewModel,
                onNavigateToProveedor = {
                    navController.navigate(Screen.ProveedorScreen)
                },
                onEditProveedor = { proveedorId ->
                    navController.navigate(Screen.EditarProveedor(proveedorId))
                }
            )
        }

        composable<Screen.ProveedorScreen> {
            val proveedorViewModel: ProveedorViewModel = hiltViewModel()
            ProveedorScreen(
                viewModel = proveedorViewModel,
                proveedorId = 0,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable<Screen.EditarProveedor> { backStackEntry ->
            val proveedorId = backStackEntry.arguments?.getString("proveedorId")?.toIntOrNull() ?: 0
            val proveedorViewModel: ProveedorViewModel = hiltViewModel()
            ProveedorScreen(
                viewModel = proveedorViewModel,
                proveedorId = proveedorId,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // ==================== COMPRA ====================
        composable<Screen.CompraListScreen> {
            val compraViewModel: CompraViewModel = hiltViewModel()
            CompraListScreen(
                viewModel = compraViewModel,
                onNavigateToCompra = {
                    navController.navigate(Screen.CompraScreen)
                },
                onEditCompra = { compraId ->
                    navController.navigate(Screen.EditarCompra(compraId))
                }
            )
        }

        composable<Screen.CompraScreen> {
            val compraViewModel: CompraViewModel = hiltViewModel()
            val proveedorViewModel: ProveedorViewModel = hiltViewModel()
            val proveedoresState = proveedorViewModel.uiState.collectAsState()

            CompraScreen(
                viewModel = compraViewModel,
                compraId = 0,
                onNavigateBack = { navController.navigateUp() },
                proveedores = proveedoresState.value.proveedores
            )

        }

        composable<Screen.EditarCompra> { backStackEntry ->
            val compraId = backStackEntry.arguments?.getString("compraId")?.toIntOrNull() ?: 0
            val compraViewModel: CompraViewModel = hiltViewModel()
            val proveedorViewModel: ProveedorViewModel = hiltViewModel()
            val proveedoresState = proveedorViewModel.uiState.collectAsState()

            CompraScreen(
                viewModel = compraViewModel,
                compraId = compraId,
                onNavigateBack = { navController.navigateUp() },
                proveedores = proveedoresState.value.proveedores
            )
        }

        // ==================== INSUMO ====================
        composable<Screen.InsumoListScreen> {
            val insumoViewModel: InsumoViewModel = hiltViewModel()
            InsumoListScreen(
                viewModel = insumoViewModel,
                onNavigateToInsumo = {
                    navController.navigate(Screen.InsumoScreen)
                },
                onEditInsumo = { insumoId ->
                    navController.navigate(Screen.EditarInsumo(insumoId))
                }
            )
        }

        composable<Screen.InsumoScreen> {
            val insumoViewModel: InsumoViewModel = hiltViewModel()
            val categoriaViewModel: CategoriaViewModel = hiltViewModel()
            val proveedorViewModel: ProveedorViewModel = hiltViewModel()

            val categoriasState = categoriaViewModel.uiState.collectAsState()
            val proveedoresState = proveedorViewModel.uiState.collectAsState()

            InsumoScreen(
                viewModel = insumoViewModel,
                insumoId = 0,
                onNavigateBack = { navController.navigateUp() },
                categorias = categoriasState.value.categorias.map {
                    CategoriaSimple(it.categoriaId, it.nombre)
                },
                proveedores = proveedoresState.value.proveedores.map {
                    ProveedorSimple(it.proveedorId, it.nombre)
                }
            )
        }

        composable<Screen.EditarInsumo> { backStackEntry ->
            val insumoId = backStackEntry.arguments?.getString("insumoId")?.toIntOrNull() ?: 0
            val insumoViewModel: InsumoViewModel = hiltViewModel()
            val categoriaViewModel: CategoriaViewModel = hiltViewModel()
            val proveedorViewModel: ProveedorViewModel = hiltViewModel()

            val categoriasState = categoriaViewModel.uiState.collectAsState()
            val proveedoresState = proveedorViewModel.uiState.collectAsState()

            InsumoScreen(
                viewModel = insumoViewModel,
                insumoId = insumoId,
                onNavigateBack = { navController.navigateUp() },
                categorias = categoriasState.value.categorias.map {
                    CategoriaSimple(it.categoriaId, it.nombre)
                },
                proveedores = proveedoresState.value.proveedores.map {
                    ProveedorSimple(it.proveedorId, it.nombre)
                }
            )
        }

        // ==================== RECLAMO ====================
        composable<Screen.ReclamoListScreen> {
            val reclamoViewModel: ReclamoViewModel = hiltViewModel()
            ReclamoListScreen(
                viewModel = reclamoViewModel,
                onNavigateToReclamo = {
                    navController.navigate(Screen.ReclamoScreen)
                },
                onEditReclamo = { reclamoId ->
                    navController.navigate(Screen.EditarReclamo(reclamoId))
                }
            )
        }

        composable<Screen.ReclamoScreen> {
            val reclamoViewModel: ReclamoViewModel = hiltViewModel()
            ReclamoScreen(
                viewModel = reclamoViewModel,
                reclamoId = 0,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable<Screen.EditarReclamo> { backStackEntry ->
            val reclamoId = backStackEntry.arguments?.getString("reclamoId")?.toIntOrNull() ?: 0
            val reclamoViewModel: ReclamoViewModel = hiltViewModel()
            ReclamoScreen(
                viewModel = reclamoViewModel,
                reclamoId = reclamoId,
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}