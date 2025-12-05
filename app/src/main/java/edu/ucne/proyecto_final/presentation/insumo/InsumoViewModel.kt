package edu.ucne.proyecto_final.presentation.insumo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.repository.InsumoRepository
import edu.ucne.proyecto_final.data.repository.InsumoDetalleRepository
import edu.ucne.proyecto_final.data.remote.dto.InsumoDto
import edu.ucne.proyecto_final.data.remote.dto.InsumoDetalleDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsumoViewModel @Inject constructor(
    private val insumoRepository: InsumoRepository,
    private val detalleRepository: InsumoDetalleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InsumoUiState())
    val uiState: StateFlow<InsumoUiState> = _uiState.asStateFlow()

    private val _insumosState = MutableStateFlow<Resource<List<InsumoDto>>>(Resource.Loading())
    val insumosState: StateFlow<Resource<List<InsumoDto>>> = _insumosState.asStateFlow()

    init {
        loadInsumos()
    }

    fun loadInsumos() {
        viewModelScope.launch {
            insumoRepository.getInsumos().collect { resource ->
                _insumosState.value = resource
                when(resource) {
                    is Resource.Success -> _uiState.update {
                        it.copy(
                            insumos = resource.data ?: emptyList(),
                            insumosFiltrados = resource.data ?: emptyList(),
                            isLoadingInsumos = false,
                            errorInsumos = null
                        )
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isLoadingInsumos = false,
                            errorInsumos = resource.message,
                            insumos = emptyList(),
                            insumosFiltrados = emptyList()
                        )
                    }
                    is Resource.Loading -> _uiState.update {
                        it.copy(isLoadingInsumos = true, errorInsumos = null)
                    }
                }
            }
        }
    }

    fun addDetalle(detalle: InsumoDetalleDto) {
        val listaActual = _uiState.value.detalles.toMutableList()
        listaActual.add(detalle)
        _uiState.update { it.copy(detalles = listaActual) }
    }

    fun removeDetalle(detalleId: Int) {
        val listaActual = _uiState.value.detalles.filter { it.insumoDetalleId != detalleId }
        _uiState.update { it.copy(detalles = listaActual) }
    }


    // Crear un detalle temporal antes de agregar
    fun createDetalleTemporal(): InsumoDetalleDto {
        val detalleId = (_uiState.value.detalles.maxOfOrNull { it.insumoDetalleId } ?: 0) + 1
        return InsumoDetalleDto(
            insumoDetalleId = detalleId,
            insumoId = _uiState.value.insumoId,
            nombre = "Detalle $detalleId",
            descripcion = "Descripción",
            cantidad = 1,
            precioUnidad = 100,
            fechaAdquisicion = "2025-12-04",
            proveedorId = _uiState.value.proveedorId,
            proveedorNombre = _uiState.value.proveedorNombre,
            categoriaId = _uiState.value.categoriaId,
            categoriaNombre = _uiState.value.categoriaNombre,
            valorTotal = 100
        )
    }

    fun createInsumo() {
        val currentState = _uiState.value

        if (currentState.nombre.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre es requerido") }
            return
        }

        if (currentState.categoriaId == 0) {
            _uiState.update { it.copy(errorMessage = "Debe seleccionar una categoría") }
            return
        }

        if (currentState.proveedorId == 0) {
            _uiState.update { it.copy(errorMessage = "Debe seleccionar un proveedor") }
            return
        }

        if (currentState.stockInicial < 0) {
            _uiState.update { it.copy(errorMessage = "El stock inicial no puede ser negativo") }
            return
        }

        _uiState.update { it.copy(isCreating = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val nuevoInsumo = InsumoDto(
                    insumoId = 0,
                    nombre = currentState.nombre,
                    categoriaId = currentState.categoriaId,
                    categoriaNombre = currentState.categoriaNombre,
                    proveedorId = currentState.proveedorId,
                    proveedorNombre = currentState.proveedorNombre,
                    stockInicial = currentState.stockInicial,
                    detalles = emptyList()
                )

                when(val result = insumoRepository.createInsumo(nuevoInsumo)) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isCreating = false,
                                successMessage = "Insumo creado exitosamente",
                                errorMessage = null,
                                nombre = "",
                                categoriaId = 0,
                                categoriaNombre = "",
                                proveedorId = 0,
                                proveedorNombre = "",
                                stockInicial = 0
                            )
                        }
                        loadInsumos()
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isCreating = false,
                            errorMessage = result.message ?: "Error al crear insumo",
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

    fun updateInsumo() {
        val currentState = _uiState.value

        if (currentState.insumoId == 0) {
            _uiState.update { it.copy(errorMessage = "No hay insumo seleccionado para actualizar") }
            return
        }

        if (currentState.nombre.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre es requerido") }
            return
        }

        _uiState.update { it.copy(isUpdating = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val insumoActualizado = InsumoDto(
                    insumoId = currentState.insumoId,
                    nombre = currentState.nombre,
                    categoriaId = currentState.categoriaId,
                    categoriaNombre = currentState.categoriaNombre,
                    proveedorId = currentState.proveedorId,
                    proveedorNombre = currentState.proveedorNombre,
                    stockInicial = currentState.stockInicial,
                    detalles = currentState.insumoSeleccionado?.detalles ?: emptyList()
                )

                when(val result = insumoRepository.updateInsumo(currentState.insumoId, insumoActualizado)) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isUpdating = false,
                                successMessage = "Insumo actualizado exitosamente",
                                errorMessage = null
                            )
                        }
                        clearForm()
                        loadInsumos()
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isUpdating = false,
                            errorMessage = result.message ?: "Error al actualizar insumo"
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

    fun deleteInsumo(insumoId: Int) {
        _uiState.update { it.copy(isDeleting = true, errorMessage = null) }

        viewModelScope.launch {
            when(val result = insumoRepository.deleteInsumo(insumoId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            successMessage = "Insumo eliminado exitosamente"
                        )
                    }
                    loadInsumos()
                }
                is Resource.Error -> _uiState.update {
                    it.copy(
                        isDeleting = false,
                        errorMessage = result.message ?: "Error al eliminar insumo"
                    )
                }
                else -> {}
            }
        }
    }

    fun setInsumoForEdit(insumo: InsumoDto) {
        _uiState.update {
            it.copy(
                insumoId = insumo.insumoId,
                nombre = insumo.nombre,
                categoriaId = insumo.categoriaId,
                categoriaNombre = insumo.categoriaNombre,
                proveedorId = insumo.proveedorId,
                proveedorNombre = insumo.proveedorNombre,
                stockInicial = insumo.stockInicial,
                insumoSeleccionado = insumo
            )
        }
    }

    fun setNombre(nombre: String) {
        _uiState.update { it.copy(nombre = nombre) }
    }

    fun setCategoria(categoriaId: Int, categoriaNombre: String) {
        _uiState.update {
            it.copy(
                categoriaId = categoriaId,
                categoriaNombre = categoriaNombre
            )
        }
    }

    fun setProveedor(proveedorId: Int, proveedorNombre: String) {
        _uiState.update {
            it.copy(
                proveedorId = proveedorId,
                proveedorNombre = proveedorNombre
            )
        }
    }

    fun setStockInicial(stock: Int) {
        _uiState.update { it.copy(stockInicial = stock) }
    }

    fun clearForm() {
        _uiState.update {
            it.copy(
                insumoId = 0,
                nombre = "",
                categoriaId = 0,
                categoriaNombre = "",
                proveedorId = 0,
                proveedorNombre = "",
                stockInicial = 0,
                insumoSeleccionado = null,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun setSearchQuery(query: String) {
        val filtrados = if(query.isBlank()) {
            _uiState.value.insumos
        } else {
            _uiState.value.insumos.filter {
                it.nombre.contains(query, ignoreCase = true) ||
                        it.categoriaNombre.contains(query, ignoreCase = true) ||
                        it.proveedorNombre.contains(query, ignoreCase = true)
            }
        }
        _uiState.update { it.copy(searchQuery = query, insumosFiltrados = filtrados) }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}