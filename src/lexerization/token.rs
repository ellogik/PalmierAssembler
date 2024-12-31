#![allow(non_snake_case, dead_code, non_camel_case_types)]

#[derive(Debug, PartialEq, Eq, Clone)]
pub enum TokenType {
    IDENTIFIER,                                     // ID of objects, example: block >start<
    NUMBER,                                         // Number, example:  0, -13, 32, 561, 1000123

    // Figure brackets - {}
    START_CODE_SPACE,                               // Symbol: '{'
    END_CODE_SPACE,                                 // Symbol: '}'

    CMD_AND_ARGS_DIVIDER,                           // Symbol: ':'
    VAR_PREFIX,                                     // Symbol: '%'
    ARGS_DIVIDER,                                   // Symbol: ','

    CMD_MOVE,                                       // Command: 'move'
    CMD_SYSCALL,                                    // Command: 'syscall'

    KW_BLOCK,                                       // Keyword: 'block'
    KW_MACRO,                                       // Keyword: 'macro'

    INVALID_TOKEN
}

#[derive(Debug, Clone)]
pub struct Token {
    pub token_type: TokenType,
    pub value: String
}

impl Token {
    pub fn new(token_type: TokenType, value: String) -> Token { Self {token_type: token_type, value: value} }
}