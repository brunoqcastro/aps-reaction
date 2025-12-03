package com.reaction.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reaction.data.ReactionRecord
import com.reaction.viewModel.QueryViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun QueryScreen(viewModel: QueryViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = state.idStr,
            onValueChange = { viewModel.setIdStr(it.filter { ch -> ch.isDigit() }) },
            label = { Text("ID da reação") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            viewModel.fetchById { msg ->
                Toast.makeText(context, "Erro: $msg", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Buscar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.result?.let { item ->
            Spacer(modifier = Modifier.height(12.dp))
            Text("Tempo: ${item.reaction_time} ms")
            val parsed = runCatching {
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val dt = sdf.parse(item.timestamp)
                SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(dt)
            }.getOrNull() ?: item.timestamp
            Text("Data: $parsed")
        }

        state.error?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text("Erro: $it")
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "Últimas 10 reações",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.lastReactions) { item ->
                ReactionRow(item)
            }
        }
    }
}

@Composable
private fun ReactionRow(item: ReactionRecord) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(Modifier.padding(12.dp)) {
            Text("Tempo: ${item.reaction_time} ms")
            Text("ID: ${item.id}", style = MaterialTheme.typography.bodySmall)
            Text("Data: ${item.timestamp}", style = MaterialTheme.typography.bodySmall)
        }
    }
}