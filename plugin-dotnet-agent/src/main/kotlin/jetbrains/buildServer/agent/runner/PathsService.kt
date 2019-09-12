package jetbrains.buildServer.agent.runner

import java.io.File

interface PathsService {
    val uniqueName: String

    fun getPath(pathType: PathType): File

    fun getTempFileName(extension: String): File
}