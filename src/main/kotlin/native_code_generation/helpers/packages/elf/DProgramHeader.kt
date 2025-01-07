package native_code_generation.helpers.packages.elf

import native_code_generation.helpers.AArchitecture
import utils.errors.DInvalidArgumentError
import utils.typing.EBitType

data class DProgramHeader(
    val type: UInt,                 // Segment type
    val flags: UInt,                // Access flags
    val offset: ULong,              // Bias to segment into file
    val virtual_address: ULong,     // Virtual address of loading
    val physical_address: ULong,    // Physical address (обычно совпадает с pVAddr)
    val file_size: ULong,           // Size of segment in file
    val in_mem_size: ULong,         // Size of segment in memory
    val align: ULong                // Alignment
) {
    companion object {
        fun forTextProgramHeader(code_size: ULong, arch: AArchitecture): DProgramHeader {
            if( arch.ELF_REQUIREMENTS == null ) throw DInvalidArgumentError("Architecture '${arch.FOR_ARCH}' doesn't implement ELF")

            return DProgramHeader(
                type = 1u,          // PT_LOAD
                flags = 5u,         // READ + EXECUTABLE
                offset = 0x40uL,    // Start of .text
                virtual_address = arch.ELF_REQUIREMENTS!!.ENTRY + 0x40uL,
                physical_address = arch.ELF_REQUIREMENTS!!.ENTRY + 0x40uL,
                file_size = code_size,
                in_mem_size = code_size,
                align = 0x1000uL
            )
        }
    }

    fun toByteArray(arch: AArchitecture): List<Byte> {
        val array = ByteArray(56).apply {
            var off = 0
            putUInt(off, type); off += 4
            putUInt(off, flags); off += 4
            putULong(off, offset); off += 8
            putULong(off, virtual_address); off += 8
            putULong(off, physical_address); off += 8
            putULong(off, file_size); off += 8
            putULong(off, in_mem_size); off += 8
            putULong(off, align)
        }.toList()

        return array
    }
}

fun EBitType.toELFProgramHeaderSize(): UShort {
    return when(this) {
        EBitType.X64 -> 56u
        EBitType.X32 -> 32u

        else -> throw DInvalidArgumentError("ELF doesn't supported '$this'")
    }
}

fun ByteArray.putUInt(offset: Int, value: UInt) {
    this[offset] = (value and 0xFFu).toByte()
    this[offset + 1] = ((value shr 8) and 0xFFu).toByte()
    this[offset + 2] = ((value shr 16) and 0xFFu).toByte()
    this[offset + 3] = ((value shr 24) and 0xFFu).toByte()
}

fun ByteArray.putULong(offset: Int, value: ULong) {
    for (i in 0 until 8) {
        this[offset + i] = ((value shr (i * 8)) and 0xFFu).toByte()
    }
}