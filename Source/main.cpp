#include "Lexer/Lexer.hpp"

int main(){
    const auto txt = R"(
/ Only for UNIX;
block start {
    move: %syscall_id, 0;       / exit;
    move: %syscall_arg1, 0;     / 0;

    syscall;                    / system call - exit(0);
}
)";
    auto lex = Lexer::Lexer(txt);

    uint ii = 0;
    for(const auto& i : lex.tokenize()) {
        ii++;
        std::cout << ii << ") ";

        for( const auto&[type, value] : i) {
            std::string type_str;

            switch(type) {
                case Lexer::TokenType::LEFT_FIGURE_BRACKETS:
                    type_str = "{";
                    break;
                case Lexer::TokenType::RIGHT_FIGURE_BRACKETS:
                    type_str = "}";
                    break;
                case Lexer::TokenType::VAR_PREFIX:
                    type_str = "%";
                    break;
                case Lexer::TokenType::VAR_AND_ARGS_DIVIDER:
                    type_str = ":";
                    break;
                case Lexer::TokenType::ARGS_DIVIDER:
                    type_str = ",";
                    break;

                default: ;
            }
            std::cout << type_str << value << " ";
        }

        std::cout << std::endl;
    }
    

    return 0;
}