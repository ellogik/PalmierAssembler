package native_code_generation.helpers.packages.elf.elf64

import native_code_generation.helpers.AArchitecture
import native_code_generation.helpers.APacker
import utils.byte_order.toByteArrayLittleEndian
import utils.typing.EOperatingSystem

object PackerELF64 : APacker() {
    var num_of_phs: Short = 1
    var num_of_shs: Short = 2
    const val HEADER_SIZE: Short = 64
    const val PROGRAM_HEADER_SIZE: Short = 56
    const val SECTION_HEADER_SIZE: Short = 64
    lateinit var ARCH: AArchitecture
    lateinit var OS: EOperatingSystem


    override fun setSettings(arch: AArchitecture, os: EOperatingSystem) {
        ARCH = arch
        OS = os
    }

    override fun pack(executable_code: List<UInt>): ByteArray {
        val size = (executable_code.size * UInt.SIZE_BYTES).toLong()
        val header_bin = DELF64Header.fromStuff().toByteArray()
        val text_program_header_bin = DELF64ProgramHeader.forText(
            size
        ).toByteArray()
        val text_section_header_bin = DELF64SectionHeader.forText(size).toByteArray()
        val shstrtab_data = byteArrayOf(
            0,            // \0
            't'.code.toByte(),
            'e'.code.toByte(),
            'x'.code.toByte(),
            't'.code.toByte(),
            0,            // \0 (завершающий нулевой байт для ".text")
            's'.code.toByte(),
            'h'.code.toByte(),
            's'.code.toByte(),
            't'.code.toByte(),
            'r'.code.toByte(),
            't'.code.toByte(),
            'a'.code.toByte(),
            'b'.code.toByte(),
            0             // \0 (завершающий нулевой байт для ".shstrtab")
        )
        val shstrtab_bin = DELF64SectionHeader.forShStrTab((shstrtab_data.size * Byte.SIZE_BYTES).toLong()).toByteArray()

        println("Original executable code: $executable_code")
        println("On ByteArray(LE): ${executable_code.toByteArrayLittleEndian().toList()}")


        val obj = header_bin + text_program_header_bin + text_section_header_bin + executable_code.toByteArrayLittleEndian() + shstrtab_bin + shstrtab_data


        return obj
    }
}