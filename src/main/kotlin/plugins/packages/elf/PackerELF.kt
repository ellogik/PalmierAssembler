package plugins.packages.elf

import plugins.APackerPlugin

object PackerELF : APackerPlugin() {
    override fun pack(executable_code: List<UInt>): List<UInt> {
        return executable_code
    }
}