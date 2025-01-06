package native_code_generation

data class DCompileError(
    val what: String
) : Exception()