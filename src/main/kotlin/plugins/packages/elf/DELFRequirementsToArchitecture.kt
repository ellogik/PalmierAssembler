package plugins.packages.elf

import utils.interfaces.IRequireEntry

data class DELFRequirementsToArchitecture(
    val PROGRAM_HEADER_SIZE: UShort,
    val SECTION_HEADER_SIZE: UShort,
    override val ENTRY: ULong,
    val HEADER_SIZE: UShort,
) : IRequireEntry
