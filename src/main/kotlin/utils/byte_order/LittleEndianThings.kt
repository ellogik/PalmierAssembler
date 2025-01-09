package utils.byte_order

import java.nio.ByteBuffer
import java.nio.ByteOrder

fun List<UInt>.toByteArrayLittleEndian(): ByteArray {
    val buffer = ByteBuffer.allocate(this.size * 4).order(ByteOrder.LITTLE_ENDIAN) // 4 байта для каждого UInt
    for (item in this) {
        buffer.putInt(item.toInt())
    }
    return buffer.array()
}

fun Int.toLittleEndian(): List<UInt> {
    return listOf(
        (this and 0xFF).toUInt(),
        ((this shr 8) and 0xFF).toUInt(),
        ((this shr 16) and 0xFF).toUInt(),
        ((this shr 24) and 0xFF).toUInt()
    )
}
