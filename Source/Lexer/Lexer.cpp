#include "Lexer.hpp"

namespace Lexer {
    Lexer::Lexer(std::string text) : text(std::move(text)) {
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

        uint for_position = 0; // Index for local for per char in line

        for( const auto& line : lines) {
            std::vector<Token> tokens_per_line;

            for( const auto& character : line ) {
                for_position++;
                if( for_position < position_in_line ) continue;

                // Checking is it special token
                if( isalpha(character) ) {
                    tokens_per_line.push_back(processIdentifier(line));
                    continue;
                }
                if( isdigit(character) ) {
                    tokens_per_line.push_back(processNumber(line));
                    continue;
                }

                switch (processSimple(character, tokens_per_line)) {
                    case 1: continue;
                    case 2: goto continue_line;
                    default:;
                }
            }

            tokens.push_back(tokens_per_line); // Add tokenized line to tokenized text

            continue_line:; // For comments skips

            // Set current index in line as 0
            for_position = 0;
            position_in_line = 0;
        }

        return tokens;
    }

    Token Lexer::processIdentifier(const std::string &line) {
        uint for_position = 0; // Index for local for
        std::string buffer; // Here is line

        for ( const auto& character : line ) {
            // Skip to current position
            {
                for_position++;

                if( for_position < position_in_line) continue;
            }



            position_in_line = for_position;

            if( (isalpha(character) || isdigit(character) || character == '_') && character != ':' && character != ',' ) {
                buffer += character; // Add character to buffer if it is digit or letter
            } else {
                if(character != ':' && character != ',') position_in_line++; // Skip to next char
                break; // End searching identifier
            }
        }


        return Token{TokenType::IDENTIFIER, buffer};
    }

    Token Lexer::processNumber(const std::string &line) {
        uint for_position = 0; // Index for local for
        std::string buffer; // Here is line

        for ( const auto& character : line ) {
            // Skip to current position
            {
                for_position++;

                if( for_position < position_in_line) continue;
            }

            position_in_line = for_position;

            if( isdigit(character) || character == '.' ) {
                buffer += character; // Add character to buffer if it is digit or '.'
            } else {
                break; // End searching identifier
            }
        }

        position_in_line++; // Skip to next char

        return Token{TokenType::IDENTIFIER, buffer};
    }

    short Lexer::processSimple(char character, std::vector<Token> &tokens_per_line) {
        switch( character ) {
            // Unnecessary things for compiler(only important for Programmers)
            case ' ':
            case '\t':
            case '\r':
            case '\n':
                position_in_line++;
                return 1; // Code: Skip

            // Space for code
            case '{':
                tokens_per_line.push_back(Token {TokenType::LEFT_FIGURE_BRACKETS, ""});
                break;
            case '}':
                tokens_per_line.push_back(Token {TokenType::RIGHT_FIGURE_BRACKETS, ""});
                break;

            case ':':
                tokens_per_line.push_back(Token {TokenType::VAR_AND_ARGS_DIVIDER, ""});
                break;

            case '%':
                tokens_per_line.push_back(Token {TokenType::VAR_PREFIX, ""});
                break;

            case ',':
                tokens_per_line.push_back(Token {TokenType::ARGS_DIVIDER, ""});
                break;

            // Comments
            case '/':
                return 2; // Code: Comment

            // Throw error if any unknown characters
            default: throw std::invalid_argument(std::format("Invalid character: '{}'", character));
        }

        return 0;
    }
}
