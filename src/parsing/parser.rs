#![allow(dead_code, non_snake_case)]

use crate::lexerization::{Token, TokenType};
use crate::lexerization::TokenType::NUMBER;
use crate::parsing::{BlockNode, InvalidNode, MoveCommandNode, NumberNode, NumberType, SyscallCommandNode, SyscallIdNode};
use crate::parsing::nodes::ASTNode;

pub struct Parser {
    input: Vec<Vec<Token>>,
    line_position: isize,


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
        let mut for_index: isize = 0;

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

        let mut for_index: isize = 0;

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

                    nodes_in = self.parseCommands();

                    break 'LINE_MARK
                }
            }

            self.line_position += 1;
            for_index += 1;
        }

        BlockNode::new(name, nodes_in)
    }

    fn parseCommands(&mut self) -> Vec<Box<dyn ASTNode>> {
        let mut nodes: Vec<Box<dyn ASTNode>> = Vec::new();

        let mut for_index: isize = 0;

        for line in self.input.clone() {
            if for_index < self.line_position { for_index += 1; continue }

            match line[0].token_type {
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

                _ => {
                    println!("Expected command or macro call in line {}", for_index + 1)
                }
            }

            for_index += 1;
            self.line_position += 1;
        }

        nodes
    }

    fn parseMoveCommand(&mut self) -> MoveCommandNode {
        let mut node_receiver: Box<dyn ASTNode> = Box::new(InvalidNode::new());
        let mut node_value: Box<dyn ASTNode> = Box::new(InvalidNode::new());

        let mut for_index: isize = 0;

        for line in self.input.clone() {
            if for_index < self.line_position { for_index += 1; continue }

            if !(line[0].token_type == TokenType::CMD_MOVE || line[1].token_type == TokenType::CMD_AND_ARGS_DIVIDER) {
                // error
                break
            }

            let args_token: Vec<Token> = line
                .into_iter()
                .skip_while(|token| token.token_type == TokenType::IDENTIFIER)
                .skip(1)
                .collect();

            let mut iter = args_token.split(|token| token.token_type == TokenType::ARGS_DIVIDER);

            let arg1_tkn = iter.next().unwrap_or(&[]).to_vec();
            let arg2_tkn = iter.next().unwrap_or(&[]).to_vec();

            node_receiver = self.parseExpression(arg1_tkn);
            node_value = self.parseExpression(arg2_tkn);

            break
        };

        self.line_position += 1;

        MoveCommandNode::new(node_receiver, node_value)
    }

    fn parseExpression(&mut self, expression: Vec<Token>) -> Box<dyn ASTNode> {
        if expression[0].token_type == TokenType::VAR_PREFIX {
            if expression[1].token_type == TokenType::IDENTIFIER {
                match expression[1].value.as_str() {
                    "syscall_id" => {
                        return Box::new(SyscallIdNode::new())
                    }

                    _ => {}
                }
            } else {
                // err
            }
        }
        if expression[0].token_type == NUMBER && expression.len() == 1 {
            return Box::new(NumberNode::new(expression[0].clone().value, NumberType::INTEGER));
        }

        Box::new(InvalidNode::new())
    }
}