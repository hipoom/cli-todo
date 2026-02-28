package com.hipoom.cli.todo.handler.find

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

/**
 * Find命令的选项定义
 *
 * @author ZhengHaiPeng
 */

val findOptions: Options = Options()
    .addOption(
        Option.builder("c")
            .longOpt("content")
            .hasArg(true)
            .desc("Find items with content containing the keyword")
            .build()
    )
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .desc("help")
            .build()
    )