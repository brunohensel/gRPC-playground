package dev.brunohensel.app.network.gRpc

import com.squareup.wire.GrpcClient
import dev.brunohensel.app.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Protocol
import java.util.concurrent.TimeUnit

object GrpcClientProvider {

    private val okHttpClient = OkHttpClient.Builder()
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES)
        .callTimeout(1, TimeUnit.MINUTES)
        .protocols(listOf(Protocol.HTTP_1_1, Protocol.HTTP_2))
        .build()

    val grpcClient: GrpcClient = GrpcClient.Builder()
        .client(okHttpClient)
        .baseUrl("http://${BuildConfig.LOCAL_HOST}:8081")
        .build()
}
