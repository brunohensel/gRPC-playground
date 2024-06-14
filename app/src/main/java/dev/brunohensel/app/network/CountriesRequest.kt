package dev.brunohensel.app.network

import dev.brunohensel.country.Pagination
import kotlinx.serialization.Serializable
import kotlin.time.Duration

fun interface CountriesRequest {
    suspend fun getCountries(): WrappedCountry
    suspend fun getCountries(pagination: Pagination): WrappedCountry {
        return getCountries()
    }
}

data class WrappedCountry(
    val countries: List<Country> = emptyList(),
    val serialisationDuration: Duration = Duration.ZERO,
)

@Serializable
data class Country(
    val name: String,
    val capital: String,
    val flag: String,
    val region: String,
    val subregion: String,
    val population: Long,
    val latlng: List<Float>,
) {

    constructor(country: dev.brunohensel.country.Country) : this(
        name = country.name,
        capital = country.capital,
        flag = country.flag,
        region = country.region,
        subregion = country.subregion,
        population = country.population,
        latlng = country.latlng,
    )
}
