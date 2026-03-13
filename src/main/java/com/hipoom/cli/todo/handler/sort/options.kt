package com.hipoom.cli.todo.handler.sort

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

val sortOptions: Options = Options()
    .addOption(
        Option.builder("i")
            .longOpt("id")
            .hasArg(true)
            .valueSeparator(',')
            .desc("指定需要调整顺序的事项 ID")
            .build()
    )
    .addOption(
        Option.builder("u")
            .longOpt("up")
            .desc("上移一位：与上一个兄弟节点交换位置")
            .build()
    )
    .addOption(
        Option.builder("d")
            .longOpt("down")
            .desc("下移一位：与下一个兄弟节点交换位置")
            .build()
    )
    .addOption(
        Option.builder("t")
            .longOpt("top")
            .desc("置顶：移动到同级事项的最前面")
            .build()
    )
    .addOption(
        Option.builder("b")
            .longOpt("bottom")
            .desc("置底：移动到同级事项的最后面")
            .build()
    )
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .desc("显示帮助信息")
            .build()
    )
