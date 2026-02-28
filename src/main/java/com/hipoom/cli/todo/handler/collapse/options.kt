package com.hipoom.cli.todo.handler.collapse

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/3/2 19:54
 *
 */


val collapseOptions: Options = Options()
    .addOption(
        Option.builder("i")
            .longOpt("id")
            .hasArg()
            .valueSeparator(',')
            .desc("Collapse items")
            .build()
    )
    .addOption(
        Option.builder("a")
            .longOpt("all")
            .desc("Collapse all items")
            .build()
    )
    .addOption(
        Option.builder("c")
            .longOpt("all")
            .desc("Collapse all items")
            .build()
    )
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .hasArg(false)
            .desc("Help")
            .build()
    )