//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document](../../index.md)/[CreateIssuanceRequestResult](../index.md)/[Success](index.md)

# Success

data class [Success](index.md)(val issuanceRequest: [IssuanceRequest](../../-issuance-request/index.md)) : [CreateIssuanceRequestResult](../index.md)

Success result containing the issuance request. The issuance request can be then used to issue the document from the issuer. The issuance request contains the certificate chain that must be sent to the issuer.

#### Parameters

androidJvm

| |
|---|
| issuanceRequest |

## Constructors

| | |
|---|---|
| [Success](-success.md) | [androidJvm]<br>constructor(issuanceRequest: [IssuanceRequest](../../-issuance-request/index.md)) |

## Functions

| Name | Summary |
|---|---|
| [getOrThrow](../get-or-throw.md) | [androidJvm]<br>open fun [getOrThrow](../get-or-throw.md)(): [IssuanceRequest](../../-issuance-request/index.md)<br>Get issuance request or throw the throwable that caused the failure |
| [onFailure](../on-failure.md) | [androidJvm]<br>open fun [onFailure](../on-failure.md)(block: ([Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [CreateIssuanceRequestResult](../index.md)<br>Execute block if the result is a failure |
| [onSuccess](../on-success.md) | [androidJvm]<br>open fun [onSuccess](../on-success.md)(block: ([IssuanceRequest](../../-issuance-request/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [CreateIssuanceRequestResult](../index.md)<br>Execute block if the result is successful |

## Properties

| Name | Summary |
|---|---|
| [issuanceRequest](issuance-request.md) | [androidJvm]<br>val [issuanceRequest](issuance-request.md): [IssuanceRequest](../../-issuance-request/index.md) |
