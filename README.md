# gRPC Playground 

![kotlin-version](https://img.shields.io/badge/kotlin-2.0.0-blue?logo=kotlin)


### Description
gRPC Playground is a sample project designed to explore the functionalities of [gRPC](https://grpc.io/). The project is structured as a monorepo with client and server implementations and serves as a hands-on resource for grasping the initial implementation details to work with gRPC.


### Project Structure

* **:app project** Contains a very simple Android application for displaying a list of countries on the screen. It also handles a rather naive attempt to benchmark the time elapsed for deserialize a payload into a data type.
* **:proto project** Includes protocol buffer definitions used to create/implement a Country service. The code generated is baked by [Wire](https://github.com/square/wire)
* **:server:ktor project** Contains an implementation of a REST API using [Ktor](https://ktor.io/) library.   
* **:server:misk project** Contains an implementation of a gRPC API using [Misk](https://github.com/cashapp/misk) library.  

### Observations

**The servers have to be running before the Android App make any requests.**

**Local host address**
All servers run locally, Ktor on port 8080 and Misk on port 8081, and the Android application connects with the servers through the local host IP address.
All you have to do is to create a property `LOCAL_HOST` inside of the `local.properties` file, and pass you local IP address there.
```
LOCAL_HOST="XXX.XXX.XXX.XX"
```

The command `./gradlew server:misk:run` or `./gradlew server:ktor:run` are enough to get the servers up and running, but it will not be possible to read a file where the JSON for the countries list is stored.

To work around this problem, choose a configuration (KtorApplicationkt or MiskApplicationkt) and then click on the ▶️ button on Android Studio. You can also hit the ▶️ button for the `main` functions in the `KtorApplication.kt` or `MiskApplication.kt`

If you run the servers and you get the `java.net.BindException: Address already in use` you have to stop the server. If it's not possible to do that via IDE, you can do it via CLI:
* lsof -i :8080 - will list the process using the port 8080
* sudo kill -9 <PID number> - will kill the process for the PID number you pass
