/*
 * Copyright (c) 2024 European Commission
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.europa.ec.eudi.wallet.document

/**
 * Outcome for encapsulating success or failure of a computation for document manager operations.
 * Wraps a [Result] instance to provide Java interop.
 */
class Outcome<out T> internal constructor(val result: Result<T>) {
    companion object {
        /**
         * Returns an instance that encapsulates the given [value] as successful value.
         */
        @JvmStatic
        fun <T> success(value: T): Outcome<T> = Outcome(Result.success(value))

        /**
         * Returns an instance that encapsulates the given [Throwable] as failure.
         */
        @JvmStatic
        fun <T> failure(throwable: Throwable): Outcome<T> =
            Outcome(Result.failure(throwable))
    }

    /**
     * Returns `true` if [result] instance represents a successful outcome.
     * In this case [eu.europa.ec.eudi.wallet.document.Outcome.isFailure] return `false` .
     */
    val isSuccess: Boolean
        get() = result.isSuccess

    /**
     * Returns `true` if [result] instance represents a failed outcome.
     * In this case [eu.europa.ec.eudi.wallet.document.Outcome.isSuccess] returns `false`.
     */
    val isFailure: Boolean
        get() = result.isFailure

    /**
     * Returns the encapsulated value if this instance represents a successful outcome or `null`
     * if it is failure.
     */
    fun getOrNull(): T? = result.getOrNull()

    /**
     * Returns the encapsulated exception if this instance represents a failure outcome or `null`
     * if it is success.
     */
    fun exceptionOrNull(): Throwable? = result.exceptionOrNull()

    /**
     * Returns the encapsulated value if this instance represents a successful outcome or throws
     * the encapsulated exception if it is failure.
     */
    fun getOrThrow(): T = result.getOrThrow()

    override fun toString(): String =
        if (isSuccess) "Outcome(value = ${getOrNull()})"
        else "Outcome(error = ${exceptionOrNull()?.message})"
}