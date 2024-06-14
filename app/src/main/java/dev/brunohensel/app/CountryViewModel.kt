package dev.brunohensel.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.brunohensel.app.network.CountriesRequest
import dev.brunohensel.app.network.Country
import dev.brunohensel.app.network.gRpc.GRpcCountriesRequest
import dev.brunohensel.app.network.rest.KtorCountriesRequest
import dev.brunohensel.country.Pagination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CountryViewModel(
    private val countryRestRequest: CountriesRequest,
    private val countryGRpcRequest: CountriesRequest,
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()
    private val page: (ProtocolType) -> Int = sourcePage()
    fun loadMore() {
        val type = state.value.protocolType
        checkNotNull(type) { "Type must not be null" }
        getCountries(type)
    }

    fun getCountries(protocolOption: ProtocolType) {
        val source = when (protocolOption) {
            ProtocolType.REST -> countryRestRequest
            ProtocolType.GRPC -> countryGRpcRequest
        }
        viewModelScope.launch {
            val pagination = Pagination(per_page = 20, page = page(protocolOption))
            val wrappedCountry = source.getCountries(pagination)
            _state.update { oldState ->
                oldState.copy(
                    countries = wrappedCountry.countries,
                    restDurationStr = if (protocolOption == ProtocolType.REST) "${wrappedCountry.serialisationDuration}" else oldState.restDurationStr,
                    gRPCDurationStr = if (protocolOption == ProtocolType.GRPC) "${wrappedCountry.serialisationDuration}" else oldState.gRPCDurationStr,
                    protocolType = protocolOption,
                )
            }
        }
    }

    private fun sourcePage(): (ProtocolType) -> Int {
        var gRPCPage = 0
        var restPage = 0
        return { type ->
            when (type) {
                ProtocolType.REST -> ++restPage
                ProtocolType.GRPC -> ++gRPCPage
            }
        }
    }

    data class State(
        val countries: List<Country> = emptyList(),
        val restDurationStr: String = "",
        val gRPCDurationStr: String = "",
        val protocolType: ProtocolType? = null
    )

    companion object {

        enum class ProtocolType(val typeName: String) {
            REST("REST"),
            GRPC("gRPC")
        }

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val countryRestRequest: CountriesRequest = KtorCountriesRequest()
                val countryGRpcRequest: CountriesRequest = GRpcCountriesRequest()
                return CountryViewModel(countryRestRequest, countryGRpcRequest) as T
            }
        }
    }
}
