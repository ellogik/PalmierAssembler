package lexicalization

enum class ETokenType {
    IDENTIFIER,
    NUMBER,

    CMD_AND_ARGS_DIVIDER,               // Symbol: ':'
    ARGS_DIVIDER,                       // Symbol: ','

    START_CODE_SPACE,                   // Symbol: '{'
    END_CODE_SPACE,                     // Symbol: '}'

    VAR_PREFIX,                         // Symbol: '%'
    REG_PREFIX,                         // Symbol: '$'

    KW_BLOCK,                           // Keyword: 'block'

    CMD_MOVE,                           // Command: 'move'
    CMD_SYSTEM_CALL,                    // Command: 'systemCall'


    INVALID_TOKEN
}