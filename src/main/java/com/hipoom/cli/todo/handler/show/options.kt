package com.hipoom.cli.todo.handler.show

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options


val showOptions: Options = Options()
    .addOption(
        Option.builder("enable-done")
            .longOpt("enable-done")
            .desc("Enable show finished items")
            .build()
    )
    .addOption(
        Option.builder("disable-done")
            .longOpt("disable-done")
            .desc("Disable show finished items")
            .build()
    )

    .addOption(
        Option.builder("enable-deleted")
            .longOpt("enable-deleted")
            .desc("Enable show deleted items")
            .build()
    )
    .addOption(
        Option.builder("disable-deleted")
            .longOpt("disable-deleted")
            .desc("Disable show deleted items")
            .build()
    )

    .addOption(
        Option.builder("enable-show-on-launch")
            .longOpt("enable-show-on-launch")
            .desc("Enable show items on app launch")
            .build()
    )
    .addOption(
        Option.builder("disable-show-on-launch")
            .longOpt("disable-show-on-launch")
            .desc("Disable show items on app launch")
            .build()
    )

    .addOption(
        Option.builder("enable-status")
            .longOpt("enable-status")
            .desc("Enable show status")
            .build()
    )
    .addOption(
        Option.builder("disable-status")
            .longOpt("disable-status")
            .desc("Disable show status")
            .build()
    )

    .addOption(
        Option.builder("enable-owner")
            .longOpt("enable-owner")
            .desc("Enable show owner")
            .build()
    )
    .addOption(
        Option.builder("disable-owner")
            .longOpt("disable-owner")
            .desc("Disable show owner")
            .build()
    )

    .addOption(
        Option.builder("enable-label")
            .longOpt("enable-label")
            .desc("Enable show label")
            .build()
    )
    .addOption(
        Option.builder("disable-label")
            .longOpt("disable-label")
            .desc("Disable show label")
            .build()
    )

    .addOption(
        Option.builder("disable-id")
            .longOpt("disable-id")
            .desc("Disable show id")
            .build()
    )
    .addOption(
        Option.builder("enable-id")
            .longOpt("enable-id")
            .desc("Enable show id")
            .build()
    )

    .addOption(
        Option.builder("h")
            .longOpt("help")
            .desc("Show help")
            .build()
    )