package native_code_generation.helpers.packages.elf.elf64

import native_code_generation.helpers.AArchitecture
import native_code_generation.helpers.packages.elf.elf64.PackerELF64.ARCH
import native_code_generation.helpers.packages.elf.elf64.PackerELF64.HEADER_SIZE
import native_code_generation.helpers.packages.elf.elf64.PackerELF64.OS
import native_code_generation.helpers.packages.elf.elf64.PackerELF64.PROGRAM_HEADER_SIZE
import native_code_generation.helpers.packages.elf.elf64.PackerELF64.SECTION_HEADER_SIZE
import native_code_generation.helpers.packages.elf.elf64.PackerELF64.SH_STR_TAB_INDEX
import native_code_generation.helpers.packages.elf.elf64.PackerELF64.num_of_phs
import native_code_generation.helpers.packages.elf.elf64.PackerELF64.num_of_shs
import utils.errors.DInvalidArgumentError
import utils.typing.EOperatingSystem
import java.nio.ByteBuffer

data class DELF64Header(
    val class_type: Byte = 2, // ELF64
    val endianness: Byte = 1, // Little-endian
    val elf_version: Byte = 1, // Original version
    val os_abi: Byte = 0, // System V
    val abi_version: Byte = 0, // ABI version
    val type: Short = 1, // Executable file
    val machine: Short = 0x3E, // x86-64
    val version: Int = 1, // Version of the ELF
    val entry: Long = 0x400000, // Entry point address (example)
    val ph_offset: Long = 64, // Program header table offset
    val sh_offset: Long = 0, // Section header table offset
    val flags: Int = 0, // Flags (depends on architecture)
    val eh_size: Short = HEADER_SIZE, // ELF header size
    val ph_entry_size: Short = PROGRAM_HEADER_SIZE, // Program header entry size
    val ph_count: Short = 1, // Number of program headers
    val sh_entry_size: Short = SECTION_HEADER_SIZE, // Section header entry size
    val sh_count: Short = 0, // Number of sections
    val sh_str_index: Short = (num_of_shs - 1).toShort() // Section header string table index
) {
    companion object {
        val PADDING: ByteArray = ByteArray(7)
        val MAGIC: ByteArray = byteArrayOf(0x7F.toByte(), 'E'.code.toByte(), 'L'.code.toByte(), 'F'.code.toByte())

        fun fromStuff() = DELF64Header(
            class_type = 2, // ELF64
            endianness = ARCH.BYTE_ORDER.toELF(),
            elf_version = 1, // current
            os_abi = OS.toELFAbi(),
            abi_version = 0, // none
            type = 1, // relocatable(static lib)
            machine = ARCH.toELF(),
            version = 1, // current
            entry = if(ARCH.ELF_ENTRY != null) ARCH.ELF_ENTRY!! else throw DInvalidArgumentError("$ARCH doesn't implement ELF"),
            ph_offset = HEADER_SIZE.toLong(),
            sh_offset = (HEADER_SIZE + num_of_phs * PROGRAM_HEADER_SIZE).toLong(),
            flags = 0,
            eh_size = HEADER_SIZE,
            ph_entry_size = PROGRAM_HEADER_SIZE,
            ph_count = num_of_phs,
            sh_entry_size = SECTION_HEADER_SIZE,
            sh_count = num_of_shs,
            sh_str_index = SH_STR_TAB_INDEX
        )
    }

    fun toByteArray(): ByteArray {
        val buffer = ByteBuffer.allocate(HEADER_SIZE.toInt()).order(ARCH.BYTE_ORDER.toJavaByteOrder())

        buffer.put(MAGIC)
        buffer.put(class_type)
        buffer.put(endianness)
        buffer.put(elf_version)
        buffer.put(os_abi)
        buffer.put(abi_version)
        buffer.put(PADDING)
        buffer.putShort(type)
        buffer.putShort(machine)
        buffer.putInt(version)
        buffer.putLong(entry)
        buffer.putLong(ph_offset)
        buffer.putLong(sh_offset)
        buffer.putInt(flags)
        buffer.putShort(eh_size)
        buffer.putShort(ph_entry_size)
        buffer.putShort(ph_count)
        buffer.putShort(sh_entry_size)
        buffer.putShort(sh_count)
        buffer.putShort(sh_str_index)

        return buffer.array()
    }

}
