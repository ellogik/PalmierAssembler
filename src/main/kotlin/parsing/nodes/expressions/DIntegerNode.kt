package parsing.nodes.expressions

import parsing.nodes.AASTNode

data class DIntegerNode(
    val value: Int
) : AASTNode()