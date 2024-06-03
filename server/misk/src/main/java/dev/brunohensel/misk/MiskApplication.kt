package dev.brunohensel.misk

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import misk.MiskApplication
import misk.MiskRealServiceModule
import misk.config.Config
import misk.config.ConfigModule
import misk.config.MiskConfig
import misk.environment.DeploymentModule
import misk.web.MiskWebModule
import misk.web.WebConfig
import org.slf4j.LoggerFactory
import wisp.deployment.getDeploymentFromEnvironmentVariable

fun main(args: Array<String>) {
    CountryLogging.configure()
    val deployment = getDeploymentFromEnvironmentVariable()
    val config = MiskConfig.load<CountryConfig>("country", deployment)

    MiskApplication(
        MiskRealServiceModule(),
        MiskWebModule(config.web),
        CountryGRpcServiceModule(),
        ConfigModule.create("country", config),
        DeploymentModule(deployment),
    ).run(args)
}

object CountryLogging {
    private val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
    private val rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME)

    fun configure() {
        rootLogger.level = Level.INFO
        installUncaughtExceptionHandler()
    }

    /** Don't dump uncaught exceptions to System.out; format them properly with logging. */
    private fun installUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            rootLogger.error("Uncaught exception!", throwable)
        }
    }
}

data class CountryConfig(val web: WebConfig) : Config
