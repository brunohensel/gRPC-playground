package dev.brunohensel.app

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import dev.brunohensel.app.network.Country

@Composable
fun CountryScreen(
    countries: List<Country>,
    modifier: Modifier = Modifier,
    onLoadMore: () -> Unit,
) {
    val listState = rememberLazyListState()

    val bottom: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != 0 && lastVisibleItem?.index == listState.layoutInfo.totalItemsCount / 2
        }
    }

    LaunchedEffect(bottom) {
        if (bottom) onLoadMore()
    }

    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        items(countries, key = { country -> country.name }) { country ->
            CountryComponent(country = country)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun CountryComponent(
    country: Country,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 8)
            .padding(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier

        ) {
            AsyncImage(
                modifier = Modifier.fillMaxWidth(0.5f),
                model = ImageRequest.Builder(LocalContext.current).data(country.flag).build(),
                contentDescription = "image of ${country.name}'s flag",
                contentScale = ContentScale.FillBounds,
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp)
            ) {
                CountryInfoRow(prefixText = "Country:", suffixText = country.name)
                Spacer(modifier = Modifier.height(4.dp))
                CountryInfoRow(prefixText = "Capital:", suffixText = country.capital)
                Spacer(modifier = Modifier.height(4.dp))
                val popFormatted = formatPopulation(country.population)
                CountryInfoRow(prefixText = "Population:", suffixText = popFormatted)
                Spacer(modifier = Modifier.height(4.dp))
                CountryInfoRow(prefixText = "Region:", suffixText = country.region)
            }
        }
    }
}

private const val BILLION = 1_000_000_000.0
private const val MILLION = 1_000_000.0
private const val THOUSAND = 1_000.0

@SuppressLint("DefaultLocale")
private fun formatPopulation(population: Long): String {
    check(population > 0) { "Population must be grater than 0. Received it as :$population" }

    return when {
        population >= BILLION -> String.format("%.1fB", population / BILLION)
        population >= MILLION -> String.format("%.1fM", population / MILLION)
        else -> String.format("%.1fK", population / THOUSAND)
    }
}

@Composable
private fun CountryInfoRow(
    prefixText: String,
    suffixText: String,
) {
    Row {
        Text(text = prefixText, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = suffixText)
    }
}
