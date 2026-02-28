package com.hipoom.cli.todo.handler.edit

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/3/2 19:54
 *
 */


val editOptions: Options = Options()
    .addOption(
        Option.builder("i")
            .longOpt("id")
            .hasArg(true)
            .desc("Specify the need edit item's id")
            .build()
    )
    .addOption(
        Option.builder("a")
            .longOpt("advance")
            .hasArg(false)
            .desc("Edit using advance mode")
            .build()
    )
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .hasArg(false)
            .desc("Help")
            .build()
    )
    .addOption(
        Option.builder("c")
            .longOpt("content")
            .hasArg(true)
            .desc("New Content")
            .build()
    )
    .addOption(
        Option.builder("o")
            .longOpt("owner")
            .hasArg(true)
            .desc("New Owner")
            .build()
    )
    .addOption(
        Option.builder("d")
            .longOpt("deadline")
            .hasArg(true)
            .desc("New Deadline")
            .build()
    )
    .addOption(
        Option.builder("l")
            .longOpt("label")
            .hasArg(true)
            .desc("New Label")
            .build()
    )
//    .addOption(
//        Option.builder("enable-window")
//            .longOpt("enable-window")
//            .hasArg(false)
//            .desc("Enable window mode")
//            .build()
//    )
//    .addOption(
//        Option.builder("disable-window")
//            .longOpt("disable-window")
//            .hasArg(false)
//            .desc("Disable window mode")
//            .build()
//    )