#![allow(dead_code)]

use crate::plugins::ArchitecturePlugin;

mod lexerization;
mod parsing;
mod native_code_generation;
mod plugins;

fn main() {
    let i = ArchitecturePlugin::fromPythonScript("examples/plugins/x86-64.py".to_string()).expect("FAIL");

    println!("{}", i.for_arch)

}
