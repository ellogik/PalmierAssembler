package parsing.nodes.expressions

import parsing.nodes.AASTNode

data class DMinusNode(
    val minuend: AASTNode,
    val subtrahend: AASTNode
) : AASTNode()
