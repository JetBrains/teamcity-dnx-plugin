package jetbrains.buildServer.dotcover

import jetbrains.buildServer.runners.CommandLine
import java.io.File

data class DotCoverProject(
        public val commandLineToGetCoverage: CommandLine,
        public val configFile: File,
        public val snapshotFile: File) {
}