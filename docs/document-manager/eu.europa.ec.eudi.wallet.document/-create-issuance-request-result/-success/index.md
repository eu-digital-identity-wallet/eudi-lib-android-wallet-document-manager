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

## Properties

| Name | Summary |
|---|---|
| [issuanceRequest](issuance-request.md) | [androidJvm]<br>val [issuanceRequest](issuance-request.md): [IssuanceRequest](../../-issuance-request/index.md) |
