package com.hipoom.cli.todo.handler.mark

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/3/2 19:54
 *
 */


val markOptions: Options = Options()
    .addOption(
        Option.builder("d")
            .longOpt("done")
            .desc("Mark done")
            .build()
    )
    .addOption(
        Option.builder("n")
            .longOpt("new")
            .desc("Mark new")
            .build()
    )
    .addOption(
        Option.builder("doing")
            .longOpt("doing")
            .desc("Mark doing")
            .build()
    )
    .addOption(
        Option.builder("i")
            .longOpt("id")
            .hasArg()
            .desc("Specify id")
            .build()
    )
    .addOption(
        Option.builder("del")
            .longOpt("deleted")
            .desc("Mark deleted")
            .build()
    )
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .desc("help")
            .build()
    )