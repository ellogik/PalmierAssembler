package native_code_generation.helpers

import native_code_generation.helpers.packages.elf.DELFRequirementsToArchitecture
import parsing.nodes.commands.DMoveCommandNode
import utils.byte_order.EByteOrder
import utils.typing.EArchitecture
import utils.typing.EBitType
import java.lang.reflect.Type

abstract class AArchitecture {
    abstract val FOR_ARCH: EArchitecture
    abstract val ELF_REQUIREMENTS: DELFRequirementsToArchitecture?
    abstract val BYTE_ORDER: EByteOrder
    abstract val BITS_TYPE: EBitType
    abstract val SIMPLE_COMMANDS: Map< Type, List<UInt> >

    abstract fun processMoveCommand(from: DMoveCommandNode): List<UInt>
}