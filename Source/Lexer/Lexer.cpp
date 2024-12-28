#include "Lexer.hpp"

namespace Lexer {
    Lexer::Lexer(const std::string &text) {
        this->text = text;
    }

    std::vector<std::vector<Token>> Lexer::tokenize() {
        std::vector<std::vector<Token>> tokens;
        std::vector<std::string> lines;

        // Split text for lines
        {
            std::stringstream stream(text);
            std::string temp;

            while (std::getline(stream, temp, ';')) {
                lines.push_back(temp);
            }
        }

        uint for_position = 0;

        for( const auto& line : lines) {
            std::vector<Token> tokens_per_line;

            for( const auto& character : line ) {
                for_position++;
                if( for_position < position_in_line ) continue;


                if( isalpha(character) ) {
                    tokens_per_line.push_back(processIdentifier(line));
                    continue;
                }

                switch( character ) {
                    case ' ':
                    case '\t':
                    case '\r':
                    case '\n':
                        position_in_line++;
                        continue;

                    case '{':
                        tokens_per_line.push_back(Token {TokenType::LEFT_FIGURE_BRACKETS, ""});
                        break;

                    case '}':
                        tokens_per_line.push_back(Token {TokenType::RIGHT_FIGURE_BRACKETS, ""});
                        break;


                    default: throw std::invalid_argument("Invalid character");
                }
            }

            tokens.push_back(tokens_per_line);

            for_position = 0;
            position_in_line = 0;
        }

        return tokens;
    }

    Token Lexer::processIdentifier(const std::string &line) {
        uint for_position = 0;
        std::string buffer;

        for ( const auto& character : line ) {
            for_position++;

            if( for_position < position_in_line) continue;

            position_in_line = for_position;

            if( isalpha(character) || isdigit(character) ) {
                buffer += character;
            } else {
                break;
            }
        }

        position_in_line++;

        return Token{TokenType::IDENTIFIER, buffer};
    }

}
