package packing.elf.elf64

import native_code_generation.helpers.AArchitecture
import native_code_generation.helpers.AOperatingSystem
import native_code_generation.helpers.architectures.IELFSupportInArch
import packing.APacker
import parsing.nodes.AASTNode
import java.nio.ByteBuffer

object PackerELF64 : APacker() {
    var num_of_phs: Short = 1
    var num_of_shs: Short = 5
    lateinit var ARCH: AArchitecture
    lateinit var OS: AOperatingSystem

    const val HEADER_SIZE: Short = 64
    const val PROGRAM_HEADER_SIZE: Short = 56
    const val SECTION_HEADER_SIZE: Short = 64

    const val TEXT_INDEX: Short = 1
    const val SH_STR_TAB_INDEX: Short = 2
    const val STR_TAB_INDEX: Short = 4

    const val TEXT_NAME_INDEX = 11
    const val SH_STR_TAB_NAME_INDEX = 1
    const val SYMTAB_NAME_INDEX = 17
    const val STR_TAB_NAME_INDEX = 25

    private val SH_STR_TAB_BASE_DATA_BIN = "${0.toChar()}.shstrtab${0.toChar()}.text${0.toChar()}.symtab${0.toChar()}.strtab".toByteArray()
    private lateinit var blocks_register_binary: ByteBuffer
    private lateinit var blocks_names_binary: ByteBuffer

    private var blocks_names = "" + 0.toChar()



    override fun setSettings(arch: AArchitecture, os: AOperatingSystem) {
        ARCH = arch
        OS = os
        ENTRY = (ARCH as IELFSupportInArch).ELF_ENTRY
    }

    override fun packCode(executable_code: List<UInt>): ByteArray {
        val size = (executable_code.size).toLong()
        val header_bin = DELF64Header.fromStuff().toByteArray()
        val text_program_header_bin = DELF64ProgramHeader.forText(
            size
        ).toByteArray()
        val text_section_header_bin = DELF64SectionHeader.forText(size).toByteArray()
        val shstrtab_data = SH_STR_TAB_BASE_DATA_BIN

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
            name = blocks_names.length + TEXT_NAME_INDEX,
            info = 0x3,
            other = 0,
            section_index = TEXT_INDEX,
            value = 0x0,
            size = 0
        ).toByteArray() + blocks_register_binary.array()

        val symtab_section_header_bin = DELF64SectionHeader.forSymTab(symtab_section_data.size.toLong()).toByteArray()

        val strtab_section_header_bin = DELF64SectionHeader.forStrTab(blocks_names_binary.array().size.toLong()).toByteArray()

        val obj = header_bin +
                text_program_header_bin +

                DELF64SectionHeader.forNull().toByteArray() +
                text_section_header_bin +
                shstrtab_section_header_bin +
                symtab_section_header_bin +
                strtab_section_header_bin +

                executable_code.map { it.toByte() } +
                shstrtab_data +
                symtab_section_data +
                blocks_names_binary.array()


        return obj
    }

    override fun packVariables(target: List<AASTNode>): Map<String, Long> {
        return mapOf()
    }

    override fun registerBlocks(target: Map<String, Pair<Number, Number>>) {
        blocks_register_binary = ByteBuffer.allocate(target.size * 24).order(ARCH.BYTE_ORDER.toJavaByteOrder())

        for( (key, value) in target ) {
            blocks_register_binary.put(
                DELF64Symbol(
                    name = blocks_names.length,
                    info = encodeElfSymbolInfo(1, 2).toByte(), // function + global
                    other = 0,
                    section_index = TEXT_INDEX,
                    value = value.first.toLong(),
                    size = value.second.toLong()
                ).toByteArray()
            )
            blocks_names += key + 0.toChar()
        }

        blocks_names_binary = ByteBuffer.allocate(blocks_names.length + SH_STR_TAB_BASE_DATA_BIN.size).order(ARCH.BYTE_ORDER.toJavaByteOrder())

        blocks_names_binary
            .put(blocks_names.toByteArray())
            .put(SH_STR_TAB_BASE_DATA_BIN)
    }
}