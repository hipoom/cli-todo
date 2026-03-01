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
