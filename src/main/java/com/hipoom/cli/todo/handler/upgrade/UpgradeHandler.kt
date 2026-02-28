package com.hipoom.cli.todo.handler.upgrade

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.VERSION_CODE
import com.hipoom.cli.todo.VERSION_NAME
import com.hipoom.cli.todo.gson
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.lang.management.ManagementFactory
import java.net.HttpURLConnection
import java.net.URL

class UpgradeHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options = upgradeOptions

    override val supportPrefixes: List<String> = listOf("upgrade")

    companion object {
        private const val DEFAULT_VERSION_URL = "https://raw.githubusercontent.com/hipoom/cli-todo/refs/heads/main/.documents/latest_version.json"
        private const val CURRENT_VERSION = VERSION_NAME
        private val CURRENT_VERSION_CODE = VERSION_CODE
        private const val CONNECT_TIMEOUT = 10000
        private const val READ_TIMEOUT = 15000
    }


    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description() = "Check and download application updates"

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        if (commandLine.hasOption("h")) {
            printHelp()
            return true
        }

        if (commandLine.hasOption("v")) {
            showCurrentVersion()
            return true
        }

        val url = commandLine.getOptionValue("u") ?: DEFAULT_VERSION_URL

        return when {
            commandLine.hasOption("c") -> checkForUpdates(url)
            commandLine.hasOption("d") -> downloadUpdate(url)
            commandLine.hasOption("update") -> autoUpdate(url)
            else -> checkForUpdates(url)
        }
    }


    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private fun showCurrentVersion() {
        printLine("Current version: $CURRENT_VERSION (code: $CURRENT_VERSION_CODE)")
    }

    private fun checkForUpdates(url: String): Boolean {
        printLine("Checking for updates from: $url")
        printLine()

        return try {
            val versionInfo = fetchVersionInfo(url)
            if (versionInfo == null) {
                printLine("Failed to fetch version information.")
                return false
            }

            printLine("Latest version: ${versionInfo.version} (code: ${versionInfo.versionCode})")
            printLine("Release date: ${versionInfo.releaseDate}")
            printLine()
            printLine("Release notes:")
            printLine(versionInfo.releaseNotes)
            printLine()

            when {
                versionInfo.versionCode > CURRENT_VERSION_CODE -> {
                    printLine("A new version is available!")
                    if (versionInfo.forceUpdate) {
                        printLine("This is a mandatory update.")
                    }
                    printLine("Download URL: ${versionInfo.downloadUrl}")
                    true
                }
                versionInfo.versionCode < CURRENT_VERSION_CODE -> {
                    printLine("Current version is newer than the latest release.")
                    true
                }
                else -> {
                    printLine("You are already using the latest version.")
                    true
                }
            }
        } catch (e: Exception) {
            printLine("Error checking for updates: ${e.message}")
            false
        }
    }

    private fun downloadUpdate(url: String): Boolean {
        printLine("Checking for updates from: $url")
        printLine()

        return try {
            val versionInfo = fetchVersionInfo(url)
            if (versionInfo == null) {
                printLine("Failed to fetch version information.")
                return false
            }

            if (versionInfo.versionCode <= CURRENT_VERSION_CODE) {
                printLine("You are already using the latest version.")
                return true
            }

            printLine("Downloading version ${versionInfo.version}...")
            printLine("Download URL: ${versionInfo.downloadUrl}")

            val downloadFile = downloadFile(versionInfo.downloadUrl)
            if (downloadFile != null) {
                printLine()
                printLine("Download completed: ${downloadFile.absolutePath}")
                printLine("Please manually replace the old version with the new one.")
                true
            } else {
                printLine("Download failed.")
                false
            }
        } catch (e: Exception) {
            printLine("Error downloading update: ${e.message}")
            false
        }
    }

    private fun fetchVersionInfo(url: String): VersionInfo? {
        return try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = CONNECT_TIMEOUT
            connection.readTimeout = READ_TIMEOUT

            val responseCode = connection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                printLine("HTTP error code: $responseCode")
                return null
            }

            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()

            gson.fromJson(response.toString(), VersionInfo::class.java)
        } catch (e: Exception) {
            printLine("Error fetching version info: ${e.message}")
            null
        }
    }

    private fun downloadFile(url: String): File? {
        return try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = CONNECT_TIMEOUT
            connection.readTimeout = READ_TIMEOUT

            val responseCode = connection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                printLine("HTTP error code: $responseCode")
                return null
            }

            val fileName = url.substring(url.lastIndexOf('/') + 1)
            val tempFile = File.createTempFile("todo-update-", "-$fileName")

            connection.inputStream.use { input ->
                tempFile.outputStream().use { output ->
                    val buffer = ByteArray(8192)
                    var bytesRead: Int
                    var totalBytesRead: Long = 0
                    val fileSize = connection.contentLengthLong

                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                        totalBytesRead += bytesRead

                        if (fileSize > 0) {
                            val progress = (totalBytesRead * 100 / fileSize).toInt()
                            print("\rDownloading: $progress%")
                        }
                    }
                    printLine()
                }
            }

            tempFile
        } catch (e: Exception) {
            printLine("Error downloading file: ${e.message}")
            null
        }
    }

    private fun autoUpdate(url: String): Boolean {
        printLine("Checking for updates from: $url")
        printLine()

        return try {
            val versionInfo = fetchVersionInfo(url)
            if (versionInfo == null) {
                printLine("Failed to fetch version information.")
                return false
            }

            if (versionInfo.versionCode <= CURRENT_VERSION_CODE) {
                printLine("You are already using the latest version.")
                return true
            }

            printLine("New version ${versionInfo.version} is available!")
            printLine("Downloading...")

            val downloadedFile = downloadFile(versionInfo.downloadUrl)
            if (downloadedFile == null) {
                printLine("Download failed.")
                return false
            }

            printLine("Download completed: ${downloadedFile.absolutePath}")

            val currentJar = getCurrentJarPath()
            if (currentJar == null) {
                printLine("Cannot determine current jar path. Please update manually.")
                downloadedFile.delete()
                return false
            }

            printLine("Current jar: ${currentJar.absolutePath}")
            printLine("Replacing and restarting...")

            replaceJarAndRestart(downloadedFile, currentJar)
        } catch (e: Exception) {
            printLine("Error during auto update: ${e.message}")
            false
        }
    }

    private fun getCurrentJarPath(): File? {
        return try {
            val path = javaClass.protectionDomain.codeSource.location.toURI().path
            val file = File(path)
            if (file.exists() && file.name.endsWith(".jar")) {
                file
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun replaceJarAndRestart(newJar: File, currentJar: File): Boolean {
        return try {
            val isWindows = System.getProperty("os.name").lowercase().contains("windows")
            val scriptFile = if (isWindows) {
                File.createTempFile("todo-update-", ".bat")
            } else {
                File.createTempFile("todo-update-", ".sh")
            }

            val javaHome = System.getProperty("java.home")
            val javaExe = if (isWindows) {
                "$javaHome\\bin\\java.exe"
            } else {
                "$javaHome/bin/java"
            }

            val pid = ManagementFactory.getRuntimeMXBean().name.split("@")[0]

            if (isWindows) {
                scriptFile.writeText("""
                    @echo off
                    :wait
                    tasklist /FI "PID eq $pid" 2>NUL | find "$pid" >NUL
                    if %ERRORLEVEL% equ 0 (
                        timeout /t 1 /nobreak >NUL
                        goto wait
                    )
                    copy /Y "${newJar.absolutePath}" "${currentJar.absolutePath}"
                    del "${newJar.absolutePath}"
                    start "" "$javaExe" -jar "${currentJar.absolutePath}"
                    del "%~f0"
                """.trimIndent())
            } else {
                scriptFile.writeText("""
                    #!/bin/bash
                    while kill -0 $pid 2>/dev/null; do
                        sleep 1
                    done
                    cp -f "${newJar.absolutePath}" "${currentJar.absolutePath}"
                    rm -f "${newJar.absolutePath}"
                    "$javaExe" -jar "${currentJar.absolutePath}" &
                    rm -f "$0"
                """.trimIndent())
                scriptFile.setExecutable(true)
            }

            printLine("Update script created: ${scriptFile.absolutePath}")
            printLine("The application will restart after update.")
            printLine()

            val processBuilder = if (isWindows) {
                ProcessBuilder("cmd", "/c", scriptFile.absolutePath)
            } else {
                ProcessBuilder("/bin/bash", scriptFile.absolutePath)
            }
            processBuilder.inheritIO()
            processBuilder.start()

            System.exit(0)
            true
        } catch (e: Exception) {
            printLine("Error creating update script: ${e.message}")
            printLine("Please manually replace: ${newJar.absolutePath} -> ${currentJar.absolutePath}")
            false
        }
    }
}
