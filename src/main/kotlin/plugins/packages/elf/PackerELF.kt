package plugins.packages.elf

import plugins.APackerPlugin

object PackerELF : APackerPlugin() {
    val NUM_PROGRAM_HEADERS: UShort = 3u
    val NUM_SECTION_HEADERS: UShort = 5u

    override fun pack(executable_code: List<UInt>): List<UInt> {
        return executable_code
    }
}