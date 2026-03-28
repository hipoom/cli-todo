package com.hipoom.cli.todo.handler.screen

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
        Option.builder("clean")
            .longOpt("clean")
            .desc("清空屏幕")
            .build()
    )
    .addOption(
        Option.builder("move-cursor-to-start")
            .longOpt("move-cursor-to-start")
            .desc("将光标移动到开始位置")
            .build()
    )
