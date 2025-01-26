package packing.elf.elf64

import native_code_generation.helpers.architectures.IELFSupportInArch
import packing.elf.elf64.PackerELF64.ARCH
import packing.elf.elf64.PackerELF64.HEADER_SIZE
import packing.elf.elf64.PackerELF64.PROGRAM_HEADER_SIZE
import packing.elf.elf64.PackerELF64.SECTION_HEADER_SIZE
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
            offset = HEADER_SIZE.toLong() +
                    PROGRAM_HEADER_SIZE * PackerELF64.num_of_phs +
                    SECTION_HEADER_SIZE * PackerELF64.num_of_shs,
            virtual_address = (ARCH as IELFSupportInArch).ELF_ENTRY,
            physical_address = (ARCH as IELFSupportInArch).ELF_ENTRY,
            in_file_size = size,
            in_memory_size = size,
            align = 0x1000L
        )
    }

    fun toByteArray(): ByteArray {
        val buffer = ByteBuffer.allocate(PROGRAM_HEADER_SIZE.toInt()).order(ARCH.BYTE_ORDER.toJavaByteOrder())

        buffer.put(type.toByteBuffer(ARCH.BYTE_ORDER).array())
        buffer.put(access_flags.toByteBuffer(ARCH.BYTE_ORDER).array())
        buffer.put(offset.toByteBuffer(ARCH.BYTE_ORDER).array())
        buffer.put(virtual_address.toByteBuffer(ARCH.BYTE_ORDER).array())
        buffer.put(physical_address.toByteBuffer(ARCH.BYTE_ORDER).array())
        buffer.put(in_file_size.toByteBuffer(ARCH.BYTE_ORDER).array())
        buffer.put(in_memory_size.toByteBuffer(ARCH.BYTE_ORDER).array())
        buffer.put(align.toByteBuffer(ARCH.BYTE_ORDER).array())

        return buffer.array()
    }
}
