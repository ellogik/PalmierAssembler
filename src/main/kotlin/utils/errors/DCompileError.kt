package utils.errors

data class DCompileError(
    val what: String
) : Exception()