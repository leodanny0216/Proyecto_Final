package edu.ucne.proyecto_final.presentation.proveedor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.repository.ProveedorRepository
import edu.ucne.proyecto_final.dto.remote.ProveedorDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProveedorViewModel @Inject constructor(
    private val proveedorRepository: ProveedorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProveedorUiState())
    val uiState: StateFlow<ProveedorUiState> = _uiState.asStateFlow()

    private val _proveedoresState = MutableStateFlow<Resource<List<ProveedorDto>>>(Resource.Loading())
    val proveedoresState: StateFlow<Resource<List<ProveedorDto>>> = _proveedoresState.asStateFlow()

    init {
        loadProveedores()
    }

    fun loadProveedores() {
        viewModelScope.launch {
            proveedorRepository.getProveedores().collect { resource ->
                _proveedoresState.value = resource
                when(resource) {
                    is Resource.Success -> _uiState.update {
                        it.copy(
                            proveedores = resource.data ?: emptyList(),
                            proveedoresFiltrados = resource.data ?: emptyList(),
                            isLoadingProveedores = false,
                            errorProveedores = null
                        )
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isLoadingProveedores = false,
                            errorProveedores = resource.message,
                            proveedores = emptyList(),
                            proveedoresFiltrados = emptyList()
                        )
                    }
                    is Resource.Loading -> _uiState.update {
                        it.copy(isLoadingProveedores = true, errorProveedores = null)
                    }
                }
            }
        }
    }

    fun createProveedor() {
        val currentState = _uiState.value

        if (currentState.nombre.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre es requerido") }
            return
        }

        if (currentState.telefono.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El teléfono es requerido") }
            return
        }

        if (currentState.email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El email es requerido") }
            return
        }

        if (!isValidEmail(currentState.email)) {
            _uiState.update { it.copy(errorMessage = "El email no es válido") }
            return
        }

        _uiState.update { it.copy(isCreating = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val nuevoProveedor = ProveedorDto(
                    proveedorId = 0,
                    nombre = currentState.nombre.trim(),
                    telefono = currentState.telefono.trim(),
                    email = currentState.email.trim(),
                    direccion = currentState.direccion.trim()
                )

                when(val result = proveedorRepository.createProveedor(nuevoProveedor)) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isCreating = false,
                                successMessage = "Proveedor creado exitosamente",
                                errorMessage = null,
                                nombre = "",
                                telefono = "",
                                email = "",
                                direccion = ""
                            )
                        }
                        loadProveedores()
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isCreating = false,
                            errorMessage = result.message ?: "Error al crear proveedor",
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

    fun updateProveedor() {
        val currentState = _uiState.value

        if (currentState.proveedorId == 0) {
            _uiState.update { it.copy(errorMessage = "No hay proveedor seleccionado para actualizar") }
            return
        }

        if (currentState.nombre.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre es requerido") }
            return
        }

        if (currentState.telefono.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El teléfono es requerido") }
            return
        }

        if (currentState.email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El email es requerido") }
            return
        }

        if (!isValidEmail(currentState.email)) {
            _uiState.update { it.copy(errorMessage = "El email no es válido") }
            return
        }

        _uiState.update { it.copy(isUpdating = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val proveedorActualizado = ProveedorDto(
                    proveedorId = currentState.proveedorId,
                    nombre = currentState.nombre.trim(),
                    telefono = currentState.telefono.trim(),
                    email = currentState.email.trim(),
                    direccion = currentState.direccion.trim()
                )

                when(val result = proveedorRepository.updateProveedor(currentState.proveedorId, proveedorActualizado)) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isUpdating = false,
                                successMessage = "Proveedor actualizado exitosamente",
                                errorMessage = null
                            )
                        }
                        clearForm()
                        loadProveedores()
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isUpdating = false,
                            errorMessage = result.message ?: "Error al actualizar proveedor"
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

    fun deleteProveedor(proveedorId: Int) {
        _uiState.update { it.copy(isDeleting = true, errorMessage = null) }

        viewModelScope.launch {
            when(val result = proveedorRepository.deleteProveedor(proveedorId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            successMessage = "Proveedor eliminado exitosamente"
                        )
                    }
                    loadProveedores()
                }
                is Resource.Error -> _uiState.update {
                    it.copy(
                        isDeleting = false,
                        errorMessage = result.message ?: "Error al eliminar proveedor"
                    )
                }
                else -> {}
            }
        }
    }

    fun setProveedorForEdit(proveedor: ProveedorDto) {
        _uiState.update {
            it.copy(
                proveedorId = proveedor.proveedorId,
                nombre = proveedor.nombre,
                telefono = proveedor.telefono,
                email = proveedor.email,
                direccion = proveedor.direccion,
                proveedorSeleccionado = proveedor
            )
        }
    }

    fun setNombre(nombre: String) {
        _uiState.update { it.copy(nombre = nombre) }
    }

    fun setTelefono(telefono: String) {
        _uiState.update { it.copy(telefono = telefono) }
    }

    fun setEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun setDireccion(direccion: String) {
        _uiState.update { it.copy(direccion = direccion) }
    }

    fun clearForm() {
        _uiState.update {
            it.copy(
                proveedorId = 0,
                nombre = "",
                telefono = "",
                email = "",
                direccion = "",
                proveedorSeleccionado = null,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun setSearchQuery(query: String) {
        val filtrados = if(query.isBlank()) {
            _uiState.value.proveedores
        } else {
            _uiState.value.proveedores.filter {
                it.nombre.contains(query, ignoreCase = true) ||
                        it.telefono.contains(query, ignoreCase = true) ||
                        it.email.contains(query, ignoreCase = true) ||
                        it.direccion.contains(query, ignoreCase = true)
            }
        }
        _uiState.update { it.copy(searchQuery = query, proveedoresFiltrados = filtrados) }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}