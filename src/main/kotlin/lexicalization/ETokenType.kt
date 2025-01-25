package lexicalization

enum class ETokenType {
    IDENTIFIER,
    STRING,
    NUMBER,

    MINUS,                              // Symbol: '-'
    PLUS,                               // Symbol: '+'
    MULTIPLY,                           // Symbol: '*'
    DIVIDE,                             // Symbol: '\'

    CMD_AND_ARGS_DIVIDER,               // Symbol: ':'
    ARGS_DIVIDER,                       // Symbol: ','

    START_CODE_SPACE,                   // Symbol: '{'
    END_CODE_SPACE,                     // Symbol: '}'

    VAR_PREFIX,                         // Symbol: '%'
    REG_PREFIX,                         // Symbol: '$'

    KW_BLOCK,                           // Keyword: 'block'
    KW_MACRO,                           // Keyword: 'macro'
    KW_GROUP,                           // Keyword: 'group'
    KW_PERMANENT,                       // Keyword: 'permanent'
    KW_TERM,                            // Keyword: 'term'

    CMD_MOVE,                           // Command: 'move'
    CMD_SYSTEM_CALL,                    // Command: 'systemCall'

    INVALID_TOKEN
}