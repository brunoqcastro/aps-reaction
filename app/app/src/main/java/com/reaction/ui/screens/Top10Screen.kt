@file:OptIn(ExperimentalMaterial3Api::class)

package com.reaction.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reaction.ui.theme.ReactionTheme
import com.reaction.viewModel.Top10ViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun Top10Screen(viewModel: Top10ViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    val isRefreshing = state.isRefreshing

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.loadTop10() },
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (state.items.isEmpty() && !isRefreshing) {
            Text("Nenhum registro encontrado", modifier = Modifier.padding(8.dp))
            Button(onClick = viewModel::loadTop10) {
                Text("Atualizar", modifier = Modifier.padding(8.dp))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(state.items) { index, item ->
                    Top10Row(
                        position = index + 1,
                        item = item
                    )
                }
            }
        }
    }
}

@Composable
fun Top10Row(
    position: Int,
    item: com.reaction.data.ReactionRecord
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "$position",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(all = 16.dp)
                    .width(40.dp)
            )

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = "${item.reaction_time} ms",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                val parsed = runCatching {
                    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    val dt = sdf.parse(item.timestamp)
                    SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(dt)
                }.getOrNull() ?: item.timestamp

                Text(
                    text = parsed,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
