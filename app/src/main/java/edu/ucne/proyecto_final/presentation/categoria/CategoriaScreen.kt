package edu.ucne.proyecto_final.presentation.categoria

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CategoriaScreen(viewModel: CategoriaViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE8F5E9), // Verde muy suave
                        Color.White
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // --- Mensajes de estado ---
            uiState.successMessage?.let {
                Text(
                    text = it,
                    color = Color(0xFF2E7D32), // Verde oscuro
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = Color(0xFFD32F2F),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // --- Card para el formulario ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextField(
                        value = uiState.nombre,
                        onValueChange = { viewModel.setNombre(it) },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth(),

                    )

                    TextField(
                        value = uiState.descripcion,
                        onValueChange = { viewModel.setDescripcion(it) },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth(),

                    )

                    // --- Botones principales ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                if (uiState.categoriaId == 0) viewModel.createCategoria()
                                else viewModel.updateCategoria()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = if (uiState.categoriaId == 0) "Crear" else "Actualizar",
                                color = Color.White
                            )
                        }

                        if (uiState.categoriaId != 0) {
                            Button(
                                onClick = { viewModel.deleteCategoria(uiState.categoriaId) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Eliminar", color = Color.White)
                            }
                        }

                        Button(
                            onClick = { viewModel.clearForm() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784)),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Limpiar", color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Lista de categorías ---
            CategoriaListScreen(
                viewModel = viewModel,
                onEditCategoria = { id ->
                    val categoria = uiState.categorias.firstOrNull { it.categoriaId == id }
                    categoria?.let { viewModel.setCategoriaForEdit(it) }
                }
            )
        }
    }
}
