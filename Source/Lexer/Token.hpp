#pragma once

#include <string>

namespace PalmierAssembler::Lexer {

    // For describe for what token
    enum class TokenType {
        IDENTIFIER,                                       // ID of objects, example: block >start<
        NUMBER,                                           // Number, example:  0, -13, 32, 561, 1000123

        // Figure brackets - {}
        START_CODE_SPACE,                             // Symbol: '{'
        END_CODE_SPACE,                            // Symbol: '}'

        CMD_AND_ARGS_DIVIDER,                             // Symbol: ':'
        VAR_PREFIX,                                       // Symbol: '%'
        ARGS_DIVIDER,                                     // Symbol: ','

        COMMAND_MOVE,                                     // Command: 'move'
        COMMAND_SYSCALL,                                  // Command: 'syscall'

        KEYWORD_BLOCK,                                    // Keyword: 'block'

        INVALID_TOKEN
    };

    struct Token {
        TokenType type;
        std::string value;
    };
}
