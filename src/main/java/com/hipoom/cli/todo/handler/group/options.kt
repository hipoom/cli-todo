package com.hipoom.cli.todo.handler.group

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

val groupOptions: Options = Options()
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .hasArg(false)
            .desc("Help")
            .build()
    )

    .addOption(
        Option.builder("o")
            .longOpt("owner")
            .desc("Group by owner on show")
            .build()
    )
    .addOption(
        Option.builder("t")
            .longOpt("tree")
            .desc("Group by parent-child relationship on show")
            .build()
    )
