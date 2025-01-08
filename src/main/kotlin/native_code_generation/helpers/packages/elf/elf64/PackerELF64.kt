package native_code_generation.helpers.packages.elf.elf64

import native_code_generation.helpers.AArchitecture
import native_code_generation.helpers.APacker
import utils.byte_order.toByteArrayLittleEndian
import utils.typing.EOperatingSystem

object PackerELF64 : APacker() {
    var num_of_phs: Short = 1
    var num_of_shs: Short = 1
    private lateinit var ARCH: AArchitecture
    private lateinit var OS: EOperatingSystem


    override fun setSettings(arch: AArchitecture, os: EOperatingSystem) {
        ARCH = arch
        OS = os
    }

    override fun pack(executable_code: List<UInt>): ByteArray {
        return executable_code.toByteArrayLittleEndian()
    }
}