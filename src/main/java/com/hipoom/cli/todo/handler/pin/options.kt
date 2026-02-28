package com.hipoom.cli.todo.handler.pin

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/3/2 20:11
 *
 */

val pinOptions: Options = Options()
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
            .hasArg()
            .desc("Pin specified item(s)")
            .build()
    )
    .addOption(
        Option.builder("u")
            .longOpt("unpin")
            .hasArg()
            .desc("Unpin specified item(s)")
            .build()
    )
    .addOption(
        Option.builder("s")
            .longOpt("show")
            .desc("Show pins")
            .build()
    )