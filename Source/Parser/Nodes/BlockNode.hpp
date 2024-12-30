#pragma once

#include <string>
#include <utility>
#include <vector>

#include "ASTNode.hpp"

namespace PalmierAssembler::Parser::Nodes {
    class BlockNode final : public ASTNode {
    public:
        std::string name;
        std::vector<ASTNode> children;

        BlockNode(std::string name, const std::vector<ASTNode>& children) : name(std::move(name)), children(children) {}
    };
}

