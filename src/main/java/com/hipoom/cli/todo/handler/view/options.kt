package com.hipoom.cli.todo.handler.view

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/3/2 19:54
 *
 */


val viewOptions: Options = Options()
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .desc("show help message")
            .build()
    )
    .addOption(
        Option.builder("c")
            .longOpt("create")
            .desc("create a virtual view")
            .hasArg()
            .argName("VIEW_NAME")
            .build()
    )
    .addOption(
        Option.builder("a")
            .longOpt("add")
            .desc("add item(s) to a virtual view")
            .hasArg()
            .argName("ITEM_IDs")
            .build()
    )
    .addOption(
        Option.builder("v")
            .longOpt("view")
            .desc("specify the view name")
            .hasArg()
            .argName("VIEW_NAME")
            .build()
    )
    .addOption(
        Option.builder("e")
            .longOpt("exit")
            .desc("exit virtual view mode")
            .build()
    )
    .addOption(
        Option.builder("l")
            .longOpt("list")
            .desc("list all virtual views")
            .build()
    )
    .addOption(
        Option.builder()
            .longOpt("delete")
            .desc("delete a virtual view")
            .hasArg()
            .argName("VIEW_NAME")
            .build()
    )