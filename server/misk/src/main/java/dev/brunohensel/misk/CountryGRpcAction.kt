package dev.brunohensel.misk

import dev.brunohensel.country.Country
import dev.brunohensel.country.CountryResponse
import dev.brunohensel.country.GetCountriesServiceGetCountriesBlockingServer
import dev.brunohensel.country.GetCountriesServiceGetPagedCountriesBlockingServer
import dev.brunohensel.country.Pagination
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import misk.web.actions.WebAction
import java.io.File

class CountryGRpcAction : WebAction,
    GetCountriesServiceGetCountriesBlockingServer,
    GetCountriesServiceGetPagedCountriesBlockingServer {

    private val countries: List<CountryDTO>
        get() = getCountriesList()

    override fun GetCountries(request: Unit): CountryResponse {
        val countries = countries.map { dto ->
            Country(
                name = dto.name,
                capital = dto.capital,
                flag = dto.flag,
                region = dto.region,
                subregion = dto.subregion,
                population = dto.population,
                latlng = dto.latlng,
            )
        }

        return CountryResponse(countries)
    }

    override fun GetPagedCountries(request: Pagination): CountryResponse {
        val take = request.page.times(request.per_page).coerceAtLeast(20)

        val countries = countries.take(take).map { dto ->
            Country(
                name = dto.name,
                capital = dto.capital,
                flag = dto.flag,
                region = dto.region,
                subregion = dto.subregion,
                population = dto.population,
                latlng = dto.latlng,
            )
        }
        return CountryResponse(countries.take(take))
    }

    private var cacheCountries: List<CountryDTO> = emptyList()
    private fun getCountriesList(): List<CountryDTO> {
        if (cacheCountries.isNotEmpty()) return cacheCountries
        val jsonPath = "./server/src/main/resources/countries.json"
        val file = File(jsonPath)
        val jsonString = file.readText()
        val countries = Json.decodeFromString<List<CountryDTO>>(jsonString)
        cacheCountries = countries
        return cacheCountries
    }

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
}
