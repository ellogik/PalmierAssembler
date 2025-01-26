package native_code_generation.helpers.operating_systems

import native_code_generation.helpers.AArchitecture
import native_code_generation.helpers.AOperatingSystem
import native_code_generation.helpers.architectures.ArchX86_64
import parsing.nodes.regs_and_vars.DGeneralRegNode
import parsing.nodes.regs_and_vars.system_calls.DSystemCallArgVarNode
import parsing.nodes.regs_and_vars.system_calls.DSystemCallIdVarNode
import parsing.nodes.regs_and_vars.types.IOsVarNode
import parsing.nodes.regs_and_vars.types.IRegNode

object OsLinux : AOperatingSystem(), IELFSupportInOs {
    override fun toELFAbi(): Byte = 3
    override val OS_VARS: Map<AArchitecture, Map<IOsVarNode, IRegNode>> = mapOf(
        ArchX86_64 to mapOf(
            DSystemCallIdVarNode() to DGeneralRegNode(1u),
            DSystemCallArgVarNode(1u) to DGeneralRegNode(6u),
            DSystemCallArgVarNode(2u) to DGeneralRegNode(5u),
            DSystemCallArgVarNode(3u) to DGeneralRegNode(3u)
        )
    )
}