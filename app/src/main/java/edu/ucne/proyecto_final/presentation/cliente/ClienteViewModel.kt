package edu.ucne.proyecto_final.presentation.cliente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.repository.ClienteRepository
import edu.ucne.proyecto_final.data.repository.ClienteDetalleRepository
import edu.ucne.proyecto_final.data.remote.dto.ClienteDto
import edu.ucne.proyecto_final.data.remote.dto.ClienteDetalleDto
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

    init {
        loadClientes()
    }

    fun loadClientes() {
        viewModelScope.launch {
            repository.getClientes().collect { resource ->
                _clientesState.value = resource
                when(resource) {
                    is Resource.Success -> {
                        val data = resource.data ?: emptyList()
                        _uiState.update {
                            it.copy(
                                clientes = data,
                                clientesFiltrados = data,
                                isLoadingClientes = false,
                                errorClientes = null
                            )
                        }
                        // Si hay un cliente seleccionado, actualizar sus detalles
                        val currentClienteId = _uiState.value.clienteId
                        if (currentClienteId > 0) {
                            cargarDetallesCliente(currentClienteId)
                        }
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isLoadingClientes = false,
                            errorClientes = resource.message,
                            clientes = emptyList(),
                            clientesFiltrados = emptyList()
                        )
                    }
                    is Resource.Loading -> _uiState.update {
                        it.copy(isLoadingClientes = true, errorClientes = null)
                    }
                }
            }
        }
    }

    private suspend fun cargarDetallesCliente(clienteId: Int) {
        try {
            detalleRepository.getClienteDetalles().collect { result ->
                when(result) {
                    is Resource.Success -> {
                        val detallesCliente = result.data?.filter { it.clienteId == clienteId } ?: emptyList()
                        _uiState.update {
                            it.copy(detalles = detallesCliente)
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(detalles = emptyList())
                        }
                    }
                    else -> {}
                }
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(detalles = emptyList())
            }
        }
    }

    fun createCliente() {
        val currentState = _uiState.value
        if (currentState.nombre.isBlank() || currentState.apellido.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Nombre y Apellido son requeridos") }
            return
        }

        _uiState.update { it.copy(isCreating = true, errorMessage = null, successMessage = null) }
        viewModelScope.launch {
            try {
                val nuevoCliente = ClienteDto(
                    clienteId = 0,
                    nombre = currentState.nombre.trim(),
                    apellido = currentState.apellido.trim(),
                    numeroTelefono = currentState.numeroTelefono.trim(),
                    correoElectronico = currentState.correoElectronico.trim(),
                    direccion = currentState.direccion.trim(),
                    detalles = null
                )

                when(val result = repository.createCliente(nuevoCliente)) {
                    is Resource.Success -> {
                        val clienteCreado = result.data
                        _uiState.update {
                            it.copy(
                                isCreating = false,
                                successMessage = "✅ Cliente creado exitosamente",
                                errorMessage = null,
                                clienteId = clienteCreado?.clienteId ?: 0,
                                clienteGuardado = true,
                                clienteSeleccionado = clienteCreado
                            )
                        }
                        loadClientes()
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isCreating = false,
                            errorMessage = "❌ Error al crear cliente: ${result.message}",
                            successMessage = null
                        )
                    }
                    else -> {
                        _uiState.update {
                            it.copy(
                                isCreating = false,
                                errorMessage = "❌ Error desconocido al crear cliente",
                                successMessage = null
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isCreating = false,
                        errorMessage = "❌ Error de conexión: ${e.message}",
                        successMessage = null
                    )
                }
            }
        }
    }

    fun updateCliente() {
        val currentState = _uiState.value
        if (currentState.clienteId == 0) {
            _uiState.update { it.copy(errorMessage = "No hay cliente seleccionado para actualizar") }
            return
        }

        _uiState.update { it.copy(isUpdating = true, errorMessage = null, successMessage = null) }
        viewModelScope.launch {
            try {
                val actualizado = ClienteDto(
                    clienteId = currentState.clienteId,
                    nombre = currentState.nombre.trim(),
                    apellido = currentState.apellido.trim(),
                    numeroTelefono = currentState.numeroTelefono.trim(),
                    correoElectronico = currentState.correoElectronico.trim(),
                    direccion = currentState.direccion.trim(),
                    detalles = null
                )

                when(val result = repository.updateCliente(currentState.clienteId, actualizado)) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isUpdating = false,
                                successMessage = "✅ Cliente actualizado exitosamente",
                                errorMessage = null,
                                clienteSeleccionado = result.data
                            )
                        }
                        loadClientes()
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isUpdating = false,
                            errorMessage = "❌ Error al actualizar: ${result.message}",
                            successMessage = null
                        )
                    }
                    else -> {
                        _uiState.update {
                            it.copy(
                                isUpdating = false,
                                errorMessage = "❌ Error desconocido al actualizar",
                                successMessage = null
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isUpdating = false,
                        errorMessage = "❌ Error de conexión: ${e.message}",
                        successMessage = null
                    )
                }
            }
        }
    }

    fun deleteCliente(id: Int) {
        _uiState.update { it.copy(isDeleting = true, errorMessage = null, successMessage = null) }
        viewModelScope.launch {
            try {
                when(val result = repository.deleteCliente(id)) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isDeleting = false,
                                successMessage = "🗑️ Cliente eliminado exitosamente",
                                errorMessage = null
                            )
                        }
                        clearForm()
                        loadClientes()
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isDeleting = false,
                            errorMessage = "❌ Error al eliminar: ${result.message}",
                            successMessage = null
                        )
                    }
                    else -> {
                        _uiState.update {
                            it.copy(
                                isDeleting = false,
                                errorMessage = "❌ Error desconocido al eliminar",
                                successMessage = null
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isDeleting = false,
                        errorMessage = "❌ Error de conexión: ${e.message}",
                        successMessage = null
                    )
                }
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
                clienteSeleccionado = cliente,
                clienteGuardado = true,
                errorMessage = null,
                successMessage = null
            )
        }
        viewModelScope.launch {
            cargarDetallesCliente(cliente.clienteId)
        }
    }

    // Setters para datos principales
    fun setNombre(nombre: String) {
        _uiState.update { it.copy(nombre = nombre, errorMessage = null) }
    }

    fun setApellido(apellido: String) {
        _uiState.update { it.copy(apellido = apellido, errorMessage = null) }
    }

    fun setNumeroTelefono(numero: String) {
        _uiState.update { it.copy(numeroTelefono = numero, errorMessage = null) }
    }

    fun setCorreoElectronico(correo: String) {
        _uiState.update { it.copy(correoElectronico = correo, errorMessage = null) }
    }

    fun setDireccion(direccion: String) {
        _uiState.update { it.copy(direccion = direccion, errorMessage = null) }
    }

    // Setters para detalle temporal
    fun setDetalleNombre(nombre: String) {
        _uiState.update { it.copy(detalleNombre = nombre, errorMessage = null) }
    }

    fun setDetalleApellido(apellido: String) {
        _uiState.update { it.copy(detalleApellido = apellido, errorMessage = null) }
    }

    fun setDetalleTelefono(telefono: String) {
        _uiState.update { it.copy(detalleTelefono = telefono, errorMessage = null) }
    }

    fun setDetalleCorreo(correo: String) {
        _uiState.update { it.copy(detalleCorreo = correo, errorMessage = null) }
    }

    fun setDetalleDireccion(direccion: String) {
        _uiState.update { it.copy(detalleDireccion = direccion, errorMessage = null) }
    }

    fun setDetalleNotas(notas: String) {
        _uiState.update { it.copy(detalleNotas = notas, errorMessage = null) }
    }

    fun setDetalleCodigoCliente(codigo: String) {
        _uiState.update { it.copy(detalleCodigoCliente = codigo, errorMessage = null) }
    }

    fun toggleDetalleDialog() {
        val currentState = _uiState.value

        if (!currentState.clienteGuardado || currentState.clienteId == 0) {
            _uiState.update {
                it.copy(errorMessage = "⚠️ Primero debes guardar el cliente antes de agregar detalles")
            }
            return
        }

        _uiState.update {
            it.copy(
                showDetalleDialog = !it.showDetalleDialog,
                errorMessage = null
            )
        }
    }

    fun agregarDetalle() {
        val currentState = _uiState.value

        if (currentState.clienteId == 0) {
            _uiState.update {
                it.copy(errorMessage = "⚠️ Primero debes guardar el cliente")
            }
            return
        }

        if (currentState.detalleNombre.isBlank() && currentState.detalleApellido.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "⚠️ Debes ingresar al menos nombre o apellido del detalle")
            }
            return
        }

        _uiState.update {
            it.copy(
                isSavingDetalle = true,
                errorMessage = null,
                successMessage = null
            )
        }

        viewModelScope.launch {
            try {
                val nuevoDetalle = ClienteDetalleDto(
                    clienteDetalleId = 0,
                    clienteId = currentState.clienteId,
                    nombre = currentState.detalleNombre.trim(),
                    apellido = currentState.detalleApellido.trim(),
                    numeroTelefono = currentState.detalleTelefono.trim(),
                    correoElectronico = currentState.detalleCorreo.trim(),
                    direccion = currentState.detalleDireccion.trim(),
                    notasAdicionales = currentState.detalleNotas.trim(),
                    codigoCliente = currentState.detalleCodigoCliente.trim(),
                    imagenPerfil = "",
                    ultimoContacto = "",
                    direccionCompleta = currentState.detalleDireccion.trim(),
                    nombreCompleto = "${currentState.detalleNombre} ${currentState.detalleApellido}".trim(),
                    telefonoFormateado = currentState.detalleTelefono.trim(),
                    cliente = "${currentState.nombre} ${currentState.apellido}"
                )

                when(val result = detalleRepository.createClienteDetalle(nuevoDetalle)) {
                    is Resource.Success -> {
                        val detalleGuardado = result.data ?: nuevoDetalle
                        val detallesActualizados = currentState.detalles.toMutableList().apply {
                            add(detalleGuardado)
                        }

                        _uiState.update {
                            it.copy(
                                isSavingDetalle = false,
                                successMessage = "✅ Detalle agregado exitosamente",
                                showDetalleDialog = false,
                                detalleNombre = "",
                                detalleApellido = "",
                                detalleTelefono = "",
                                detalleCorreo = "",
                                detalleDireccion = "",
                                detalleNotas = "",
                                detalleCodigoCliente = "",
                                detalles = detallesActualizados
                            )
                        }
                        loadClientes()
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isSavingDetalle = false,
                                errorMessage = "❌ Error al guardar detalle: ${result.message}",
                                successMessage = null
                            )
                        }
                    }
                    else -> {
                        _uiState.update {
                            it.copy(
                                isSavingDetalle = false,
                                errorMessage = "❌ Error desconocido al guardar detalle",
                                successMessage = null
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSavingDetalle = false,
                        errorMessage = "❌ Error de conexión: ${e.message}",
                        successMessage = null
                    )
                }
            }
        }
    }

    fun eliminarDetalle(index: Int) {
        val currentState = _uiState.value
        val detalleAEliminar = currentState.detalles.getOrNull(index) ?: return

        if (detalleAEliminar.clienteDetalleId == 0) {
            _uiState.update {
                it.copy(
                    detalles = it.detalles.filterIndexed { i, _ -> i != index },
                    successMessage = "Detalle eliminado"
                )
            }
            return
        }

        viewModelScope.launch {
            try {
                when(val result = detalleRepository.deleteClienteDetalle(detalleAEliminar.clienteDetalleId)) {
                    is Resource.Success -> {
                        val detallesActualizados = currentState.detalles.toMutableList().apply {
                            removeAt(index)
                        }
                        _uiState.update {
                            it.copy(
                                detalles = detallesActualizados,
                                successMessage = "✅ Detalle eliminado exitosamente"
                            )
                        }
                        loadClientes()
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(errorMessage = "❌ Error al eliminar detalle: ${result.message}")
                        }
                    }
                    else -> {
                        _uiState.update {
                            it.copy(errorMessage = "❌ Error desconocido al eliminar detalle")
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "❌ Error de conexión: ${e.message}")
                }
            }
        }
    }

    fun setSearchQuery(query: String) {
        val filtrados = if (query.isBlank()) {
            _uiState.value.clientes
        } else {
            _uiState.value.clientes.filter {
                it.nombre.contains(query, true) ||
                        it.apellido.contains(query, true) ||
                        it.correoElectronico.contains(query, true) ||
                        it.numeroTelefono.contains(query, true)
            }
        }
        _uiState.update {
            it.copy(
                searchQuery = query,
                clientesFiltrados = filtrados
            )
        }
    }

    fun clearForm() {
        _uiState.update {
            ClienteUiState(
                clientes = it.clientes,
                clientesFiltrados = it.clientesFiltrados,
                isLoadingClientes = it.isLoadingClientes,
                errorClientes = it.errorClientes,
                searchQuery = it.searchQuery
            )
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(
                errorMessage = null,
                successMessage = null,
                errorClientes = null
            )
        }
    }
}