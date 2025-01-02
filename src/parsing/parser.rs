#![allow(dead_code, non_snake_case, unused_variables, unused)]

use crate::lexerization::{Token, TokenType};
use crate::lexerization::TokenType::NUMBER;
use crate::parsing::{BlockNode, GeneralRegNode, InvalidNode, MoveCommandNode, NumberNode, NumberType, SyscallArgNode, SyscallCommandNode, SyscallIdNode};
use crate::parsing::errors::UnknownCommandError;
use crate::parsing::nodes::ASTNode;

pub struct Parser {
    input: Vec<Vec<Token>>,
    line_position: usize,

    pub top_layer_nodes: Vec<Box<dyn ASTNode>>
}

impl Parser {
    pub fn new(input: Vec<Vec<Token>>) -> Self
    {
        Self {
            input,
            line_position: 0,
            top_layer_nodes: Vec::new()
        }
    }

    pub fn parse(&mut self) -> &Vec<Box<dyn ASTNode>>
    {
        let mut for_index: usize = 0;

        for line in self.input.clone() {
            if for_index < self.line_position { for_index += 1; continue }

            for token in line {
                match token.token_type {
                    TokenType::KW_BLOCK => {
                        let node = Box::new(self.parseBlock());

                        self.top_layer_nodes.push(node)
                    }
                    TokenType::KW_MACRO => {

                    }

                    _ => {
                        // error
                    }
                }
            }

            self.line_position += 1;
            for_index += 1;
        }

        &self.top_layer_nodes
    }

    fn parseBlock(&mut self) -> BlockNode {
        let mut name = String::new();
        let mut nodes_in: Vec<Box<dyn ASTNode>> = Vec::new();

        let mut for_index: usize = 0;

        'LINE_MARK: for line in self.input.clone() {
            if for_index < self.line_position {
                for_index += 1;
                continue
            }

            let mut is_block_before = false;
            let mut is_name_before = false;

            for token in line {
                if token.token_type == TokenType::KW_BLOCK {
                    is_block_before = true;
                }
                if token.token_type == TokenType::IDENTIFIER && is_block_before {
                    name = token.value;
                    is_block_before = false;
                    is_name_before = true;
                }
                if token.token_type == TokenType::START_CODE_SPACE && is_name_before {
                    self.line_position += 1;
                    for_index += 1;
                    is_name_before = false;

                    match self.parseCommands() {
                        Ok(ok) => {nodes_in = ok}
                        Err(e) => {
                            println!("{}", e);

                            break 'LINE_MARK;
                        }
                    }

                    break 'LINE_MARK
                }
            }

            self.line_position += 1;
            for_index += 1;
        }

        BlockNode::new(name, nodes_in)
    }

    fn parseCommands(&mut self) -> Result<Vec<Box<dyn ASTNode>>, UnknownCommandError> {
        let mut nodes: Vec<Box<dyn ASTNode>> = Vec::new();

        let mut for_index: usize = 0;

        let lines = self.input.clone();

        for line in lines {
            if for_index < self.line_position { for_index += 1; continue }

            match &line[0].token_type {
                TokenType::CMD_MOVE => {
                    let i= self.parseMoveCommand();
                    nodes.push(Box::new(i))
                }
                TokenType::CMD_SYSCALL => {
                    nodes.push(Box::new(SyscallCommandNode::new()))
                }

                TokenType::END_CODE_SPACE => {
                    break
                }

                _e => {
                    return Err(
                        UnknownCommandError::new(
                            String::from("main.plmr.pasm"),
                            self.line_position,
                            line[0].value.clone()
                        )
                    )
                }
            }

            for_index += 1;
            self.line_position += 1;
        }

        Ok(nodes)
    }

    fn parseMoveCommand(&mut self) -> MoveCommandNode {
        let mut node_receiver: Box<dyn ASTNode> = Box::new(InvalidNode::new());
        let mut node_value: Box<dyn ASTNode> = Box::new(InvalidNode::new());

        let mut for_index: usize = 0;

        for line in self.input.clone() {
            if for_index < self.line_position { for_index += 1; continue }

            if !(line[0].token_type == TokenType::CMD_MOVE || line[1].token_type == TokenType::CMD_AND_ARGS_DIVIDER) {
                // error
                break
            }

            let args_token: Vec<Token> = line
                .into_iter()
                .skip_while(|token| token.token_type != TokenType::CMD_AND_ARGS_DIVIDER)
                .skip(1)
                .collect();

            let mut iter = args_token.split(|token| token.token_type == TokenType::ARGS_DIVIDER);

            let arg1_tkn = iter.next().unwrap_or(&[]).to_vec();
            let arg2_tkn = iter.next().unwrap_or(&[]).to_vec();

            node_receiver = self.parseExpression(arg1_tkn);
            node_value = self.parseExpression(arg2_tkn);

            break
        };


        MoveCommandNode::new(node_receiver, node_value)
    }

    fn parseExpression(&mut self, expression: Vec<Token>) -> Box<dyn ASTNode> {
        if expression[0].token_type == TokenType::VAR_PREFIX || expression[0].token_type == TokenType::REG_PREFIX {
            if expression[1].token_type == TokenType::IDENTIFIER {
                match expression[1].value.as_str() {
                    "syscall_id" => {
                        return Box::new(SyscallIdNode::new())
                    }

                    _ => {
                        if expression[1].value.starts_with("syscall_arg") {
                            if let Ok(num) = expression[1].value["syscall_arg".len()..].parse::<u8>() {
                                return Box::new(SyscallArgNode::new(num));
                            }
                        }
                        if expression[1].value.starts_with("general_reg") {
                            if let Ok(num) = expression[1].value["general_reg".len()..].parse::<u8>() {
                                return Box::new(GeneralRegNode::new(num));
                            }
                        }
                    }
                }
            } else {
                // err
            }
        }
        if expression[0].token_type == NUMBER && expression.len() == 1 {
            return if expression[0].value.contains('.') {
                Box::new(NumberNode::new(expression[0].clone().value, NumberType::FLOAT))
            } else {
                Box::new(NumberNode::new(expression[0].clone().value, NumberType::INTEGER))
            }
        }

        Box::new(InvalidNode::new())
    }
}