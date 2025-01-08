package utils.byte_order

fun List<UInt>.toByteArrayLittleEndian(): ByteArray {
    val byteArray = ByteArray(this.size * UInt.SIZE_BYTES)
    for (i in this.indices) {
        val value = this[i]
        byteArray[i * 4] = value.toByte()
        byteArray[i * 4 + 1] = (value shr 8).toByte()
        byteArray[i * 4 + 2] = (value shr 16).toByte()
        byteArray[i * 4 + 3] = (value shr 24).toByte()
    }
    return byteArray
}

fun Int.toLittleEndian(): List<UInt> {
    return listOf(
        (this and 0xFF).toUInt(),
        ((this shr 8) and 0xFF).toUInt(),
        ((this shr 16) and 0xFF).toUInt(),
        ((this shr 24) and 0xFF).toUInt()
    )
}
