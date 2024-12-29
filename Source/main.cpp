#include "Lexer/Lexer.hpp"
#include <iostream>

int main() {
    const auto txt = R"(
/ Only for UNIX;
block start {;
    move: %syscall_id, 0;       / exit;
    move: %syscall_arg1, 0;     / 0;

    syscall;                    / system call - exit(0);
};
)";
    const auto lex = Lexer::Lexer(txt);

    uint line_number = 0;
    for (const auto& tokens_per_line : lex.tokenize()) {
        line_number++;
        std::cout << line_number << ") ";

        for (const auto& [type, value] : tokens_per_line) {
            std::string type_str;

            switch (type) {
                case Lexer::TokenType::LEFT_FIGURE_BRACKETS: type_str = "{"; break;
                case Lexer::TokenType::RIGHT_FIGURE_BRACKETS: type_str = "}"; break;
                case Lexer::TokenType::VAR_PREFIX: type_str = "%"; break;
                case Lexer::TokenType::CMD_AND_ARGS_DIVIDER: type_str = ":"; break;
                case Lexer::TokenType::ARGS_DIVIDER: type_str = ","; break;
                default: break;
            }

            std::cout << type_str << value << " ";
        }

        std::cout << std::endl;
    }

    return 0;
}
