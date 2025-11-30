package edu.ucne.proyecto_final.presentation.cliente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.repository.ClienteRepository
import edu.ucne.proyecto_final.data.repository.ClienteDetalleRepository
import edu.ucne.proyecto_final.data.remote.dto.ClienteDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClienteViewModel @Inject constructor(
    private val repository: ClienteRepository,
    private val detalleRepository: ClienteDetalleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClienteUiState())
    val uiState: StateFlow<ClienteUiState> = _uiState.asStateFlow()

    private val _clientesState = MutableStateFlow<Resource<List<ClienteDto>>>(Resource.Loading())
    val clientesState: StateFlow<Resource<List<ClienteDto>>> = _clientesState.asStateFlow()

    init { loadClientes() }

    fun loadClientes() {
        viewModelScope.launch {
            repository.getClientes().collect { resource ->
                _clientesState.value = resource
                when(resource) {
                    is Resource.Success -> _uiState.update {
                        it.copy(
                            clientes = resource.data,
                            clientesFiltrados = resource.data,
                            isLoadingClientes = false,
                            errorClientes = null
                        )
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isLoadingClientes = false,
                            errorClientes = resource.message,
                            clientes = emptyList(),
                            clientesFiltrados = emptyList()
                        )
                    }
                    is Resource.Loading -> _uiState.update { it.copy(isLoadingClientes = true, errorClientes = null) }
                }
            }
        }
    }

    fun createCliente() {
        val currentState = _uiState.value
        if (currentState.nombre.isBlank() || currentState.apellido.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Todos los campos son requeridos") }
            return
        }

        _uiState.update { it.copy(isCreating = true, errorMessage = null) }
        viewModelScope.launch {
            val nuevoCliente = ClienteDto(
                clienteId = 0,
                nombre = currentState.nombre,
                apellido = currentState.apellido,
                numeroTelefono = currentState.numeroTelefono,
                correoElectronico = currentState.correoElectronico,
                direccion = currentState.direccion,
                detalles = currentState.detalles
            )

            when(val result = repository.createCliente(nuevoCliente)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isCreating = false,
                            successMessage = "Cliente creado exitosamente",
                            errorMessage = null,
                            nombre = "",
                            apellido = "",
                            numeroTelefono = "",
                            correoElectronico = "",
                            direccion = "",
                            detalles = emptyList()
                        )
                    }
                    loadClientes()
                }
                is Resource.Error -> _uiState.update { it.copy(isCreating = false, errorMessage = result.message, successMessage = null) }
                else -> {}
            }
        }
    }

    fun updateCliente() {
        val currentState = _uiState.value
        if (currentState.clienteId == 0) return

        _uiState.update { it.copy(isUpdating = true, errorMessage = null) }
        viewModelScope.launch {
            val actualizado = ClienteDto(
                clienteId = currentState.clienteId,
                nombre = currentState.nombre,
                apellido = currentState.apellido,
                numeroTelefono = currentState.numeroTelefono,
                correoElectronico = currentState.correoElectronico,
                direccion = currentState.direccion,
                detalles = currentState.detalles
            )

            when(val result = repository.updateCliente(currentState.clienteId, actualizado)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(isUpdating = false, successMessage = "Cliente actualizado exitosamente", errorMessage = null)
                    }
                    loadClientes()
                }
                is Resource.Error -> _uiState.update { it.copy(isUpdating = false, errorMessage = result.message, successMessage = null) }
                else -> {}
            }
        }
    }

    fun deleteCliente(id: Int) {
        _uiState.update { it.copy(isDeleting = true, errorMessage = null) }
        viewModelScope.launch {
            when(val result = repository.deleteCliente(id)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(isDeleting = false, successMessage = "Cliente eliminado", errorMessage = null)
                    }
                    loadClientes()
                }
                is Resource.Error -> _uiState.update { it.copy(isDeleting = false, errorMessage = result.message, successMessage = null) }
                else -> {}
            }
        }
    }

    fun setClienteForEdit(cliente: ClienteDto) {
        _uiState.update {
            it.copy(
                clienteId = cliente.clienteId,
                nombre = cliente.nombre,
                apellido = cliente.apellido,
                numeroTelefono = cliente.numeroTelefono,
                correoElectronico = cliente.correoElectronico,
                direccion = cliente.direccion,
                detalles = cliente.detalles,
                clienteSeleccionado = cliente,
                errorMessage = null
            )
        }
    }

    fun setNombre(nombre: String) { _uiState.update { it.copy(nombre = nombre) } }
    fun setApellido(apellido: String) { _uiState.update { it.copy(apellido = apellido) } }
    fun setNumeroTelefono(numero: String) { _uiState.update { it.copy(numeroTelefono = numero) } }
    fun setCorreoElectronico(correo: String) { _uiState.update { it.copy(correoElectronico = correo) } }
    fun setDireccion(direccion: String) { _uiState.update { it.copy(direccion = direccion) } }

    fun setSearchQuery(query: String) {
        val filtrados = if (query.isBlank()) _uiState.value.clientes
        else _uiState.value.clientes.filter { it.nombre.contains(query, true) || it.apellido.contains(query, true) }
        _uiState.update { it.copy(searchQuery = query, clientesFiltrados = filtrados) }
    }

    fun clearForm() {
        _uiState.update {
            it.copy(
                clienteId = 0,
                nombre = "",
                apellido = "",
                numeroTelefono = "",
                correoElectronico = "",
                direccion = "",
                detalles = emptyList(),
                clienteSeleccionado = null,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}
