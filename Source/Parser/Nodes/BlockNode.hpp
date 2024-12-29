#pragma once

#include <string>
#include <vector>

#include "ASTNode.hpp"

namespace PalmierAssembler::Parser::Nodes {
    struct BlockNode final : public ASTNode {
        std::string name;
        std::vector<ASTNode*> children;

        ~BlockNode() override = default;
    };
}

