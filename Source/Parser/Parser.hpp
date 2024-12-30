#pragma once

#include <vector>

#include "../Lexer/Token.hpp"
#include "Nodes/ASTNode.hpp"
#include "Nodes/BlockNode.hpp"
#include "Nodes/SyscallCommandNode.hpp"


namespace PalmierAssembler::Parser {
    class Parser {
    public:
        explicit Parser(std::vector<std::vector<Lexer::Token>> tokens);

        [[nodiscard]] std::vector<Nodes::ASTNode> parse();

    private:
        const std::vector<std::vector<Lexer::Token>> tokens;
        uint line_index = 0;

        Nodes::BlockNode parseBlock();
        std::vector<Nodes::ASTNode> parseCommands(uint& layer);

    };
}
