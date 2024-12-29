#pragma once

#include <string>

namespace Lexer {

    // For describe for what token
    enum class TokenType {
        IDENTIFIER,                                       // ID of objects, example: block >start<
        NUMBER,                                           // Number, example:  0, -13, 32, 561, 1000123

        // Figure brackets - {}
        LEFT_FIGURE_BRACKETS,                             // Symbol: '{'
        RIGHT_FIGURE_BRACKETS,                            // Symbol: '}'

        VAR_AND_ARGS_DIVIDER,                             // Symbol: ':'
        VAR_PREFIX,                                       // Symbol: '%'
        ARGS_DIVIDER,                                     // Symbol: ','

        COMMAND_MOVE,                                     // Command: 'move'
        COMMAND_SYSCALL,                                  // Command: 'syscall'

        VARIABLE_SYSCALL_ID,                              // Variable: 'syscall_id'
        VARIABLE_SYSCALL_ARG,                             // Variable: 'syscall_arg*'

    };

    struct Token {
    public:
        TokenType type;
        std::string value;
    };
}
