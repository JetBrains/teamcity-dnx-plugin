package jetbrains.buildServer.dotnet

import jetbrains.buildServer.agent.FileSystemService
import org.apache.log4j.Logger
import java.io.File

class DotnetFrameworkValidatorImpl(
        private val _fileSystemService: FileSystemService)
    : DotnetFrameworkValidator {
    override fun isValid(framework: DotnetFramework): Boolean {
        LOG.info("Validating $framework.")
        if (!_fileSystemService.isDirectory(framework.path)) {
            LOG.info("\"${framework.path}\" is not a directory.")
            return false
        }

        if (framework.version.major == 3 && framework.version.minor == 0) {
            return true
        }

        var files = _fileSystemService
                .list(framework.path)
                .filter { _fileSystemService.isFile(it) }
                .map { it.name.toLowerCase() }
                .toHashSet()

        if (!files.contains("csc.exe")) {
            LOG.info("\"${framework.path}\" does not containt \"csc.exe\".")
            return false
        }

        if (!files.contains("vbc.exe")) {
            LOG.info("\"${framework.path}\" does not containt \"vbc.exe\".")
            return false
        }

        if (framework.version.major == 1 && framework.version.minor == 1) {
            return true
        }

        if (!files.contains("msbuild.exe")) {
            LOG.info("\"${framework.path}\" does not containt \"msbuild.exe\".")
            return false
        }

        return true
    }

    companion object {
        private val LOG = Logger.getLogger(DotnetFrameworkValidatorImpl::class.java)
    }
}