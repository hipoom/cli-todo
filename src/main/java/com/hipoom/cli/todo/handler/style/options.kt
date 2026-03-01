package com.hipoom.cli.todo.handler.style

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

val styleOptions: Options = Options()
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .desc("help")
            .build()
    )
    .addOption(
        Option.builder()
            .longOpt("set")
            .desc("Set style")
            .build()
    )
    .addOption(
        Option.builder("l")
            .longOpt("list")
            .desc("List all available styles")
            .build()
    )
    .addOption(
        Option.builder("c")
            .longOpt("create")
            .desc("Create a new style.")
            .build()
    )
    .addOption(
        Option.builder("d")
            .longOpt("delete")
            .hasArg(true)
            .desc("Delete an existing style")
            .build()
    )
    .addOption(
        Option.builder()
            .longOpt("detail")
            .hasArg(true)
            .desc("Show details of a specific style")
            .build()
    )
    .addOption(
        Option.builder("e")
            .longOpt("edit")
            .hasArg(true)
            .desc("Edit an existing style.")
            .build()
    )
