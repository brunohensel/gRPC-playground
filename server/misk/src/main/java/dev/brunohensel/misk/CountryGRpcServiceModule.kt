package dev.brunohensel.misk

import misk.inject.KAbstractModule
import misk.web.WebActionModule

class CountryGRpcServiceModule : KAbstractModule() {
    override fun configure() {
        install(WebActionModule.create<CountryGRpcAction>())
    }
}
