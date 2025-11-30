package edu.ucne.proyecto_final.presentation.compra

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.repository.CompraRepository
import edu.ucne.proyecto_final.data.repository.CompraDetalleRepository
import edu.ucne.proyecto_final.data.remote.dto.CompraDto
import edu.ucne.proyecto_final.data.remote.dto.CompraDetalleDto
import edu.ucne.proyecto_final.dto.remote.ProveedorDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompraViewModel @Inject constructor(
    private val compraRepository: CompraRepository,
    private val detalleRepository: CompraDetalleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompraUiState())
    val uiState: StateFlow<CompraUiState> = _uiState.asStateFlow()

    private val _comprasState = MutableStateFlow<Resource<List<CompraDto>>>(Resource.Loading())
    val comprasState: StateFlow<Resource<List<CompraDto>>> = _comprasState.asStateFlow()

    init {
        loadCompras()
    }

    fun loadCompras() {
        viewModelScope.launch {
            compraRepository.getCompras().collect { resource ->
                _comprasState.value = resource
                when(resource) {
                    is Resource.Success -> _uiState.update {
                        it.copy(
                            compras = resource.data ?: emptyList(),
                            comprasFiltradas = resource.data ?: emptyList(),
                            isLoadingCompras = false,
                            errorCompras = null
                        )
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isLoadingCompras = false,
                            errorCompras = resource.message,
                            compras = emptyList(),
                            comprasFiltradas = emptyList()
                        )
                    }
                    is Resource.Loading -> _uiState.update {
                        it.copy(isLoadingCompras = true, errorCompras = null)
                    }
                }
            }
        }
    }

    fun createCompra() {
        val currentState = _uiState.value

        if (currentState.proveedor == null) {
            _uiState.update { it.copy(errorMessage = "Debe seleccionar un proveedor") }
            return
        }

        if (currentState.fecha.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Debe ingresar una fecha") }
            return
        }

        if (currentState.articulo.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Debe ingresar un artículo") }
            return
        }

        if (currentState.cantidad <= 0) {
            _uiState.update { it.copy(errorMessage = "La cantidad debe ser mayor a 0") }
            return
        }

        _uiState.update { it.copy(isCreating = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val nuevaCompra = CompraDto(
                    compraId = 0,
                    proveedor = currentState.proveedor,
                    proveedorId = currentState.proveedor.proveedorId,
                    proveedorNombre = currentState.proveedor.nombre,
                    fecha = currentState.fecha,
                    articulo = currentState.articulo,
                    cantidad = currentState.cantidad,
                    detalles = listOf("${currentState.articulo} - ${currentState.cantidad}")
                )

                when(val result = compraRepository.createCompra(nuevaCompra)) {
                    is Resource.Success -> {
                        val compraCreada = result.data
                        if (compraCreada != null) {
                            // Crear detalle asociado
                            val detalle = CompraDetalleDto(
                                compraDetalleId = 0,
                                compraId = compraCreada.compraId,
                                fecha = currentState.fecha,
                                total = currentState.cantidad,
                                compra = null
                            )
                            detalleRepository.createCompraDetalle(detalle)
                        }

                        _uiState.update {
                            it.copy(
                                isCreating = false,
                                successMessage = "Compra creada exitosamente",
                                errorMessage = null,
                                proveedor = null,
                                fecha = "",
                                articulo = "",
                                cantidad = 0,
                                total = 0
                            )
                        }
                        loadCompras()
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isCreating = false,
                            errorMessage = result.message ?: "Error al crear compra",
                            successMessage = null
                        )
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isCreating = false,
                        errorMessage = "Error: ${e.message}"
                    )
                }
            }
        }
    }

    fun updateCompra() {
        val currentState = _uiState.value

        if (currentState.compraId == 0) {
            _uiState.update { it.copy(errorMessage = "No hay compra seleccionada para actualizar") }
            return
        }

        if (currentState.proveedor == null) {
            _uiState.update { it.copy(errorMessage = "Debe seleccionar un proveedor") }
            return
        }

        if (currentState.fecha.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Debe ingresar una fecha") }
            return
        }

        if (currentState.articulo.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Debe ingresar un artículo") }
            return
        }

        _uiState.update { it.copy(isUpdating = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val compraActualizada = CompraDto(
                    compraId = currentState.compraId,
                    proveedor = currentState.proveedor,
                    proveedorId = currentState.proveedor.proveedorId,
                    proveedorNombre = currentState.proveedor.nombre,
                    fecha = currentState.fecha,
                    articulo = currentState.articulo,
                    cantidad = currentState.cantidad,
                    detalles = listOf("${currentState.articulo} - ${currentState.cantidad}")
                )

                when(val result = compraRepository.updateCompra(currentState.compraId, compraActualizada)) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isUpdating = false,
                                successMessage = "Compra actualizada exitosamente",
                                errorMessage = null
                            )
                        }
                        clearForm()
                        loadCompras()
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isUpdating = false,
                            errorMessage = result.message ?: "Error al actualizar compra"
                        )
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isUpdating = false,
                        errorMessage = "Error: ${e.message}"
                    )
                }
            }
        }
    }

    fun deleteCompra(compraId: Int) {
        _uiState.update { it.copy(isDeleting = true, errorMessage = null) }

        viewModelScope.launch {
            when(val result = compraRepository.deleteCompra(compraId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            successMessage = "Compra eliminada exitosamente"
                        )
                    }
                    loadCompras()
                }
                is Resource.Error -> _uiState.update {
                    it.copy(
                        isDeleting = false,
                        errorMessage = result.message ?: "Error al eliminar compra"
                    )
                }
                else -> {}
            }
        }
    }

    fun setCompraForEdit(compra: CompraDto) {
        _uiState.update {
            it.copy(
                compraId = compra.compraId,
                proveedor = compra.proveedor,
                fecha = compra.fecha,
                articulo = compra.articulo,
                cantidad = compra.cantidad,
                total = compra.cantidad,
                compraSeleccionada = compra
            )
        }
    }

    fun setProveedor(proveedor: ProveedorDto) {
        _uiState.update { it.copy(proveedor = proveedor) }
    }

    fun setFecha(fecha: String) {
        _uiState.update { it.copy(fecha = fecha) }
    }

    fun setArticulo(articulo: String) {
        _uiState.update { it.copy(articulo = articulo) }
    }

    fun setCantidad(cantidad: Int) {
        _uiState.update { it.copy(cantidad = cantidad, total = cantidad) }
    }

    fun clearForm() {
        _uiState.update {
            it.copy(
                compraId = 0,
                proveedor = null,
                fecha = "",
                articulo = "",
                cantidad = 0,
                total = 0,
                compraSeleccionada = null,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun setSearchQuery(query: String) {
        val filtradas = if(query.isBlank()) {
            _uiState.value.compras
        } else {
            _uiState.value.compras.filter {
                it.proveedorNombre.contains(query, ignoreCase = true) ||
                        it.articulo.contains(query, ignoreCase = true) ||
                        it.fecha.contains(query, ignoreCase = true)
            }
        }
        _uiState.update { it.copy(searchQuery = query, comprasFiltradas = filtradas) }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}