package com.hipoom.cli.todo.handler.detail

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/3/2 19:54
 *
 */


val detailOptions: Options = Options()
    .addOption(
        Option.builder("i")
            .longOpt("id")
            .hasArg(true)
            .desc("Show the item detail.")
            .build()
    )
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .desc("help")
            .build()
    )