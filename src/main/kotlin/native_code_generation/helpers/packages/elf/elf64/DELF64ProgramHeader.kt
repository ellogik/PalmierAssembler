package native_code_generation.helpers.packages.elf.elf64

import native_code_generation.helpers.AArchitecture
import native_code_generation.helpers.packages.elf.elf64.DELF64Header.Companion.HEADER_SIZE
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
        lateinit var ARCH: AArchitecture
    }

    fun toByteArray(): ByteArray {
        val buffer = ByteBuffer.allocate(HEADER_SIZE.toInt()).order(ARCH.BYTE_ORDER.toJavaByteOrder())

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
