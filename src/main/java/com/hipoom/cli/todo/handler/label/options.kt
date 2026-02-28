package com.hipoom.cli.todo.handler.label

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/3/2 19:54
 *
 */


val labelOptions: Options = Options()
    .addOption(
        Option.builder("f")
            .longOpt("find")
            .hasArg(true)
            .desc("Find item with label")
            .build()
    )
    .addOption(
        Option.builder("a")
            .longOpt("add")
            .hasArg(true)
            .desc("Add label to item(s)")
            .build()
    )
    .addOption(
        Option.builder("i")
            .longOpt("id")
            .hasArg(true)
            .desc("Specify item(s)")
            .build()
    )
    .addOption(
        Option.builder("hide")
            .longOpt("hide")
            .hasArg(true)
            .desc("Hide item(s) with specified label(s)")
            .build()
    )
    .addOption(
        Option.builder("unhide")
            .longOpt("unhide")
            .hasArg(true)
            .desc("Hide item(s) with specified label(s)")
            .build()
    )
    .addOption(
        Option.builder()
            .longOpt("auto-add")
            .hasArg(true)
            .argName("LabelName")
            .desc("Add auto label rule")
            .build()
    )
    .addOption(
        Option.builder()
            .longOpt("if-contains")
            .hasArg(true)
            .argName("KeyWord")
            .desc("Content to check for auto label")
            .build()
    )
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .desc("help")
            .build()
    )