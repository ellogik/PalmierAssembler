package packing

import native_code_generation.helpers.AArchitecture
import utils.typing.EOperatingSystem

abstract class APacker {
    abstract fun setSettings(arch: AArchitecture, os: EOperatingSystem)
    abstract fun pack(executable_code: List<UInt>): ByteArray
}