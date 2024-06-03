package dev.brunohensel.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.memory.MemoryCache
import coil3.request.crossfade
import coil3.util.DebugLogger
import dev.brunohensel.app.CountryViewModel.Companion.ProtocolType
import dev.brunohensel.app.CountryViewModel.Companion.ProtocolType.GRPC
import dev.brunohensel.app.CountryViewModel.Companion.ProtocolType.REST
import dev.brunohensel.app.ui.theme.MyTheme

class MainActivity : ComponentActivity() {

    private val viewModel: CountryViewModel by viewModels { CountryViewModel.Factory }

    @OptIn(ExperimentalCoilApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            setSingletonImageLoaderFactory { context ->
                newImageLoader(context)
            }

            val state by viewModel.state.collectAsState()

            MyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        ApiProtocolOptions(
                            restDuration = state.restDurationStr,
                            gRPCDuration = state.gRPCDurationStr,
                        ) { option ->
                            viewModel.getCountries(option)
                        }
                        CountryScreen(state.countries, Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
private fun ApiProtocolOptions(
    restDuration: String,
    gRPCDuration: String,
    onClick: (ProtocolType) -> Unit,
) {
    var selectionState by remember { mutableStateOf("") }

    Row(Modifier.fillMaxWidth()) {
        ProtocolTypeOption(
            selected = REST.typeName == selectionState,
            duration = restDuration,
            type = REST,
            onClick = {
                onClick(REST)
                selectionState = REST.typeName
            }
        )
        ProtocolTypeOption(
            selected = GRPC.typeName == selectionState,
            duration = gRPCDuration,
            type = GRPC,
            onClick = {
                onClick(GRPC)
                selectionState = GRPC.typeName
            }
        )
    }
}

@Composable
private fun RowScope.ProtocolTypeOption(
    selected: Boolean,
    duration: String,
    type: ProtocolType,
    onClick: (ProtocolType) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .weight(2f)
    ) {
        Row(
            Modifier
                .padding(horizontal = 8.dp)
        ) {
            RadioButton(
                selected = selected,
                onClick = { onClick(type) },
            )
            Text(
                text = type.typeName,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
            )

        }
        Text(
            text = if (duration.isEmpty()) "" else "Took: $duration",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(start = 16.dp)
        )
    }
}

private fun newImageLoader(context: PlatformContext): ImageLoader {
    return ImageLoader.Builder(context)
        .memoryCache {
            MemoryCache.Builder()
                // Set the max size to 25% of the app's available memory.
                .maxSizePercent(context, percent = 0.25)
                .build()
        }
        .crossfade(true)
        .apply {
            logger(DebugLogger())
        }
        .build()
}
