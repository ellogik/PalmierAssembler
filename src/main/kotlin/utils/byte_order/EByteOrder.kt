package utils.byte_order

import java.nio.ByteOrder

enum class EByteOrder {
    LITTLE_ENDIAN,
    BIG_ENDIAN;

    fun toJavaByteOrder(): ByteOrder = when(this){
        LITTLE_ENDIAN -> ByteOrder.LITTLE_ENDIAN
        BIG_ENDIAN -> ByteOrder.BIG_ENDIAN
    }


}