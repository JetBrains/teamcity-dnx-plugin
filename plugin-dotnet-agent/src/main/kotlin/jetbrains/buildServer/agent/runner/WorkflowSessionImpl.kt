/*
 * Copyright 2000-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * See LICENSE in the project root for license information.
 */

package jetbrains.buildServer.agent.runner

import jetbrains.buildServer.agent.*
import jetbrains.buildServer.rx.Observer
import jetbrains.buildServer.rx.subjectOf
import java.io.File

class WorkflowSessionImpl(
        private val _workflowComposer: WorkflowComposer,
        private val _buildStepContext: BuildStepContext,
        private val _loggerService: LoggerService,
        private val _commandLinePresentationService: CommandLinePresentationService,
        private val _virtualContext: VirtualContext)
    : MultiCommandBuildSession, WorkflowContext {

    private var _commandLinesIterator: Iterator<CommandLine>? = null
    private val _eventSource = subjectOf<CommandResultEvent>()
    private var _buildFinishedStatus: BuildFinishedStatus? = null

    override fun subscribe(observer: Observer<CommandResultEvent>) = _eventSource.subscribe(observer)

    override fun getNextCommand(): CommandExecution? {
        val commandLinesIterator: Iterator<CommandLine> = _commandLinesIterator ?: _workflowComposer.compose(this).commandLines.iterator()
        _commandLinesIterator = commandLinesIterator

        if (status != WorkflowStatus.Running) {
            return null
        }

        // yield command here
        if (!commandLinesIterator.hasNext()) {
            if (_buildFinishedStatus == null) {
                _buildFinishedStatus = BuildFinishedStatus.FINISHED_SUCCESS
            }

            return null
        }

        return CommandExecutionAdapter(
                commandLinesIterator.next(),
                _buildStepContext,
                _loggerService,
                _eventSource,
                _commandLinePresentationService,
                _virtualContext)
    }

    override val status: WorkflowStatus
        get() =
            when (_buildFinishedStatus) {
                null -> WorkflowStatus.Running
                BuildFinishedStatus.FINISHED_SUCCESS, BuildFinishedStatus.FINISHED_WITH_PROBLEMS -> WorkflowStatus.Completed
                else -> WorkflowStatus.Failed
            }

    override fun abort(buildFinishedStatus: BuildFinishedStatus) {
        _buildFinishedStatus = buildFinishedStatus
    }

    override fun sessionStarted() = Unit

    override fun sessionFinished(): BuildFinishedStatus? {
        _eventSource.onComplete()
        return _buildFinishedStatus ?: BuildFinishedStatus.FINISHED_SUCCESS
    }

    private class CommandExecutionAdapter(
            private val _commandLine: CommandLine,
            private val _buildStepContext: BuildStepContext,
            private val _loggerService: LoggerService,
            private val _eventSource: Observer<CommandResultEvent>,
            private val _commandLinePresentationService: CommandLinePresentationService,
            private val _virtualContext: VirtualContext) : CommandExecution {

        override fun beforeProcessStarted() {
            val executableFilePresentation = _commandLinePresentationService.buildExecutablePresentation(_commandLine.executableFile)
            val argsPresentation = _commandLinePresentationService.buildArgsPresentation(_commandLine.arguments)
            _loggerService.writeStandardOutput(*(listOf(StdOutText("Starting: ", Color.Header)) + executableFilePresentation + argsPresentation).toTypedArray())
            val workingDirectory = _virtualContext.resolvePath(_commandLine.workingDirectory.path)
            _loggerService.writeStandardOutput(StdOutText("in directory: ", Color.Header), StdOutText(workingDirectory, Color.Header))
        }

        override fun processStarted(programCommandLine: String, workingDirectory: File) = Unit

        override fun processFinished(exitCode: Int) {
            _eventSource.onNext(CommandResultExitCode(exitCode))
        }

        override fun makeProgramCommandLine(): ProgramCommandLine = ProgramCommandLineAdapter(
                _commandLine,
                _buildStepContext.runnerContext.buildParameters.environmentVariables)

        override fun onStandardOutput(text: String) {
            _eventSource.onNext(CommandResultOutput(text))
            _loggerService.writeStandardOutput(text)
        }

        override fun onErrorOutput(error: String) {
            _eventSource.onNext(CommandResultOutput(error))
            _loggerService.writeErrorOutput(error)
        }

        override fun interruptRequested(): TerminationAction = TerminationAction.KILL_PROCESS_TREE

        override fun isCommandLineLoggingEnabled(): Boolean = false
    }

    private class ProgramCommandLineAdapter(
            private val _commandLine: CommandLine,
            private val _environmentVariables: Map<String, String>)
        : ProgramCommandLine {

        override fun getExecutablePath(): String = _commandLine.executableFile.path

        override fun getWorkingDirectory(): String = _commandLine.workingDirectory.path

        override fun getArguments(): MutableList<String> = _commandLine.arguments.map { it.value }.toMutableList()

        override fun getEnvironment(): MutableMap<String, String> {
            val environmentVariables = _environmentVariables.toMutableMap()
            _commandLine.environmentVariables.forEach { environmentVariables[it.name] = it.value }
            return environmentVariables
        }
    }
}