package native_code_generation.helpers

import parsing.nodes.regs_and_vars.DGeneralRegNode
import parsing.nodes.regs_and_vars.types.IOsVarNode
import parsing.nodes.regs_and_vars.types.IRegNode

abstract class AOperatingSystem {
    abstract val OS_VARS: Map<AArchitecture, Map<IOsVarNode, IRegNode>>
}