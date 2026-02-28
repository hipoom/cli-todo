package com.hipoom.cli.todo.handler.path

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/3/2 20:11
 *
 */

val pathOptions: Options = Options()
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .hasArg(false)
            .desc("Help")
            .build()
    )

    .addOption(
        Option.builder("i")
            .longOpt("items")
            .desc("Print items file path")
            .build()
    )
    .addOption(
        Option.builder("c")
            .longOpt("configs")
            .desc("Print config file path")
            .build()
    )
