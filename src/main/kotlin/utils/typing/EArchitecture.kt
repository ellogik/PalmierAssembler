package utils.typing

enum class EArchitecture {
    X86,
    X86_64,
    ARM,
    ARM64,
    RISC_V


    ;

    override fun toString(): String {
        return when(this) {
            X86_64 -> "x86-64"
            RISC_V -> "RISC-V"
            else -> super.toString()
        }
    }

    fun fromString(from: String): EArchitecture {
        return when(from.lowercase()) {
            "x86-64", "x86_64" -> X86_64

            else -> throw RuntimeException()
        }
    }
}