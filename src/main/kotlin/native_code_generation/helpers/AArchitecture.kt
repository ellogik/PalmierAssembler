package native_code_generation.helpers

import parsing.nodes.commands.DMoveCommandNode
import utils.byte_order.EByteOrder
import utils.EBitType
import java.lang.reflect.Type

abstract class AArchitecture {
    abstract val BYTE_ORDER: EByteOrder
    abstract val BITS_TYPE: EBitType
    abstract val SIMPLE_COMMANDS: Map< Type, List<UInt> >

    abstract fun processMoveCommand(from: DMoveCommandNode): List<UInt>
}