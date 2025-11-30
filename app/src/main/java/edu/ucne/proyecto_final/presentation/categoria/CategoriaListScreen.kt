package edu.ucne.proyecto_final.presentation.categoria

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CategoriaListScreen(
    viewModel: CategoriaViewModel = hiltViewModel(),
    onEditCategoria: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = uiState.searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            label = { Text("Buscar categoría") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.isLoadingCategorias) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else if (uiState.errorCategorias != null) {
            Text(text = uiState.errorCategorias!!, color = MaterialTheme.colorScheme.error)
        } else {
            LazyColumn {
                items(uiState.categoriasFiltradas) { categoria ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { onEditCategoria(categoria.categoriaId) },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = categoria.nombre)
                        Text(text = categoria.descripcion)
                    }
                }
            }
        }
    }
}
