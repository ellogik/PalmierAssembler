package utils.errors

data class DInvalidArgumentError(
    val what: String
) : Exception()