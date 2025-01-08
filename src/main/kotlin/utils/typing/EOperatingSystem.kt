package utils.typing

enum class EOperatingSystem {
    LINUX,
    WINDOWS_NT,
    MAC_OS;

    override fun toString(): String {
        return when(this) {
            LINUX -> "Linux"
            WINDOWS_NT -> "WindowsNT"
            MAC_OS -> "macOS"
        }
    }

    fun fromString(from: String): EOperatingSystem {
        return when(from.lowercase()){
            "linux" -> LINUX
            "windowsnt" -> WINDOWS_NT
            "macos" -> MAC_OS

            else -> throw RuntimeException("Unknown Operating System '$from'")
        }
    }
}