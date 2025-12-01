@file:OptIn(ExperimentalMaterial3Api::class)

package com.reaction.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reaction.viewModel.Top10ViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun Top10Screen(viewModel: Top10ViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()

    val isRefreshing = state.isRefreshing

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.loadTop10()
        },
        modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        if (state.items.isEmpty() && !isRefreshing) {
            Text("Nenhum registro encontrado", modifier = Modifier.padding(8.dp))
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.items) { item ->
                    Top10Row(item)
                }
            }
        }
    }
}

@Composable
private fun Top10Row(item: com.reaction.data.ReactionRecord) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
        Text("Tempo: ${item.reaction_time} ms")
        val parsed = runCatching {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val dt = sdf.parse(item.timestamp)
            SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(dt)
        }.getOrNull() ?: item.timestamp
        Text("Data: $parsed")
        HorizontalDivider()
    }
}
