#pragma once
#include "ASTNode.hpp"

namespace PalmierAssembler::Parser::Nodes {
    class MoveCommandNode final : public ASTNode {
    public:
        ASTNode *receiver, *sender;

        ~MoveCommandNode() override = default;
    };
}

