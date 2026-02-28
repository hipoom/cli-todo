package com.hipoom.cli.todo.handler.upgrade

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

val upgradeOptions: Options = Options()
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .hasArg(false)
            .desc("Help")
            .build()
    )
    .addOption(
        Option.builder("u")
            .longOpt("url")
            .hasArg(true)
            .desc("Specify the URL to check for updates")
            .build()
    )
    .addOption(
        Option.builder("c")
            .longOpt("check")
            .hasArg(false)
            .desc("Check for updates without downloading")
            .build()
    )
    .addOption(
        Option.builder("d")
            .longOpt("download")
            .hasArg(false)
            .desc("Download the latest version if available")
            .build()
    )
    .addOption(
        Option.builder("v")
            .longOpt("version")
            .hasArg(false)
            .desc("Show current version")
            .build()
    )
