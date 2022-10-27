package com.equisoft.awsmocks.common.interfaces.http

import com.amazonaws.AmazonWebServiceRequest
import io.ktor.http.Parameters
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.log
import io.ktor.server.request.receiveParameters
import io.ktor.util.StringValues
import io.ktor.util.filter
import io.ktor.util.flattenEntries

private const val ACTION_NAME_PARAM = "Action"

interface ParametersDeserializer {
    fun addParameters(parameters: Parameters, request: AmazonWebServiceRequest): AmazonWebServiceRequest
}

class FormRequestFactory(
    private val parametersDeserializer: ParametersDeserializer,
    private val rootPackage: String
) {
    suspend fun create(call: ApplicationCall): AmazonWebServiceRequest {
        val parameters: Parameters = call.receiveParameters().also { logRequest(call, it) }
        val actionName: String = checkNotNull(parameters[ACTION_NAME_PARAM])
        val klass: Class<*> = Class.forName("$rootPackage.${actionName}Request")

        val request: AmazonWebServiceRequest = klass.getDeclaredConstructor().newInstance() as AmazonWebServiceRequest
        request.parseAuthorization(call.request)

        return parametersDeserializer.addParameters(parameters, request)
    }

    private fun logRequest(call: ApplicationCall, parameters: Parameters) {
        call.application.log.debug(parameters.toString())
    }
}

fun StringValues.values(): List<String> = entries().flatMap { it.value }

fun Parameters.toList(name: String): List<String> = filter { key, _ -> key.contains("$name.", true) }.values()

/**
 * This is used to transform

 * $name.1.$keyName="param-name"
 * $name.1.$valueName.1="value-1"
 * $name.1.$valueName.2="value-2"
 * $name.2.$keyName="param-name-2"
 * $name.2.$valueName.1="value-3"
 *
 * into a map of ("param-name" to ["value-1", "value-2"], "param-name-2" to ["value-3"])
 */
fun Parameters.toMap(name: String, keyName: String, valueName: String): Map<String, List<String>> =
    groupListPerIndex(name)
        .values
        .associateBy({ it.find { pair -> pair.first.endsWith(keyName) }!!.second }) { values ->
            values.filter { it.first.matches(Regex(".*$valueName\\.\\d*")) }.map { it.second }
        }

fun <T> Parameters.toObjects(name: String, factory: () -> T, block: (Int, T, Pair<String, String>) -> T): List<T> =
    groupListPerIndex(name)
        .mapValues { (_, values) ->
            values.map {
                it.first.replace(Regex("$name\\.\\d*\\.*"), "") to it.second
            }
        }
        .map { (index, values) ->
            values.fold(factory()) { accumulator, keyValue ->
                block(index.toInt(), accumulator, keyValue)
            }
        }

private fun Parameters.groupListPerIndex(name: String): Map<String, List<Pair<String, String>>> {
    return filter { key, _ -> key.startsWith("$name.", true) }
        .flattenEntries()
        .groupBy { it.first.replace(Regex("$name\\.(\\d*?)\\..*")) { match -> match.groupValues[1] } }
}

fun Parameters.toPairs(name: String, keyName: String, valueName: String): List<Pair<String, String>> =
    filter { key, _ -> key.startsWith("$name.", true) }
        .flattenEntries()
        .groupBy { it.first.replace(Regex("$name\\.(\\d*?)\\..*")) { match -> match.groupValues[1] } }
        .values
        .associateBy({ it.find { pair -> pair.first.endsWith(keyName) }!!.second }) {
            it.find { pair -> pair.first.endsWith(valueName) }!!.second
        }.toList()
