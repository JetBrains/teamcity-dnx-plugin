package jetbrains.buildServer.dotnet

import jetbrains.buildServer.RunBuildException
import jetbrains.buildServer.agent.*
import jetbrains.buildServer.agent.runner.ParameterType
import jetbrains.buildServer.agent.runner.ParametersService
import jetbrains.buildServer.agent.runner.PathType
import jetbrains.buildServer.agent.runner.PathsService
import jetbrains.buildServer.util.OSType
import java.io.File

class TargetServiceImpl(
        private val _pathsService: PathsService,
        private val _parametersService: ParametersService,
        private val _argumentsService: ArgumentsService,
        private val _pathMatcher: PathMatcher,
        private val _fileSystemService: FileSystemService,
        private val _virtualContext: VirtualContext)
    : TargetService {
    override val targets: Sequence<CommandTarget>
        get() = sequence {
            _parametersService.tryGetParameter(ParameterType.Runner, DotnetConstants.PARAM_PATHS)?.trim()?.let {
                val workingDirectory = _pathsService.getPath(PathType.WorkingDirectory)
                val includeRulesStr = it.trim()
                if (includeRulesStr.isEmpty()) {
                    return@sequence
                }

                // We need to resolve paths in the specified sequence where
                // include rules may be mix of regular and wildcard paths
                _argumentsService.split(includeRulesStr).forEach {
                    if (wildCardPattern.matches(it)) {
                        val targets = _pathMatcher.match(workingDirectory, listOf(it))
                        if (targets.isEmpty()) {
                            throw RunBuildException("Target files not found for pattern \"$it\"")
                        }

                        targets.forEach { yield(createCommandTarget(workingDirectory, it)) }
                    } else {
                        yield(createCommandTarget(workingDirectory, File(it)))
                    }
                }
            }
        }

    private fun createCommandTarget(workingDirectory: File, target: File): CommandTarget {
        if (_virtualContext.targetOSType == OSType.WINDOWS) {
            return CommandTarget(Path(target.path))
        }

        var targetFile: File = target
        if (!_fileSystemService.isAbsolute(target)) {
            targetFile = File(workingDirectory, target.path)
        }

        return CommandTarget(Path(_virtualContext.resolvePath(targetFile.path)))
    }

    companion object {
        val wildCardPattern = Regex(".*[?*].*")
    }
}