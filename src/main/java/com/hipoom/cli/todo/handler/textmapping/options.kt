package com.hipoom.cli.todo.handler.textmapping

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

val textMappingOptions: Options = Options()
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .desc("Print help information")
            .build()
    )
    .addOption(
        Option.builder("l")
            .longOpt("list")
            .desc("List all text mappings")
            .build()
    )
    .addOption(
        Option.builder()
            .longOpt("add")
            .hasArgs()
            .numberOfArgs(2)
            .desc("Add a new text mapping: --add \"original\" \"replacement\"")
            .build()
    )
    .addOption(
        Option.builder("d")
            .longOpt("delete")
            .hasArg(true)
            .desc("Delete a text mapping by original text")
            .build()
    )
