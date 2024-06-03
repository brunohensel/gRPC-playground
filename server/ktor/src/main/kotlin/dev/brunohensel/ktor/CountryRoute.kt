package dev.brunohensel.ktor

import io.ktor.resources.Resource
import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import kotlinx.serialization.Serializable
import java.io.File

@Resource("/countries")
class CountriesRoute

@Serializable
data class CountryDTO(
    val name: String,
    val capital: String,
    val flag: String,
    val region: String,
    val subregion: String,
    val population: Long,
    val latlng: List<Float>,
)

fun Route.createCountryRouting() {
    get<CountriesRoute> {
        call.respond(CountriesRepository.countries)
    }
}

object CountriesRepository {
    val countries: List<CountryDTO>
        get() = getCountriesList()

    private var cacheCountries: List<CountryDTO> = emptyList()

    private fun getCountriesList(): List<CountryDTO> {
        if (cacheCountries.isNotEmpty()) return cacheCountries
        val jsonPath = "./server/src/main/resources/countries.json"
        val file = File(jsonPath)
        val jsonString = file.readText()
        val countries = DefaultJson.decodeFromString<List<CountryDTO>>(jsonString)
        cacheCountries = countries
        return cacheCountries
    }
}
