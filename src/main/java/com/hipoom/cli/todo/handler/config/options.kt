package com.hipoom.cli.todo.handler.config

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/3/2 19:54
 *
 */


val configOptions: Options = Options()
    .addOption(
        Option.builder("e")
            .longOpt("edit")
            .desc("Edit configs")
            .build()
    )
    .addOption(
        Option.builder("s")
            .longOpt("show")
            .desc("Show configs")
            .build()
    )
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .hasArg(false)
            .desc("Help")
            .build()
    )