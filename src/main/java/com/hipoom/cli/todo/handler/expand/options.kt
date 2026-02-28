package com.hipoom.cli.todo.handler.expand

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/3/2 19:54
 *
 */


val expandOptions: Options = Options()
    .addOption(
        Option.builder("i")
            .longOpt("id")
            .hasArg()
            .valueSeparator(',')
            .desc("Expand items")
            .build()
    )
    .addOption(
        Option.builder("a")
            .longOpt("all")
            .desc("Expand all items")
            .build()
    )
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .hasArg(false)
            .desc("Help")
            .build()
    )