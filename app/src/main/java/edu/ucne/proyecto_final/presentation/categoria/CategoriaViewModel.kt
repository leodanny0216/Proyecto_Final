package edu.ucne.proyecto_final.presentation.categoria

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.repository.CategoriaRepository
import edu.ucne.proyecto_final.data.remote.dto.CategoriaDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriaViewModel @Inject constructor(
    private val repository: CategoriaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriaUiState())
    val uiState: StateFlow<CategoriaUiState> = _uiState.asStateFlow()

    init { loadCategorias() }

    fun loadCategorias() {
        viewModelScope.launch {
            repository.getCategorias().collect { resource ->
                when (resource) {
                    is Resource.Success -> _uiState.update {
                        it.copy(
                            categorias = resource.data,
                            categoriasFiltradas = resource.data,
                            isLoadingCategorias = false,
                            errorCategorias = null
                        )
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            categorias = emptyList(),
                            categoriasFiltradas = emptyList(),
                            isLoadingCategorias = false,
                            errorCategorias = resource.message
                        )
                    }
                    is Resource.Loading -> _uiState.update {
                        it.copy(isLoadingCategorias = true, errorCategorias = null)
                    }
                }
            }
        }
    }

    fun createCategoria() {
        val current = _uiState.value
        if (current.nombre.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre es requerido") }
            return
        }

        _uiState.update { it.copy(isCreating = true, errorMessage = null) }

        viewModelScope.launch {
            val nuevaCategoria = CategoriaDto(
                categoriaId = 0,
                nombre = current.nombre,
                descripcion = current.descripcion
            )
            when (val result = repository.createCategoria(nuevaCategoria)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isCreating = false,
                            successMessage = "Categoría creada exitosamente",
                            nombre = "",
                            descripcion = ""
                        )
                    }
                    loadCategorias()
                }
                is Resource.Error -> _uiState.update {
                    it.copy(isCreating = false, errorMessage = result.message)
                }
                else -> {}
            }
        }
    }

    fun updateCategoria() {
        val current = _uiState.value
        if (current.nombre.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre es requerido") }
            return
        }

        _uiState.update { it.copy(isUpdating = true, errorMessage = null) }

        viewModelScope.launch {
            val categoriaActualizada = CategoriaDto(
                categoriaId = current.categoriaId,
                nombre = current.nombre,
                descripcion = current.descripcion
            )
            when (val result = repository.updateCategoria(current.categoriaId, categoriaActualizada)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            successMessage = "Categoría actualizada exitosamente"
                        )
                    }
                    loadCategorias()
                }
                is Resource.Error -> _uiState.update {
                    it.copy(isUpdating = false, errorMessage = result.message)
                }
                else -> {}
            }
        }
    }

    fun deleteCategoria(id: Int) {
        _uiState.update { it.copy(isDeleting = true, errorMessage = null) }

        viewModelScope.launch {
            when (val result = repository.deleteCategoria(id)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            successMessage = "Categoría eliminada exitosamente"
                        )
                    }
                    loadCategorias()
                }
                is Resource.Error -> _uiState.update {
                    it.copy(isDeleting = false, errorMessage = result.message)
                }
                else -> {}
            }
        }
    }

    fun setNombre(nombre: String) = _uiState.update { it.copy(nombre = nombre, errorMessage = null) }
    fun setDescripcion(descripcion: String) = _uiState.update { it.copy(descripcion = descripcion, errorMessage = null) }

    fun setCategoriaForEdit(categoria: CategoriaDto) {
        _uiState.update {
            it.copy(
                categoriaId = categoria.categoriaId,
                nombre = categoria.nombre,
                descripcion = categoria.descripcion,
                categoriaSeleccionada = categoria,
                errorMessage = null
            )
        }
    }

    fun clearForm() {
        _uiState.update {
            it.copy(
                categoriaId = 0,
                nombre = "",
                descripcion = "",
                categoriaSeleccionada = null,
                errorMessage = null
            )
        }
    }

    fun setSearchQuery(query: String) {
        val filtradas = if (query.isBlank()) _uiState.value.categorias
        else _uiState.value.categorias.filter {
            it.nombre.contains(query, ignoreCase = true) ||
                    it.categoriaId.toString().contains(query)
        }
        _uiState.update { it.copy(searchQuery = query, categoriasFiltradas = filtradas) }
    }

    fun clearMessages() = _uiState.update { it.copy(successMessage = null, errorMessage = null) }
}
