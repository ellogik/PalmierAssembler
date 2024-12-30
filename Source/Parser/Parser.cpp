#include "Parser.hpp"

#include <iostream>
#include <stdexcept>
#include <utility>

namespace PalmierAssembler::Parser {

    Parser::Parser(std::vector<std::vector<Lexer::Token>> tokens) : tokens(std::move(tokens)) {}

    std::vector<Nodes::ASTNode> Parser::parse() {
        std::vector<Nodes::ASTNode> nodes;

        for (line_index = 0; line_index < tokens.size(); ++line_index) {
            const auto& line = tokens[line_index];

            for (const auto& [type, value] : line) {
                if (type == Lexer::TokenType::KEYWORD_BLOCK) {
                    nodes.push_back(parseBlock());
                }
            }
        }

        return nodes;
    }

    Nodes::BlockNode Parser::parseBlock() {
        std::vector<Nodes::ASTNode> nodes;
        std::string name;

        uint layer = 0;
        bool is_block_before = false;

        for (; line_index < tokens.size(); ++line_index) {
            const auto& line = tokens[line_index];

            for (const auto& [type, value] : line) {
                if (type == Lexer::TokenType::KEYWORD_BLOCK && layer == 0) {
                    is_block_before = true;
                } else if (type == Lexer::TokenType::IDENTIFIER && is_block_before && layer == 0) {
                    is_block_before = false;
                    name = value;
                } else if (type == Lexer::TokenType::START_CODE_SPACE) {
                    ++layer;
                    nodes = parseCommands(layer);
                    break;
                }
            }

            if (!nodes.empty() || !name.empty()) break;
        }

        if (name.empty()) {
            throw std::invalid_argument("Invalid block name");
        }

        return Nodes::BlockNode{name, nodes};
    }

    std::vector<Nodes::ASTNode> Parser::parseCommands(uint& layer) {
        std::vector<Nodes::ASTNode> nodes;
        auto current_command = Lexer::TokenType::INVALID_TOKEN;

        for (; line_index < tokens.size(); ++line_index) {
            const auto& line = tokens[line_index];

            for (const auto& [type, value] : line) {
                if (type == Lexer::TokenType::END_CODE_SPACE && layer == 1) {
                    --layer;
                    return nodes;
                }

                switch (current_command) {
                    case Lexer::TokenType::COMMAND_MOVE:
                        // Handle COMMAND_MOVE logic
                        current_command = Lexer::TokenType::INVALID_TOKEN;
                        break;

                    case Lexer::TokenType::COMMAND_SYSCALL:
                        nodes.emplace_back(Nodes::SyscallCommandNode());
                        current_command = Lexer::TokenType::INVALID_TOKEN;
                        break;

                    case Lexer::TokenType::INVALID_TOKEN:
                        if (type == Lexer::TokenType::COMMAND_MOVE || type == Lexer::TokenType::COMMAND_SYSCALL) {
                            current_command = type;
                        }
                        break;

                    default:
                        break;
                }
            }
        }

        return nodes;
    }
}
