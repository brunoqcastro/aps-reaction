package com.reaction.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reaction.viewModel.ConfigViewModel

@Composable
fun ConfigScreen(viewModel: ConfigViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadConfig()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(text = "Configuração de tempo (ms)")

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = state.minMsStr,
            onValueChange = { viewModel.setMinStr(it.filter { ch -> ch.isDigit() }) },
            label = { Text("Min (ms)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.maxMsStr,
            onValueChange = { viewModel.setMaxStr(it.filter { ch -> ch.isDigit() }) },
            label = { Text("Max (ms)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = {
                // load
                viewModel.loadConfig()
                Toast.makeText(context, "Buscando valores...", Toast.LENGTH_SHORT).show()
            }) {
                Text("Pegar configurações")
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = {
                    viewModel.updateConfig(
                        onSuccess = {
                            Toast.makeText(context, "Valores atualizados", Toast.LENGTH_SHORT).show()
                        },
                        onError = { msg ->
                            Toast.makeText(context, "Erro: $msg", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                enabled = viewModel.canUpdate()
            ) {
                Text("Atualizar")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Erro: $it")
        }
    }
}
