package com.hipoom.cli.todo.handler.template

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/3/3 12:52
 *
 */


val templateOptions: Options = Options()
    .addOption(
        Option.builder("c")
            .longOpt("create")
            .hasArg(false)
            .desc("Add a new template")
            .build()
    )
    .addOption(
        Option.builder("e")
            .longOpt("edit")
            .optionalArg(true)
            .desc("Edit the specified template")
            .build()
    )
    .addOption(
        Option.builder("l")
            .longOpt("list")
            .hasArg(false)
            .desc("Show all templates")
            .build()
    )
    .addOption(
        Option.builder("d")
            .longOpt("delete")
            .hasArg(false)
            .desc("Delete template")
            .build()
    )
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .hasArg(false)
            .desc("Show template help")
            .build()
    )