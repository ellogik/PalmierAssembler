#![allow(dead_code)]

use std::fs;
use crate::lexerization::Lexer;
use crate::parsing::Parser;
use crate::plugins::ArchitecturePlugin;

mod lexerization;
mod parsing;
mod native_code_generation;
mod plugins;

fn main() {
    let _txt = fs::read_to_string("examples/projects/basic.plmr.pasm").expect("FAIL::TXT");

    let mut _lexer = Lexer::new(_txt);

    let mut parser = Parser::new(_lexer.tokenize());


    println!("{:#?}", parser.parse());
}
