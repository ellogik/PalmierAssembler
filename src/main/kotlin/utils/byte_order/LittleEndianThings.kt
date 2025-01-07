package utils.byte_order

import java.math.BigInteger

object LittleEndianThings {
    fun toLittleEndian32(value: Int): List<UInt> {
        val byte1 = (value and 0xFF).toUInt()
        val byte2 = ((value shr 8) and 0xFF).toUInt()
        val byte3 = ((value shr 16) and 0xFF).toUInt()
        val byte4 = ((value shr 24) and 0xFF).toUInt()

        return listOf(byte1, byte2, byte3, byte4)
    }

    fun toLittleEndian64(value: Long): List<UInt> {
        val byte1 = (value and 0xFF).toUInt()
        val byte2 = ((value shr 8) and 0xFF).toUInt()
        val byte3 = ((value shr 16) and 0xFF).toUInt()
        val byte4 = ((value shr 24) and 0xFF).toUInt()
        val byte5 = ((value shr 32) and 0xFF).toUInt()
        val byte6 = ((value shr 40) and 0xFF).toUInt()
        val byte7 = ((value shr 48) and 0xFF).toUInt()
        val byte8 = ((value shr 56) and 0xFF).toUInt()

        return listOf(byte1, byte2, byte3, byte4, byte5, byte6, byte7, byte8)
    }

    fun toLittleEndian128(value: BigInteger): List<UInt> {
        val byte1 = (value.and(BigInteger.valueOf(0xFF)).toInt() and 0xFF).toUInt()
        val byte2 = (value.shiftRight(8).and(BigInteger.valueOf(0xFF)).toInt() and 0xFF).toUInt()
        val byte3 = (value.shiftRight(16).and(BigInteger.valueOf(0xFF)).toInt() and 0xFF).toUInt()
        val byte4 = (value.shiftRight(24).and(BigInteger.valueOf(0xFF)).toInt() and 0xFF).toUInt()
        val byte5 = (value.shiftRight(32).and(BigInteger.valueOf(0xFF)).toInt() and 0xFF).toUInt()
        val byte6 = (value.shiftRight(40).and(BigInteger.valueOf(0xFF)).toInt() and 0xFF).toUInt()
        val byte7 = (value.shiftRight(48).and(BigInteger.valueOf(0xFF)).toInt() and 0xFF).toUInt()
        val byte8 = (value.shiftRight(56).and(BigInteger.valueOf(0xFF)).toInt() and 0xFF).toUInt()
        val byte9 = (value.shiftRight(64).and(BigInteger.valueOf(0xFF)).toInt() and 0xFF).toUInt()
        val byte10 = (value.shiftRight(72).and(BigInteger.valueOf(0xFF)).toInt() and 0xFF).toUInt()
        val byte11 = (value.shiftRight(80).and(BigInteger.valueOf(0xFF)).toInt() and 0xFF).toUInt()
        val byte12 = (value.shiftRight(88).and(BigInteger.valueOf(0xFF)).toInt() and 0xFF).toUInt()
        val byte13 = (value.shiftRight(96).and(BigInteger.valueOf(0xFF)).toInt() and 0xFF).toUInt()
        val byte14 = (value.shiftRight(104).and(BigInteger.valueOf(0xFF)).toInt() and 0xFF).toUInt()
        val byte15 = (value.shiftRight(112).and(BigInteger.valueOf(0xFF)).toInt() and 0xFF).toUInt()
        val byte16 = (value.shiftRight(120).and(BigInteger.valueOf(0xFF)).toInt() and 0xFF).toUInt()

        return listOf(byte1, byte2, byte3, byte4, byte5, byte6, byte7, byte8, byte9, byte10, byte11, byte12, byte13, byte14, byte15, byte16)
    }
}