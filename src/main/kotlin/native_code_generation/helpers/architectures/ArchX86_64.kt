package native_code_generation.helpers.architectures

import frontend.Center.CURRENT_OS
import parsing.nodes.commands.DMoveCommandNode
import parsing.nodes.commands.DSystemCallCommandNode
import parsing.nodes.expressions.DIntegerNode
import native_code_generation.helpers.AArchitecture
import parsing.nodes.expressions.DIdNode
import parsing.nodes.regs_and_vars.*
import parsing.nodes.regs_and_vars.types.IRegNode
import parsing.nodes.regs_and_vars.types.IVarNode
import utils.byte_order.EByteOrder
import utils.byte_order.toLittleEndian
import utils.EBitType
import java.lang.reflect.Type

object ArchX86_64 : AArchitecture(), IELFSupportInArch {
    override val ELF_ID: Short = 0x3E
    override val ELF_ENTRY: Long = 0x401000
    override val BITS_TYPE: EBitType = EBitType.X64
    override val BYTE_ORDER: EByteOrder = EByteOrder.LITTLE_ENDIAN
    override val SIMPLE_COMMANDS: Map<Type, List<UInt>> = mapOf(
        Pair(DSystemCallCommandNode::class.java, listOf(0x0Fu, 0x05u))
    )
    private val MOV_REG_IMM32_OP_CODES : Map<IRegNode, List<UInt>> = mapOf(
        DGeneralRegNode(1u) to listOf(0xB8u),    // rax <- imm32\64
        DGeneralRegNode(2u) to listOf(0xB8u),    // rcx <- imm32\64
        DGeneralRegNode(3u) to listOf(0xBAu),    // rdx <- imm32\64
        DGeneralRegNode(4u) to listOf(0xBBu),    // rbx <- imm32\64
        DGeneralRegNode(5u) to listOf(0xBEu),    // rsi <- imm32\64
        DGeneralRegNode(6u) to listOf(0xBFu),    // rdi <- imm32\64
    )



    override fun processMoveCommand(from: DMoveCommandNode): List<UInt> {
        val native_commands = mutableListOf<UInt>()

        when (from.receiver) {
             is IRegNode -> {
                 when (from.value) {
                    is DIntegerNode -> {
                        native_commands += MOV_REG_IMM32_OP_CODES[from.receiver]!!
                        native_commands += from.value.value.toLittleEndian()
                    }

                    is DIdNode -> {
                        println("Id here")
                    }
                }
            }

            is IVarNode -> {
                when (from.value) {
                    is DIntegerNode -> {
                        native_commands += MOV_REG_IMM32_OP_CODES[CURRENT_OS.OS_VARS[this]!![from.receiver]]!!
                        native_commands += from.value.value.toLittleEndian()
                    }

                    is DIdNode -> {
                        //native_commands += MOV_REG_IMM32_OP_CODES[CURRENT_OS.OS_VARS[this]!![from.receiver]]!!
                        // TODO: Create translate from ID to memory address
                    }
                }
            }
        }

        return native_commands
    }
}