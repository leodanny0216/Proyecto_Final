package edu.ucne.proyecto_final.presentation.cliente

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
fun ClienteListScreen(
    viewModel: ClienteViewModel = hiltViewModel(),
    onEditCliente: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = uiState.searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            label = { Text("Buscar cliente") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.isLoadingClientes) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else if (uiState.errorClientes != null) {
            Text(text = uiState.errorClientes!!, color = MaterialTheme.colorScheme.error)
        } else {
            LazyColumn {
                items(uiState.clientesFiltrados) { cliente ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { onEditCliente(cliente.clienteId) },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "${cliente.nombre} ${cliente.apellido}")
                            Text(text = cliente.correoElectronico)
                        }
                    }
                }
            }
        }
    }
}
