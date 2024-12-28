#include "Lexer/Lexer.hpp"

int main(){
    auto lex = Lexer::Lexer("Hello; World; ds sad {}");

    uint ii = 0;
    for(const auto& i : lex.tokenize()) {
        ii++;
        std::cout << ii << ") ";
        for( const auto& j : i) {
            std::cout << j.value << " ";
        }
        std::cout << std::endl;
    }
    

    return 0;
}