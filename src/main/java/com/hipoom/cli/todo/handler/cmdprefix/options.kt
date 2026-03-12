package com.hipoom.cli.todo.handler.cmdprefix

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

val cmdPrefixOptions: Options = Options()
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .desc("帮助信息")
            .build()
    )
    .addOption(
        Option.builder("s")
            .longOpt("set")
            .hasArg(true)
            .argName("prefix")
            .desc("设置默认命令前缀")
            .build()
    )
    .addOption(
        Option.builder("c")
            .longOpt("clear")
            .desc("清除已设置的命令前缀")
            .build()
    )
