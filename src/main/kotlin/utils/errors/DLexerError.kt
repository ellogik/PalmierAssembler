package utils.errors

data class DLexerError(
    val what: String,
    val type: ELexerErrorType,
    val where: String
) : Exception()

enum class ELexerErrorType {
    UNKNOWN_SYMBOL
}