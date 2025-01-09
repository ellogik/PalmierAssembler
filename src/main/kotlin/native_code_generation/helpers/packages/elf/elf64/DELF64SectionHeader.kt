package native_code_generation.helpers.packages.elf.elf64

import native_code_generation.helpers.packages.elf.elf64.PackerELF64.ARCH
import native_code_generation.helpers.packages.elf.elf64.PackerELF64.HEADER_SIZE
import native_code_generation.helpers.packages.elf.elf64.PackerELF64.PROGRAM_HEADER_SIZE
import native_code_generation.helpers.packages.elf.elf64.PackerELF64.SECTION_HEADER_SIZE
import java.nio.ByteBuffer

data class DELF64SectionHeader(
    val name: Int,
    val type: Int,
    val flags: Long,
    val address: Long,
    val offset: Long,
    val size: Long,
    val link: Int,
    val info: Int,
    val address_align: Long,
    val entry_size: Long
) {
    companion object {
        private var DOT_TEXT_SIZE: Long? = null

        fun forText(size: Long): DELF64SectionHeader {
            DOT_TEXT_SIZE = size
            return DELF64SectionHeader(
                name = 1, //.text
                type = 1, // progbits
                flags = 0x6, // SHF_ALLOC | SHF_EXECINSTR
                address = ARCH.ELF_ENTRY!! + 0x1000L,
                offset = (HEADER_SIZE + (PROGRAM_HEADER_SIZE * PackerELF64.num_of_phs) + SECTION_HEADER_SIZE).toLong(),
                size = size,
                link = 0,
                info = 0,
                address_align = 16,
                entry_size = 0
            )
        }
        fun forShStrTab(size: Long) = DELF64SectionHeader(
            name = 7, //.shstrtab
            type = 3, // SHT_STRTAB
            flags = 0,
            address = 0,
            offset = (HEADER_SIZE + (PROGRAM_HEADER_SIZE * PackerELF64.num_of_phs) + (SECTION_HEADER_SIZE * PackerELF64.num_of_shs) + DOT_TEXT_SIZE!! ),
            size = size,
            link = 0,
            info = 0,
            address_align = 1,
            entry_size = 0
        )
    }

    fun toByteArray(): ByteArray {
        val buffer = ByteBuffer.allocate(SECTION_HEADER_SIZE.toInt()).order(ARCH.BYTE_ORDER.toJavaByteOrder())

        buffer.putInt(name)
        buffer.putInt(type)
        buffer.putLong(flags)
        buffer.putLong(address)
        buffer.putLong(offset)
        buffer.putLong(size)
        buffer.putInt(link)
        buffer.putInt(info)
        buffer.putLong(address_align)
        buffer.putLong(entry_size)

        return buffer.array()
    }
}