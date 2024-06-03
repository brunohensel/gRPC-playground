package dev.brunohensel.app.network.rest

import dev.brunohensel.app.network.CountriesRequest
import dev.brunohensel.app.network.Country
import dev.brunohensel.app.network.WrappedCountry
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.resources.Resource
import kotlinx.serialization.Serializable
import kotlin.time.TimeSource

@Serializable
@Resource("/countries")
private class CountriesResourceRoute

class KtorCountriesRequest(
    private val httpClient: HttpClient = KtorHttpClientProvider.httpClient,
) : CountriesRequest {

    override suspend fun getCountries(): WrappedCountry {
        val startTime = TimeSource.Monotonic.markNow()

        val countries: List<Country> = httpClient.get(CountriesResourceRoute()).body()

        val finishedIn = startTime.elapsedNow()

        return WrappedCountry(countries, finishedIn)
    }
}
