package edu.ucne.proyecto_final.presentation.compra

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.remote.dto.CompraDto
import edu.ucne.proyecto_final.data.remote.dto.CompraDetalleDto
import edu.ucne.proyecto_final.data.repository.CompraDetalleRepository
import edu.ucne.proyecto_final.data.repository.CompraRepository
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
    private val compraDetalleRepository: CompraDetalleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompraUiState())
    val uiState: StateFlow<CompraUiState> = _uiState.asStateFlow()

    init {
        loadCompras()
    }

    // -------------------------------------------------------------------------
    //  CARGAR COMPRAS
    // -------------------------------------------------------------------------
    fun loadCompras() {
        viewModelScope.launch {
            compraRepository.getCompras().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoadingCompras = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                compras = resource.data,
                                comprasFiltradas = resource.data,
                                isLoadingCompras = false,
                                errorCompras = null
                            )
                        }

                        // cargar detalles de cada compra
                        resource.data.forEach { compra ->
                            loadDetallesCompra(compra.compraId)
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorCompras = resource.message,
                                compras = emptyList(),
                                comprasFiltradas = emptyList(),
                                isLoadingCompras = false
                            )
                        }
                    }
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    //  CARGAR DETALLES
    // -------------------------------------------------------------------------
    private fun loadDetallesCompra(compraId: Int) {
        viewModelScope.launch {
            compraDetalleRepository.getCompraDetalles().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoadingDetalles = true) }
                    }
                    is Resource.Success -> {
                        val filtrados = resource.data.filter { it.compraId == compraId }

                        _uiState.update { state ->
                            val lista = state.detalles.toMutableList()
                            lista.removeAll { it.compraId == compraId }
                            lista.addAll(filtrados)

                            state.copy(
                                detalles = lista,
                                isLoadingDetalles = false,
                                errorDetalles = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoadingDetalles = false,
                                errorDetalles = resource.message
                            )
                        }
                    }
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    //  CREAR COMPRA
    // -------------------------------------------------------------------------
    fun createCompra() {
        val state = _uiState.value

        if (state.proveedorId <= 0) {
            _uiState.update { it.copy(errorMessage = "Seleccione un proveedor") }
            return
        }
        if (state.fecha.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Debe elegir la fecha") }
            return
        }
        if (state.detallesTemporales.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Debe agregar al menos un detalle") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isCreating = true, errorMessage = null) }

            val compra = CompraDto(
                proveedorId = state.proveedorId,
                articulo = state.detallesTemporales.first().articulo,
                cantidad = state.detallesTemporales.first().cantidad,
                fecha = state.fecha
            )

            when (val result = compraRepository.createCompra(compra)) {
                is Resource.Success -> {

                    val compraId = result.data.compraId

                    // Crear detalles
                    for (d in state.detallesTemporales) {
                        val detalle = CompraDetalleDto(
                            compraId = compraId,
                            fecha = state.fecha,
                            total = d.subtotal
                        )
                        compraDetalleRepository.createCompraDetalle(detalle)
                    }

                    _uiState.update {
                        it.copy(
                            isCreating = false,
                            successMessage = "Compra creada exitosamente"
                        )
                    }

                    clearForm()
                    loadCompras()
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isCreating = false,
                            errorMessage = result.message
                        )
                    }
                }
                else -> {}
            }
        }
    }

    // -------------------------------------------------------------------------
    //  ELIMINAR COMPRA
    // -------------------------------------------------------------------------
    fun deleteCompra(compraId: Int) {
        viewModelScope.launch {

            _uiState.update { it.copy(isDeleting = true) }

            // eliminar detalles primero
            val detalles = _uiState.value.detalles.filter { it.compraId == compraId }
            detalles.forEach { compraDetalleRepository.deleteCompraDetalle(it.compraDetalleId) }

            // eliminar compra
            when (val result = compraRepository.deleteCompra(compraId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            successMessage = "Compra eliminada correctamente"
                        )
                    }
                    loadCompras()
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            errorMessage = result.message
                        )
                    }
                }
                else -> {}
            }
        }
    }

    // -------------------------------------------------------------------------
    //  MANEJO DE DETALLES TEMPORALES PARA CREAR/EDITAR
    // -------------------------------------------------------------------------
    fun addDetalleTemporal(detalle: DetalleCompraTemporal) {
        val lista = _uiState.value.detallesTemporales.toMutableList()
        lista.add(detalle)

        _uiState.update {
            it.copy(
                detallesTemporales = lista,
                errorMessage = null
            )
        }
    }

    fun removeDetalleTemporal(index: Int) {
        val lista = _uiState.value.detallesTemporales.toMutableList()
        if (index in lista.indices) lista.removeAt(index)

        _uiState.update {
            it.copy(detallesTemporales = lista)
        }
    }

    // -------------------------------------------------------------------------
    //  SETTERS
    // -------------------------------------------------------------------------
    fun setProveedorId(id: Int) = _uiState.update { it.copy(proveedorId = id) }
    fun setFecha(fecha: String) = _uiState.update { it.copy(fecha = fecha) }

    fun setSearchQuery(query: String) {
        val lista = if (query.isBlank()) {
            _uiState.value.compras
        } else {
            _uiState.value.compras.filter {
                it.articulo.contains(query, ignoreCase = true) ||
                        it.compraId.toString().contains(query)
            }
        }

        _uiState.update { it.copy(searchQuery = query, comprasFiltradas = lista) }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(
                errorMessage = null,
                successMessage = null,
                errorCompras = null,
                errorDetalles = null
            )
        }
    }

    fun clearForm() {
        _uiState.update { CompraUiState() }
    }

    // ============================ PROVEEDOR ===============================
    fun showProveedorSelector(lista: List<ProveedorDto>) {
        _uiState.update { it.copy(showProveedorDialog = true) }
    }

    fun hideProveedorSelector() {
        _uiState.update { it.copy(showProveedorDialog = false) }
    }

    // ============================ DETALLE ACTUAL ===============================
    fun setDetalleArticulo(value: String) {
        _uiState.update { it.copy(detalleActual = it.detalleActual.copy(articulo = value)) }
    }

    fun setDetalleCantidad(value: Int) {
        _uiState.update { it.copy(detalleActual = it.detalleActual.copy(cantidad = value)) }
    }

    fun setDetallePrecioUnitario(value: Double) {
        _uiState.update { it.copy(detalleActual = it.detalleActual.copy(precioUnitario = value)) }
    }

    fun addDetalle() {
        val d = _uiState.value.detalleActual

        if (d.articulo.isBlank() || d.cantidad <= 0 || d.precioUnitario <= 0) return

        val lista = _uiState.value.detallesTemporales.toMutableList()
        lista.add(d)

        _uiState.update {
            it.copy(
                detallesTemporales = lista,
                detalleActual = DetalleCompraTemporal() // limpiar form
            )
        }
    }

}

data class DetalleCompraTemporal(
    val articulo: String = "",
    val cantidad: Int = 0,
    val precioUnitario: Double = 0.0
) {
    val subtotal: Double get() = cantidad * precioUnitario
}
