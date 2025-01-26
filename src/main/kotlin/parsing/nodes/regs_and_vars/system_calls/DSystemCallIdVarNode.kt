package parsing.nodes.regs_and_vars.system_calls

import parsing.nodes.AASTNode
import parsing.nodes.regs_and_vars.types.IOsVarNode
import parsing.nodes.regs_and_vars.types.IVarNode

class DSystemCallIdVarNode : AASTNode(), IOsVarNode {
    override fun equals(other: Any?) = other!!::class.java == this::class.java
    override fun hashCode() = javaClass.hashCode()
}
