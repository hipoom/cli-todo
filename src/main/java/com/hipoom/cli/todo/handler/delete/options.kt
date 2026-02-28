package com.hipoom.cli.todo.handler.delete

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/3/2 19:54
 *
 */


val deleteOptions: Options = Options()
    .addOption(
        Option.builder("i")
            .longOpt("id")
            .hasArg(true)
            .desc("Need delete item's id.")
            .build()
    )
    .addOption(
        Option.builder("p")
            .longOpt("physical")
            .desc("Delete physically.")
            .build()
    )
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .desc("help")
            .build()
    )