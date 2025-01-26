package utils.errors

data class DParserError(
    val what: String
) : Exception()