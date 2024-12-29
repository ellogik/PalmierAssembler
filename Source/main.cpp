#include "Lexer/Lexer.hpp"
#include <iostream>

using namespace PalmierAssembler::Lexer;

int main() {
    const auto txt = R"(
/ Only for UNIX;
block start {;
    move: %syscall_id, 0;       / exit;
    move: %syscall_arg1, 0;     / 0;

    syscall;                    / system call - exit(0);
};
)";
    const auto lex = Lexer(txt);

    uint line_number = 0;
    for (const auto& tokens_per_line : lex.tokenize()) {
        line_number++;
        std::cout << line_number << ") ";

        for (const auto& [type, value] : tokens_per_line) {
            std::string type_str;

            switch (type) {
                case TokenType::START_CODE_SPACE: type_str = "{"; break;
                case TokenType::END_CODE_SPACE: type_str = "}"; break;
                case TokenType::VAR_PREFIX: type_str = "%"; break;
                case TokenType::CMD_AND_ARGS_DIVIDER: type_str = ":"; break;
                case TokenType::ARGS_DIVIDER: type_str = ","; break;
                case TokenType::KEYWORD_BLOCK: type_str = "BLOCK"; break;
                case TokenType::COMMAND_MOVE: type_str = "MOVE"; break;
                case TokenType::COMMAND_SYSCALL: type_str = "SYSCALL"; break;

                default: break;
            }

            std::cout << type_str << value << " ";
        }

        std::cout << std::endl;
    }

    return 0;
}
