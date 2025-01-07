package native_code_generation.helpers

import utils.typing.EAppType
import utils.typing.EOperatingSystem

abstract class APacker {
    abstract fun setSettings(arch: AArchitecture, os: EOperatingSystem, app_type: EAppType)
    abstract fun pack(executable_code: List<UInt>): List<UInt>
}