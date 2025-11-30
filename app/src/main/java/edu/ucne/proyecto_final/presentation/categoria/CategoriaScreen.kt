package edu.ucne.proyecto_final.presentation.categoria

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CategoriaScreen(
    viewModel: CategoriaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        uiState.successMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.primary) }
        uiState.errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = uiState.nombre,
            onValueChange = { viewModel.setNombre(it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = uiState.descripcion,
            onValueChange = { viewModel.setDescripcion(it) },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                if (uiState.categoriaId == 0) viewModel.createCategoria()
                else viewModel.updateCategoria()
            }) {
                Text(if (uiState.categoriaId == 0) "Crear" else "Actualizar")
            }

            if (uiState.categoriaId != 0) {
                Button(onClick = { viewModel.deleteCategoria(uiState.categoriaId) }) {
                    Text("Eliminar")
                }
            }

            Button(onClick = { viewModel.clearForm() }) {
                Text("Limpiar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CategoriaListScreen(
            viewModel = viewModel,
            onEditCategoria = { id ->
                val categoria = uiState.categorias.firstOrNull { it.categoriaId == id }
                categoria?.let { viewModel.setCategoriaForEdit(it) }
            }
        )
    }
}
