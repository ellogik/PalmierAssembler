package plugins

import parsing.nodes.commands.DMoveCommandNode
import plugins.packages.elf.DELFRequirementsToArchitecture
import utils.byte_order.EByteOrder
import utils.typing.EArchitecture
import utils.typing.EBitType
import java.lang.reflect.Type

abstract class AArchitecturePlugin : IPlugin {
    abstract val FOR_ARCH: EArchitecture
    abstract val ELF_REQUIREMENTS: DELFRequirementsToArchitecture?
    abstract val BYTE_ORDER: EByteOrder
    abstract val BITS_TYPE: EBitType
    abstract val SIMPLE_COMMANDS: Map< Type, List<UInt> >

    abstract fun processMoveCommand(from: DMoveCommandNode): List<UInt>
}