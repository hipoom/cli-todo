package com.hipoom.cli.todo.handler.mapping

import com.google.gson.annotations.SerializedName
import com.hipoom.cli.core.ui.AsciiTable
import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.scaffold.utils.gson
import com.hipoom.cli.scaffold.utils.readInt
import com.hipoom.cli.scaffold.utils.readString
import com.hipoom.cli.workspace.WorkspaceContext
import com.hipoom.files.child
import com.hipoom.files.createNewFileIfNotExist
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/3/3 0:26
 *
 */
class MappingHandler: ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = Options()
        .addOption(
            Option.builder("a")
                .longOpt("add")
                .desc("Add quick cmd mapping.")
                .build()
        )
        .addOption(
            Option.builder("d")
                .longOpt("delete")
                .desc("Delete quick cmd mapping.")
                .build()
        )
        .addOption(
            Option.builder("s")
                .longOpt("show")
                .desc("Show quick cmd mapping.")
                .build()
        )
        .addOption(
            Option.builder("h")
                .longOpt("help")
                .hasArg(false)
                .desc("Help")
                .build()
        )

    override val supportPrefixes: List<String> = listOf("mapping")

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        when {
            commandLine.hasOption("h") -> printHelp()
            commandLine.hasOption("a") -> addMapping(app)
            commandLine.hasOption("d") -> deleteMapping(app)
            commandLine.hasOption("s") -> showMapping(app)
        }

        return true
    }

    override fun description(): String = "Cmd Mapping"



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private fun addMapping(app: CliApp) {
        val mappings = app.load()

        val quick = readString("请输入快捷指令") ?: return
        val origin = readString("请输入对应的原始指令") ?: return

        val id = mappings.maxId + 1
        mappings.maxId += 1
        mappings.mappings.add(
            CmdMapping(
                id = id,
                origin = origin,
                quick = quick.trim()
            )
        )

        app.store(mappings)
    }

    private fun deleteMapping(app: CliApp) {
        showMapping(app)
        val id = readInt("请输入想删除的快捷指令序号: ") ?: return
        val mappings = app.load()
        mappings.mappings.removeIf { it.id == id }
        app.store(mappings)
    }

    private fun showMapping(app: CliApp) {
        val mappings = app.load()

        val builder = AsciiTable.Builder()
        builder.setHeads(" id ", " quick cmd ", " origin cmd ")

        mappings.mappings.forEach {
            builder.addRow(" ${it.id} ", " ${it.quick} ", " ${it.origin} ")
        }

        builder.build().forEach { println(it) }
        println()
    }

}

lateinit var cmdMappings: CmdMappings

fun CliApp.initQuickCmd() {
    load()
}

private fun CliApp.load(): CmdMappings {
    val file = getAppRootDirectory().child("cmd-mapping.json")
    file.createNewFileIfNotExist {
        file.writeText(gson.toJson(CmdMappings(maxId = 0, mappings = ArrayList())))
    }
    cmdMappings = gson.fromJson(file.readText(), CmdMappings::class.java)
    return cmdMappings
}

private fun CliApp.store(mappings: CmdMappings) {
    val file = getAppRootDirectory().child("cmd-mapping.json")
    file.createNewFileIfNotExist()
    file.writeText(gson.toJson(mappings))
    cmdMappings = mappings
}

class CmdMapping(
    @SerializedName("id")
    var id: Int
    ,
    @SerializedName("origin")
    var origin: String
    ,
    @SerializedName("quick")
    var quick: String
)

class CmdMappings(
    @SerializedName("maxId")
    var maxId: Int
    ,
    @SerializedName("mappings")
    var mappings: MutableList<CmdMapping>
)