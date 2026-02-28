package com.hipoom.cli.todo.handler.owner

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/3/2 19:54
 *
 */


val ownerOptions: Options = Options()
    .addOption(
        Option.builder("f")
            .longOpt("find")
            .hasArg(true)
            .desc("Find item with owners")
            .build()
    )
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .desc("help")
            .build()
    )