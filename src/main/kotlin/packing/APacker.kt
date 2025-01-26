package packing

import native_code_generation.helpers.AArchitecture
import native_code_generation.helpers.AOperatingSystem

abstract class APacker {
    abstract fun setSettings(arch: AArchitecture, os: AOperatingSystem)
    abstract fun pack(executable_code: List<UInt>): ByteArray
}