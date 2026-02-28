package com.hipoom.cli.todo.handler.focus

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/3/2 19:54
 *
 */


val focusOptions: Options = Options()
    .addOption(
        Option.builder("c")
            .longOpt("clear")
            .hasArg(false)
            .desc("Clear Focus Config")
            .build()
    )
    .addOption(
        Option.builder("p")
            .longOpt("parent")
            .hasArg(false)
            .desc("Focus on the parent item of the current focus.")
            .build()
    )
    .addOption(
        Option.builder("i")
            .longOpt("id")
            .hasArg(true)
            .desc("Focus on the item.")
            .build()
    )
    .addOption(
        Option.builder("d")
            .longOpt("down")
            .desc("Focus on the next item.")
            .build()
    )
    .addOption(
        Option.builder("u")
            .longOpt("up")
            .desc("Focus on the prev item.")
            .build()
    )
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .desc("help")
            .build()
    )