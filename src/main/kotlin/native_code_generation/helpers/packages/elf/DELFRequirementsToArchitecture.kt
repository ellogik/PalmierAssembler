package native_code_generation.helpers.packages.elf

import utils.interfaces.IRequireEntry

data class DELFRequirementsToArchitecture(
    override val ENTRY: ULong,
) : IRequireEntry
