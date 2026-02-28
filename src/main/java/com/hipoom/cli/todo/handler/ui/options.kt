package com.hipoom.cli.todo.handler.ui

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/3/2 19:54
 *
 */


val uiOptions: Options = Options()
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .desc("help")
            .build()
    )
    .addOption(
        Option.builder("color")
            .longOpt("color")
            .desc("Set Color Mode")
            .build()
    )