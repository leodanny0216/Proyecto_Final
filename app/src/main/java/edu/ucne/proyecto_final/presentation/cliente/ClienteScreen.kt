package edu.ucne.proyecto_final.presentation.cliente

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ClienteScreen(
    viewModel: ClienteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        uiState.successMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.primary) }
        uiState.errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(value = uiState.nombre, onValueChange = { viewModel.setNombre(it) }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = uiState.apellido, onValueChange = { viewModel.setApellido(it) }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = uiState.numeroTelefono, onValueChange = { viewModel.setNumeroTelefono(it) }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = uiState.correoElectronico, onValueChange = { viewModel.setCorreoElectronico(it) }, label = { Text("Correo electrónico") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = uiState.direccion, onValueChange = { viewModel.setDireccion(it) }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { if (uiState.clienteId == 0) viewModel.createCliente() else viewModel.updateCliente() }) {
                Text(if (uiState.clienteId == 0) "Crear" else "Actualizar")
            }

            if (uiState.clienteId != 0) {
                Button(onClick = { viewModel.deleteCliente(uiState.clienteId) }) { Text("Eliminar") }
            }

            Button(onClick = { viewModel.clearForm() }) { Text("Limpiar") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ClienteListScreen(viewModel = viewModel, onEditCliente = { id ->
            val cliente = uiState.clientes.firstOrNull { it.clienteId == id }
            cliente?.let { viewModel.setClienteForEdit(it) }
        })
    }
}
