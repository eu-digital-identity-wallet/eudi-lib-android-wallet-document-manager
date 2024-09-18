//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document](../../index.md)/[Document](../index.md)/[State](index.md)

# State

[androidJvm]\
enum [State](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)
&lt;[Document.State](index.md)&gt;

The state of the document.

## Entries

|                                       |                                                       |
|---------------------------------------|-------------------------------------------------------|
| [UNSIGNED](-u-n-s-i-g-n-e-d/index.md) | [androidJvm]<br>[UNSIGNED](-u-n-s-i-g-n-e-d/index.md) |
| [ISSUED](-i-s-s-u-e-d/index.md)       | [androidJvm]<br>[ISSUED](-i-s-s-u-e-d/index.md)       |
| [DEFERRED](-d-e-f-e-r-r-e-d/index.md) | [androidJvm]<br>[DEFERRED](-d-e-f-e-r-r-e-d/index.md) |

## Properties

| Name                                                                      | Summary                                                                                                                                                                                                                                                                             |
|---------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [entries](entries.md)                                                     | [androidJvm]<br>val [entries](entries.md): [EnumEntries](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.enums/-enum-entries/index.html)&lt;[Document.State](index.md)&gt;<br>Returns a representation of an immutable list of all enum entries, in the order they're declared. |
| [name](-d-e-f-e-r-r-e-d/index.md#-372974862%2FProperties%2F1351694608)    | [androidJvm]<br>val [name](-d-e-f-e-r-r-e-d/index.md#-372974862%2FProperties%2F1351694608): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)                                                                                                        |
| [ordinal](-d-e-f-e-r-r-e-d/index.md#-739389684%2FProperties%2F1351694608) | [androidJvm]<br>val [ordinal](-d-e-f-e-r-r-e-d/index.md#-739389684%2FProperties%2F1351694608): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)                                                                                                           |
| [value](value.md)                                                         | [androidJvm]<br>val [value](value.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)                                                                                                                                                                 |

## Functions

| Name                   | Summary                                                                                                                                                                                                                                                                                                                                                                             |
|------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [valueOf](value-of.md) | [androidJvm]<br>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Document.State](index.md)<br>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md)    | [androidJvm]<br>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Document.State](index.md)&gt;<br>Returns an array containing the constants of this enum type, in the order they're declared.                                                                                                                          |
