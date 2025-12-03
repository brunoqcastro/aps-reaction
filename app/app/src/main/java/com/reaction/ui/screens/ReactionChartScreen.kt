package com.reaction.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.reaction.data.ReactionRecord
import com.reaction.viewModel.ChartViewModel

@Composable
fun ReactionChartScreen(viewModel: ChartViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()

    if (state.error != null) {
        Text("Erro: ${state.error}")
        return
    }

    if (state.items.isEmpty()) {
        Text("Nenhum dado para exibir")
        return
    }

    ReactionChart(state.items)
}

@Composable
fun ReactionChart(list: List<ReactionRecord>) {
    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            lineSeries { series(list.map { it.reaction_time }) }
        }
    }

    if (list.isEmpty()) {
        Column {
            Text("Nenhum registro encontrado", modifier = Modifier.padding(8.dp))
        }
    } else {
        Column(
            Modifier.padding(16.dp)
        ) {
            Text(
                "Grafico das últimas 10 reações",
                style = MaterialTheme.typography.titleLarge,
                textDecoration = TextDecoration.Underline
            )
            CartesianChartHost(
                rememberCartesianChart(
                    rememberLineCartesianLayer(),
                    startAxis = VerticalAxis.rememberStart(),
                    bottomAxis = HorizontalAxis.rememberBottom(),
                ),
                modelProducer,
            )
        }
    }

}

