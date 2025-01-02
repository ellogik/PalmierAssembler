#![allow(non_snake_case, dead_code, non_camel_case_types)]

use crate::lexerization::token::{Token, TokenType};
use crate::lexerization::TokenType::INVALID_TOKEN;

static KEYWORDS: [&str; 3] = ["block", "move", "syscall"];

pub struct Lexer {
    input: String,
    in_line_index: usize,
    tokens: Vec<Vec<Token>>,
}

impl Lexer {
    pub fn new(input: String) -> Self {
        Self {
            input,
            in_line_index: 0,
            tokens: vec!(),
        }
    }

    pub fn tokenize(&mut self) -> Vec<Vec<Token>> {
        let lines = self.splitToLines();

        let mut for_index = 0;

        'LINE_MARK: for line in lines {
            let mut tokens_per_line: Vec<Token> = vec![];

            for char in line.chars() {
                if for_index != self.in_line_index {
                    for_index += 1;
                    continue;
                }

                match char {
                    '/' => {
                        // Handle comments
                        if !tokens_per_line.is_empty() {
                            self.tokens.push(tokens_per_line);
                        }

                        for_index = 0;
                        self.in_line_index = 0;
                        continue 'LINE_MARK;
                    }


                    char if char.is_whitespace() => {}

                    char if char.is_alphabetic() => {
                        let identifier = self.processIdentifier(char, &line);

                        if KEYWORDS.contains(&identifier.to_ascii_lowercase().as_str()) {
                            tokens_per_line.push(
                                Token::new(
                                    Self::processKeyword(identifier),
                                    "".to_string()
                                )
                            )
                        }
                        else {
                            tokens_per_line.push(Token::new(TokenType::IDENTIFIER, identifier));
                        }
                    }

                    char if char.is_ascii_digit() => {
                        let number = self.processNumber(char, &line);
                        tokens_per_line.push(Token::new(TokenType::NUMBER, number));
                    }

                    _ => {
                        tokens_per_line.push(
                            Token::new(
                                Self::processSimple(char),
                                "".to_string()
                            )
                        )
                    }
                }

                self.in_line_index += 1;
                for_index += 1;
            }

            if !tokens_per_line.is_empty() {
                self.tokens.push(tokens_per_line);
            }

            for_index = 0;
            self.in_line_index = 0;
        }

        self.tokens.clone()
    }

    fn splitToLines(&self) -> Vec<String> {
        let mut strings = vec![];
        let mut buffer = String::new();

        for char in self.input.chars() {
            if char == '{' || char == '}' {
                buffer.push(char);

                if !buffer.is_empty() {
                    strings.push(buffer.clone());
                }
                buffer.clear();

                continue;
            }
            if char == ';' {
                strings.push(buffer.clone());
                buffer.clear();
                continue;
            }

            buffer.push(char);
        }

        if !buffer.is_empty() {
            strings.push(buffer);
        }

        strings
    }

    fn processIdentifier(&mut self, first_char: char, line: &str) -> String {
        let mut buffer = String::new();
        buffer.push(first_char);

        let mut chars = line.chars().skip(self.in_line_index + 1);
        while let Some(c) = chars.next() {
            if (c.is_alphanumeric() || c == '_') && c != ':' {
                buffer.push(c);
            } else {
                break;
            }
        }

        self.in_line_index += buffer.len() - 1;

        buffer
    }

    fn processNumber(&mut self, first_char: char, line: &str) -> String {
        let mut buffer = String::new();
        buffer.push(first_char);

        let mut chars = line.chars().skip(self.in_line_index + 1);
        while let Some(c) = chars.next() {
            if c.is_digit(10) || c == '-' || c == '.' {
                buffer.push(c);
            } else {
                break;
            }
        }

        self.in_line_index += buffer.len() - 1;

        buffer
    }

    fn processSimple(char: char) -> TokenType {
        match char {
            '{' => TokenType::START_CODE_SPACE,
            '}' => TokenType::END_CODE_SPACE,
            ':' => TokenType::CMD_AND_ARGS_DIVIDER,
            ',' => TokenType::ARGS_DIVIDER,
            '%' => TokenType::VAR_PREFIX,
            '$' => TokenType::REG_PREFIX,

            _ => INVALID_TOKEN // Handle other characters here if necessary
        }
    }

    fn processKeyword(keyword: String) -> TokenType {
        match keyword.to_ascii_lowercase().as_str() {
            "block" => TokenType::KW_BLOCK,
            "move" => TokenType::CMD_MOVE,
            "syscall" => TokenType::CMD_SYSCALL,

            _ => INVALID_TOKEN
        }
    }
}

