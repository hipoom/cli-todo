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
        Option.builder("s")
            .longOpt("set")
            .hasArg(true)
            .desc("Set color style (default, dark, light, colorful)")
            .build()
    )
    .addOption(
        Option.builder("l")
            .longOpt("list")
            .desc("List all available styles")
            .build()
    )
    .addOption(
        Option.builder("a")
            .longOpt("add")
            .hasArg(true)
            .desc("Add a new style (format: name,desc,textColor,bgColor,pinColor)")
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
        Option.builder("show")
            .longOpt("show")
            .hasArg(true)
            .desc("Show details of a specific style")
            .build()
    )
    .addOption(
        Option.builder("e")
            .longOpt("edit")
            .hasArg(true)
            .desc("Edit an existing style (format: name,desc,textColor,bgColor,pinColor)")
            .build()
    )
