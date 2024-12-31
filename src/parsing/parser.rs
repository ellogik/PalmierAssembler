#![allow(dead_code, non_snake_case)]

use crate::lexerization::{Token, TokenType};
use crate::parsing::{BlockNode, MoveCommandNode, SyscallCommandNode};
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

        for line in self.input {
            if for_index < self.line_position { for_index += 1; continue }

            for token in line {
                match token.token_type {
                    TokenType::KW_BLOCK => {
                        self.top_layer_nodes.push(Box::new(self.parseBlock()))
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

        'LINE_MARK: for line in self.input {
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

        for line in self.input {
            if for_index < self.line_position { for_index += 1; continue }

            match line[0].token_type {
                TokenType::CMD_MOVE => {
                    let i= self.parseMoveCommand();
                }
                _ => {}
            }

            for_index += 1;
            self.line_position += 1;
        }

        nodes
    }

    fn parseMoveCommand(&mut self) -> MoveCommandNode {



        MoveCommandNode::new(Box::new(SyscallCommandNode::new()), Box::new(SyscallCommandNode::new()))
    }
}