package com.hipoom.cli.todo.handler.developer

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/3/2 19:54
 *
 */


val developerOptions: Options = Options()
    .addOption(
        Option.builder("ccw")
            .longOpt("check-char-width")
            .hasArg(true)
            .desc("Check a charactor's ui width.")
            .build()
    )
    .addOption(
        Option.builder("slmi")
            .longOpt("show-last-modify-item")
            .hasArg(true)
            .desc("Show the Last Modified Item's id After each Cmd is Executed.")
            .build()
    )
    .addOption(
        Option.builder("sec")
            .longOpt("show-expand-cmd")
            .hasArg(false)
            .desc("Show Expand Cmds")
            .build()
    )
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .desc("help")
            .build()
    )