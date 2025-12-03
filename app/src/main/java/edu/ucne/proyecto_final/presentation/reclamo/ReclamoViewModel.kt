// package edu.ucne.proyecto_final.presentation.reclamo
package edu.ucne.proyecto_final.presentation.reclamo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.remote.dto.ReclamoDto
import edu.ucne.proyecto_final.data.repository.ReclamoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ReclamoViewModel @Inject constructor(
    private val reclamoRepository: ReclamoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReclamoUiState())
    val uiState: StateFlow<ReclamoUiState> = _uiState.asStateFlow()

    private val _reclamosState = MutableStateFlow<Resource<List<ReclamoDto>>>(Resource.Loading())
    val reclamosState: StateFlow<Resource<List<ReclamoDto>>> = _reclamosState.asStateFlow()

    init {
        loadReclamos()
    }

    fun loadReclamos() {
        viewModelScope.launch {
            reclamoRepository.getReclamos().collect { resource ->
                _reclamosState.value = resource
                when(resource) {
                    is Resource.Success -> _uiState.update {
                        it.copy(
                            reclamos = resource.data ?: emptyList(),
                            reclamosFiltrados = resource.data ?: emptyList(),
                            isLoadingReclamos = false,
                            errorReclamos = null
                        )
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isLoadingReclamos = false,
                            errorReclamos = resource.message,
                            reclamos = emptyList(),
                            reclamosFiltrados = emptyList()
                        )
                    }
                    is Resource.Loading -> _uiState.update {
                        it.copy(isLoadingReclamos = true, errorReclamos = null)
                    }
                }
            }
        }
    }

    fun createReclamo() {
        val currentState = _uiState.value

        // Validaciones
        if (currentState.tipoReclamoId == 0) {
            _uiState.update { it.copy(errorMessage = "Debe seleccionar un tipo de reclamo") }
            return
        }

        if (currentState.fechaIncidente.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Debe seleccionar una fecha del incidente") }
            return
        }

        if (currentState.descripcion.length < 10) {
            _uiState.update { it.copy(errorMessage = "La descripción debe tener al menos 10 caracteres") }
            return
        }

        _uiState.update { it.copy(isCreating = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val nuevoReclamo = ReclamoDto(
                    reclamoId = 0,
                    tipoReclamoId = currentState.tipoReclamoId,
                    tipoReclamo = currentState.tipoReclamo,
                    fechaIncidente = currentState.fechaIncidente,
                    descripcion = currentState.descripcion,
                    evidencias = currentState.evidencias
                )

                when(val result = reclamoRepository.createReclamo(nuevoReclamo)) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isCreating = false,
                                successMessage = "Reclamo creado exitosamente",
                                errorMessage = null,
                                // Limpiar formulario
                                tipoReclamoId = 0,
                                tipoReclamo = "",
                                fechaIncidente = "",
                                descripcion = "",
                                evidencias = emptyList()
                            )
                        }
                        loadReclamos()
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isCreating = false,
                            errorMessage = result.message ?: "Error al crear reclamo",
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

    fun updateReclamo() {
        val currentState = _uiState.value

        if (currentState.reclamoId == 0) {
            _uiState.update { it.copy(errorMessage = "No hay reclamo seleccionado para actualizar") }
            return
        }

        if (currentState.descripcion.length < 10) {
            _uiState.update { it.copy(errorMessage = "La descripción debe tener al menos 10 caracteres") }
            return
        }

        _uiState.update { it.copy(isUpdating = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val reclamoActualizado = ReclamoDto(
                    reclamoId = currentState.reclamoId,
                    tipoReclamoId = currentState.tipoReclamoId,
                    tipoReclamo = currentState.tipoReclamo,
                    fechaIncidente = currentState.fechaIncidente,
                    descripcion = currentState.descripcion,
                    evidencias = currentState.evidencias
                )

                when(val result = reclamoRepository.updateReclamo(currentState.reclamoId, reclamoActualizado)) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isUpdating = false,
                                successMessage = "Reclamo actualizado exitosamente",
                                errorMessage = null
                            )
                        }
                        clearForm()
                        loadReclamos()
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isUpdating = false,
                            errorMessage = result.message ?: "Error al actualizar reclamo"
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

    fun deleteReclamo(reclamoId: Int) {
        _uiState.update { it.copy(isDeleting = true, errorMessage = null) }

        viewModelScope.launch {
            when(val result = reclamoRepository.deleteReclamo(reclamoId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            successMessage = "Reclamo eliminado exitosamente"
                        )
                    }
                    loadReclamos()
                }
                is Resource.Error -> _uiState.update {
                    it.copy(
                        isDeleting = false,
                        errorMessage = result.message ?: "Error al eliminar reclamo"
                    )
                }
                else -> {}
            }
        }
    }

    fun setReclamoForEdit(reclamo: ReclamoDto) {
        _uiState.update {
            it.copy(
                reclamoId = reclamo.reclamoId,
                tipoReclamoId = reclamo.tipoReclamoId,
                tipoReclamo = reclamo.tipoReclamo,
                fechaIncidente = reclamo.fechaIncidente,
                descripcion = reclamo.descripcion,
                evidencias = reclamo.evidencias,
                reclamoSeleccionado = reclamo
            )
        }
    }

    fun setTipoReclamo(tipoId: Int, tipoNombre: String) {
        _uiState.update {
            it.copy(
                tipoReclamoId = tipoId,
                tipoReclamo = tipoNombre
            )
        }
    }

    fun setFechaIncidente(fecha: String) {
        _uiState.update { it.copy(fechaIncidente = fecha) }
    }

    fun setDescripcion(descripcion: String) {
        _uiState.update { it.copy(descripcion = descripcion) }
    }

    fun addEvidencia(url: String) {
        val nuevasEvidencias = _uiState.value.evidencias.toMutableList()
        nuevasEvidencias.add(url)
        _uiState.update { it.copy(evidencias = nuevasEvidencias) }
    }

    fun removeEvidencia(index: Int) {
        val nuevasEvidencias = _uiState.value.evidencias.toMutableList()
        nuevasEvidencias.removeAt(index)
        _uiState.update { it.copy(evidencias = nuevasEvidencias) }
    }

    fun clearForm() {
        _uiState.update {
            it.copy(
                reclamoId = 0,
                tipoReclamoId = 0,
                tipoReclamo = "",
                fechaIncidente = "",
                descripcion = "",
                evidencias = emptyList(),
                reclamoSeleccionado = null,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun setSearchQuery(query: String) {
        val filtrados = if(query.isBlank()) {
            _uiState.value.reclamos
        } else {
            _uiState.value.reclamos.filter {
                it.descripcion.contains(query, ignoreCase = true) ||
                        it.tipoReclamo.contains(query, ignoreCase = true) ||
                        it.fechaIncidente.contains(query, ignoreCase = true)
            }
        }
        _uiState.update { it.copy(searchQuery = query, reclamosFiltrados = filtrados) }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}