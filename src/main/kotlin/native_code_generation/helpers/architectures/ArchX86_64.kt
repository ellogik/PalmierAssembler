package native_code_generation.helpers.architectures

import parsing.nodes.AASTNode
import parsing.nodes.commands.DMoveCommandNode
import parsing.nodes.commands.DSystemCallCommandNode
import parsing.nodes.expressions.DIntegerNode
import parsing.nodes.regs_and_vars.DGeneralRegNode
import native_code_generation.helpers.AArchitecture
import utils.byte_order.EByteOrder
import utils.byte_order.LittleEndianThings
import utils.typing.EArchitecture
import utils.typing.EBitType
import java.lang.reflect.Type

object ArchX86_64 : AArchitecture() {
    override val FOR_ARCH: EArchitecture = EArchitecture.X86_64
    override val ELF_REQUIREMENTS: DELFRequirementsToArchitecture = DELFRequirementsToArchitecture(
        ENTRY = 0x400000u
    )
    override val BITS_TYPE: EBitType = EBitType.X64
    override val BYTE_ORDER: EByteOrder = EByteOrder.LITTLE_ENDIAN
    override val SIMPLE_COMMANDS: Map<Type, List<UInt>> = mapOf(
        Pair(DSystemCallCommandNode::class.java, listOf(0x0Fu, 0x05u))
    )
    private val MOV_REG_IMM32_OP_CODES : Map<AASTNode, List<UInt>> = mapOf(
        Pair(DGeneralRegNode(1), listOf(0x48u, 0xB8u)), // rax <- imm32
        Pair(DGeneralRegNode(2), listOf(0x48u, 0xBBu)), // rbx <- imm32
    )



    override fun processMoveCommand(from: DMoveCommandNode): List<UInt> {
        val native_commands = mutableListOf<UInt>()

        if( from.value is DIntegerNode ) {
            native_commands += MOV_REG_IMM32_OP_CODES[from.receiver]!!
            native_commands += LittleEndianThings.toLittleEndian32(from.value.value)
        }

        return native_commands
    }
}