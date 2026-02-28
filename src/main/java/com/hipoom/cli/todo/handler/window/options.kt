package com.hipoom.cli.todo.handler.window

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/3/2 20:11
 *
 */

val windowOptions: Options = Options()
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .hasArg(false)
            .desc("Help")
            .build()
    )

    .addOption(
        Option.builder("enable")
            .longOpt("enable")
            .hasArg(true)
            .desc("Enable window mode when editing or creating templates.")
            .build()
    )
    .addOption(
        Option.builder("disable")
            .longOpt("disable")
            .hasArg(false)
            .desc("Disable window mode when editing or creating templates.")
            .build()
    )
