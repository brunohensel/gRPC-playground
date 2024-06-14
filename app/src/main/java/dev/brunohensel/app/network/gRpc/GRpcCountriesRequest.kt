package dev.brunohensel.app.network.gRpc

import com.squareup.wire.GrpcClient
import dev.brunohensel.app.network.CountriesRequest
import dev.brunohensel.app.network.Country
import dev.brunohensel.app.network.WrappedCountry
import dev.brunohensel.country.GetCountriesServiceClient
import dev.brunohensel.country.Pagination
import kotlin.time.TimeSource

class GRpcCountriesRequest(
    private val grpcClient: GrpcClient = GrpcClientProvider.grpcClient
) : CountriesRequest {

    override suspend fun getCountries(): WrappedCountry {
        val startTime = TimeSource.Monotonic.markNow()

        val response = grpcClient.create<GetCountriesServiceClient>()
            .GetCountries()
            .execute(Unit)

        val finishedIn = startTime.elapsedNow()
        val countries = response.countries.map(::Country)

        return WrappedCountry(countries, finishedIn)
    }

    override suspend fun getCountries(pagination: Pagination): WrappedCountry {
        val startTime = TimeSource.Monotonic.markNow()

        val response = grpcClient.create<GetCountriesServiceClient>()
            .GetPagedCountries()
            .execute(pagination)

        val finishedIn = startTime.elapsedNow()
        val countries = response.countries.map(::Country)

        return WrappedCountry(countries, finishedIn)
    }
}
