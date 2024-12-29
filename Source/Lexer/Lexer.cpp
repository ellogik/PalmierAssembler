#include "Lexer.hpp"
#include <sstream>
#include <stdexcept>
#include <format>

namespace PalmierAssembler::Lexer {
    Lexer::Lexer(std::string text) : text(std::move(text)) {}

    std::vector<std::vector<Token>> Lexer::tokenize() const
    {
        std::vector<std::vector<Token>> tokens;
        std::vector<std::string> lines;

        // Divide by lines
        {
            std::stringstream stream(text);
            std::string temp;

            while (std::getline(stream, temp, ';')) {
                lines.push_back(temp);
            }
        }

        // Every line validation
        for (const auto& line : lines) {
            std::vector<Token> tokens_per_line; // Tokens in current line

            // Every char validation
            for (size_t i = 0; i < line.size(); ++i) {
                const char character = line[i];

                // IDs validation
                if (isalpha(character)) {
                    tokens_per_line.push_back(processIdentifier(line, i));
                    continue;
                }

                // Digits validation
                if (isdigit(character)) {
                    tokens_per_line.push_back(processNumber(line, i));
                    continue;
                }

                // Simple tokens validation
                switch (processSimple(character, tokens_per_line)) {
                    case 1: continue; // Skip character
                    case 2: goto skip_line; // Skip line
                    default: break;
                }
            }

            skip_line:; // For skipping current line

            if(!tokens_per_line.empty())
                tokens.push_back(tokens_per_line);
        }

        return tokens;
    }

    Token Lexer::processIdentifier(const std::string& line, size_t& index)
    {
        std::string buffer;

        while (index < line.size() && (isalnum(line[index]) || line[index] == '_')) {
            buffer += line[index++];
        }
        --index; // Minus because outer loop pluses index

        if(auto temp_keyword = processKeyword(buffer); temp_keyword.type != TokenType::INVALID_TOKEN )
            return temp_keyword;


        return Token{TokenType::IDENTIFIER, buffer};
    }

    Token Lexer::processNumber(const std::string& line, size_t& index)
    {
        std::string buffer;

        while (index < line.size() && (isdigit(line[index]) || line[index] == '.')) {
            buffer += line[index++];
        }
        --index; // Minus because outer loop pluses index

        return Token{TokenType::NUMBER, buffer};
    }

    Token Lexer::processKeyword(const std::string& stroke) {
        if(stroke == "block") return Token { TokenType::KEYWORD_BLOCK, "" };
        if(stroke == "move") return Token { TokenType::COMMAND_MOVE, "" };
        if(stroke == "syscall") return Token { TokenType::COMMAND_SYSCALL, "" };

        return Token { TokenType::INVALID_TOKEN, "" };
    }

    short Lexer::processSimple(char character, std::vector<Token>& tokens_per_line)
    {
        switch (character) {
            // Useless things for compiler
            case ' ':
            case '\t':
            case '\r':
            case '\n':
                return 1; // Skip whitespace

            // Code space
            case '{':
                tokens_per_line.push_back(Token{TokenType::START_CODE_SPACE, ""});
                break;
            case '}':
                tokens_per_line.push_back(Token{TokenType::END_CODE_SPACE, ""});
                break;

            // Command and args divider
            case ':':
                tokens_per_line.push_back(Token{TokenType::CMD_AND_ARGS_DIVIDER, ""});
                break;

            // Variables prefix
            case '%':
                tokens_per_line.push_back(Token{TokenType::VAR_PREFIX, ""});
                break;

            // Args divider
            case ',':
                tokens_per_line.push_back(Token{TokenType::ARGS_DIVIDER, ""});
                break;

            // Comment
            case '/':
                return 2; // Skip line(comment)


            default:
                throw std::invalid_argument(std::format("Invalid character: '{}'", character));
        }


        return 0;
    }
}
