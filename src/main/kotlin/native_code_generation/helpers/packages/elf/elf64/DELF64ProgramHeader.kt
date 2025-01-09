package native_code_generation.helpers.packages.elf.elf64

import native_code_generation.helpers.packages.elf.elf64.PackerELF64.ARCH
import native_code_generation.helpers.packages.elf.elf64.PackerELF64.HEADER_SIZE
import native_code_generation.helpers.packages.elf.elf64.PackerELF64.PROGRAM_HEADER_SIZE
import java.nio.ByteBuffer

data class DELF64ProgramHeader(
    val type: Int,
    val access_flags: Int,
    val offset: Long,
    val virtual_address: Long,
    val physical_address: Long,
    val in_file_size: Long,
    val in_memory_size: Long,
    val align: Long
) {
    companion object {
        fun forText(size: Long) = DELF64ProgramHeader(
            type = 1, // LOAD
            access_flags = 5, // READ + EXEC,
            offset = 0x1000L,
            virtual_address = ARCH.ELF_ENTRY!! + 0x1000L,
            physical_address = 0,
            in_file_size = size,
            in_memory_size = size,
            align = 0x1000L
        )
    }

    fun toByteArray(): ByteArray {
        val buffer = ByteBuffer.allocate(PROGRAM_HEADER_SIZE.toInt()).order(ARCH.BYTE_ORDER.toJavaByteOrder())

        buffer.putInt(type)
        buffer.putInt(access_flags)
        buffer.putLong(offset)
        buffer.putLong(virtual_address)
        buffer.putLong(physical_address)
        buffer.putLong(in_file_size)
        buffer.putLong(in_memory_size)
        buffer.putLong(align)

        return buffer.array()
    }
}
