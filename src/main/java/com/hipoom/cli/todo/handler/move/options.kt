package com.hipoom.cli.todo.handler.move

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/3/2 19:54
 *
 */


val moveOptions: Options = Options()
    .addOption(
        Option.builder("i")
            .longOpt("id")
            .hasArg(true)
            .valueSeparator(',')
            .desc("Specify need move ids.")
            .build()
    )
    .addOption(
        Option.builder("p")
            .longOpt("parent")
            .hasArg(true)
            .desc("The target parent id.")
            .build()
    )
    .addOption(
        Option.builder("u")
            .longOpt("up")
            .desc("Move up one level, be on the same level as the parent node.")
            .build()
    )
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .desc("help")
            .build()
    )
    .addOption(
        Option.builder("ui")
            .longOpt("unfinished-items")
            .desc("Move all unfinished items from <--id> to <--parent>")
            .build()
    )