#![allow(dead_code)]

use std::fs;
use crate::lexerization::Lexer;
use crate::native_code_generation::CodeGenerator;
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
    parser.parse();

    let arch = ArchitecturePlugin::fromPythonScript("examples/plugins/x86-64.py".to_string()).expect("FAIL::ARCH");
    let mut code_gen = CodeGenerator::new(arch, parser.top_layer_nodes);

    match code_gen.compile() {
        Ok(output) => {
            println!("OK!");
        }
        Err(err) => {
            println!("{:#?}", err);
        }
    };
}
