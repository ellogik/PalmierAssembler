package utils

enum class EBitType {
    X8,
    X16,
    X32,
    X64,
    X128;

    override fun toString(): String {
        return when(this){
            X128 -> "128bit"
            X64 -> "64bit"
            X32 -> "32bit"
            X16 -> "16bit"
            X8 -> "8bit"
        }
    }
}