package com.hipoom.cli.todo.handler.add

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

val addOptions: Options = Options()
    .addOption(
        Option.builder("p")
            .longOpt("parent")
            .hasArg(true)
            .valueSeparator(',')
            .type(Int::class.java)
            .desc("Add to the specified parent node")
            .build()
    )
    .addOption(
        Option.builder("t")
            .longOpt("template")
            .hasArg(false)
            .desc("Create a new item using the specified template")
            .build()
    )
    .addOption(
        Option.builder("a")
            .longOpt("advance")
            .hasArg(false)
            .desc("Create item using advance mode")
            .build()
    )
    .addOption(
        Option.builder("o")
            .longOpt("owner")
            .hasArg(true)
            .desc("Specify owner")
            .build()
    )
    .addOption(
        Option.builder("l")
            .longOpt("label")
            .hasArg(true)
            .valueSeparator(',')
            .desc("Specify label(s)")
            .build()
    )
    .addOption(
        Option.builder("d")
            .longOpt("deadline")
            .hasArg(true)
            .valueSeparator(' ')
            .desc("Specify deadline, such as 14:00 or +1d 14:00 etc.")
            .build()
    )
    .addOption(
        Option.builder("b")
            .longOpt("batch")
            .desc("Batch add items.")
            .build()
    )
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .hasArg(false)
            .desc("Help")
            .build()
    )
    .addOption(
        Option.builder("v")
            .longOpt("view")
            .hasArg(true)
            .desc("Add this item to the special virtual view.")
            .build()
    )
    .addOption(
        Option.builder("qm")
            .longOpt("quick-mode")
            .hasArg(true)
            .desc("Toggle quick mode. If true, you can add item without input 'add' command.")
            .build()
    )