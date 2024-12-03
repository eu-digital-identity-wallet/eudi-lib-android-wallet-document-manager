package eu.europa.ec.eudi.wallet.document.internal

import kotlinx.serialization.json.Json

@JvmSynthetic
internal inline fun <reified T> ByteArray.toClassObject(): T? {
    return try {
        val jsonString = this.toString(Charsets.UTF_8)
        Json.decodeFromString<T>(jsonString)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}