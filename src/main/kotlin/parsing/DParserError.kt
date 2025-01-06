package parsing

data class DParserError(
    val what: String
) : Exception()