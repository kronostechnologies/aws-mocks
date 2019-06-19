package com.equisoft.awsmocks.common.interfaces.http

import com.amazonaws.AmazonWebServiceRequest
import io.ktor.application.ApplicationCall
import io.ktor.application.log
import io.ktor.http.Parameters
import io.ktor.request.receive
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
        val parameters: Parameters = call.receive<Parameters>().also { logRequest(call, it) }
        val actionName: String = checkNotNull(parameters[ACTION_NAME_PARAM])
        val klass: Class<*> = Class.forName("$rootPackage.${actionName}Request")

        val request: AmazonWebServiceRequest = klass.newInstance() as AmazonWebServiceRequest

        val accountId: String? = call.getAccountId()
        if (accountId != null) {
            request.accountId = accountId
        }

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
    filter { key, _ -> key.startsWith("$name.", true) }
        .flattenEntries()
        .groupBy { it.first.replace(Regex("$name\\.(\\d*?)\\..*")) { it.groupValues[1] } }
        .map { it.value }
        .associateBy({ it.find { pair -> pair.first.endsWith(keyName) }!!.second }) { values ->
            values.filter { it.first.matches(Regex(".*$valueName\\.\\d*")) }.map { it.second }
        }
