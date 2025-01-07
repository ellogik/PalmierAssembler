package native_code_generation.helpers.packages.elf

import native_code_generation.helpers.AArchitecture
import native_code_generation.helpers.APacker
import utils.typing.EAppType
import utils.typing.EOperatingSystem

object PackerELF : APacker() {
    val NUM_PROGRAM_HEADERS: UShort = 1u
    val NUM_SECTION_HEADERS: UShort = 1u
    private lateinit var current_arch: AArchitecture
    private lateinit var current_os: EOperatingSystem
    private lateinit var current_app_type: EAppType

    override fun setSettings(arch: AArchitecture, os: EOperatingSystem, app_type: EAppType)
    {
        current_arch = arch
        current_os = os
        current_app_type = app_type
    }

    override fun pack(executable_code: List<UInt>): List<UInt>
    {
        val header = DELFHeader.fromStuff(
            current_arch,
            current_os,
            current_app_type
        ).toByteArray(current_arch)
        val text_ph = DProgramHeader.forTextProgramHeader(
            (executable_code.size * UInt.SIZE_BYTES).toULong(),
            current_arch
        ).toByteArray(current_arch)
        val text_sh = DSectionHeader
            .forTextSection(executable_code.size.toULong())
            .toByteArray(current_arch)

        println("ELF Header: $header")
        println("Program Header for .text: $text_ph")
        println("Section Header for .text: $text_sh")


        return executable_code
    }
}