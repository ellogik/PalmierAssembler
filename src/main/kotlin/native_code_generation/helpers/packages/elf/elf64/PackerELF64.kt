package native_code_generation.helpers.packages.elf.elf64

import native_code_generation.helpers.AArchitecture
import native_code_generation.helpers.APacker
import utils.typing.EOperatingSystem

object PackerELF64 : APacker() {
    var num_of_phs: Short = 1
    var num_of_shs: Short = 4
    const val HEADER_SIZE: Short = 64
    const val PROGRAM_HEADER_SIZE: Short = 56
    const val SECTION_HEADER_SIZE: Short = 64
    const val SH_STR_TAB_INDEX: Short = 2
    const val TEXT_INDEX: Short = 1
    lateinit var ARCH: AArchitecture
    lateinit var OS: EOperatingSystem


    override fun setSettings(arch: AArchitecture, os: EOperatingSystem) {
        ARCH = arch
        OS = os
    }

    override fun pack(executable_code: List<UInt>): ByteArray {
        val size = (executable_code.size).toLong()
        val header_bin = DELF64Header.fromStuff().toByteArray()
        val text_program_header_bin = DELF64ProgramHeader.forText(
            size
        ).toByteArray()
        val text_section_header_bin = DELF64SectionHeader.forText(size).toByteArray()
        val shstrtab_data = byteArrayOf(
            0,
            '.'.code.toByte(), // 1
            's'.code.toByte(),
            'h'.code.toByte(),
            's'.code.toByte(),
            't'.code.toByte(),
            'r'.code.toByte(),
            't'.code.toByte(),
            'a'.code.toByte(),
            'b'.code.toByte(),
            0,
            '.'.code.toByte(), // 11
            't'.code.toByte(),
            'e'.code.toByte(),
            'x'.code.toByte(),
            't'.code.toByte(),
            0,
            '.'.code.toByte(), // 17
            's'.code.toByte(),
            'y'.code.toByte(),
            'm'.code.toByte(),
            't'.code.toByte(),
            'a'.code.toByte(),
            'b'.code.toByte(),
            0
        )

        val shstrtab_section_header_bin = DELF64SectionHeader.forShStrTab(
            shstrtab_data.size.toLong()
        ).toByteArray()

        val symtab_section_data = DELF64Symbol(
            name = 0,
            info = 0,
            other = 0,
            section_index = 0,
            value = 0,
            size = 0
        ).toByteArray() + DELF64Symbol(
            name = 11,
            info = 0x3,
            other = 0,
            section_index = TEXT_INDEX,
            value = 0x0,
            size = 0
        ).toByteArray()

        val symtab_section_header_bin = DELF64SectionHeader.forSymTab(symtab_section_data.size.toLong()).toByteArray()
        val obj = header_bin +
                text_program_header_bin +

                DELF64SectionHeader.forNull().toByteArray() +
                text_section_header_bin +
                shstrtab_section_header_bin +
                symtab_section_header_bin +

                executable_code.map { it.toByte() } +
                shstrtab_data +
                symtab_section_data


        return obj
    }
}