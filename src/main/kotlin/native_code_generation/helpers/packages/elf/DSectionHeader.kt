package native_code_generation.helpers.packages.elf

import native_code_generation.helpers.AArchitecture
import utils.errors.DInvalidArgumentError
import utils.typing.EBitType
import java.nio.ByteBuffer

data class DSectionHeader(
    val name: UInt,                 // String's index with name of section in table of strings
    val type: UInt,                 // Section type
    val flags: ULong,               // Flags for section
    val address: ULong,             // Address in memory where will be loaded section
    val offset: ULong,              // Bias section in file
    val size: ULong,                // Size of section
    val linked_section: UInt,       // Index of linked section
    val other_info: UInt,           // Other info
    val address_align: ULong,       // Alignment in memory
    val entry_size: ULong           // Size of elements in section, if enable
) {
    companion object {
        fun forTextSection(size_of_code: ULong): DSectionHeader {
            return DSectionHeader(
                name = 1u,                  // .text | 1
                type = 1u,                  // SHT_PROGBITS
                flags = 4uL,                // SHF_EXECINSTR
                address = 0uL,
                offset = 64uL,              // 64 bytes
                size = size_of_code,
                linked_section = 0u,
                other_info = 0u,
                address_align = 16uL,       // By 16 byte
                entry_size = 0uL
            )
        }
    }

    fun toByteArray(arch: AArchitecture): List<Byte> {
        val byte_buffer = ByteBuffer.allocate(arch.BITS_TYPE.toELFSectionHeaderSize().toInt())

        byte_buffer.putInt(name.toInt())
        byte_buffer.putInt(type.toInt())
        byte_buffer.putLong(flags.toLong())
        byte_buffer.putLong(address.toLong())
        byte_buffer.putLong(offset.toLong())
        byte_buffer.putLong(size.toLong())
        byte_buffer.putInt(linked_section.toInt())
        byte_buffer.putInt(other_info.toInt())
        byte_buffer.putLong(address_align.toLong())
        byte_buffer.putLong(entry_size.toLong())

        return byte_buffer.array().toList()
    }
}

fun EBitType.toELFSectionHeaderSize(): UShort {
    return when(this) {
        EBitType.X64 -> 64u
        EBitType.X32 -> 40u

        else -> throw DInvalidArgumentError("ELF doesn't support '$this'")
    }
}


