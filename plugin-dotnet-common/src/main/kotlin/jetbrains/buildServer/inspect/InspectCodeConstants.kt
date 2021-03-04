package jetbrains.buildServer.inspect

object InspectCodeConstants {
    const val DATA_PROCESSOR_TYPE = "ReSharperInspectCode"

    const val RUNNER_TYPE = "dotnet-tools-inspectcode"
    const val INSPECTCODE_BINARY_ZIP_X64 = "inspectcode.exe"
    const val INSPECTCODE_BINARY_ZIP_X86 = "inspectcode.x86.exe"
    const val INSPECTCODE_BINARY_NUPKG_X64 = "tools/$INSPECTCODE_BINARY_ZIP_X64"
    const val INSPECTCODE_BINARY_NUPKG_X86 = "tools/$INSPECTCODE_BINARY_ZIP_X86"

    const val RUNNER_DISPLAY_NAME = "Inspections (ReSharper)"
    const val RUNNER_DESCRIPTION = "Runner for gathering JetBrains ReSharper inspection results"

    const val RUNNER_SETTING_CLT_PLATFORM = "jetbrains.resharper-clt.platform"
    const val RUNNER_SETTING_CLT_PLUGINS = "jetbrains.resharper-clt.plugins"
    const val RUNNER_SETTING_CLT_PLATFORM_X86_PARAMETER = "x86"
    const val RUNNER_SETTING_CLT_PLATFORM_X64_PARAMETER = "x64"
    const val RUNNER_SETTING_SOLUTION_PATH = "$RUNNER_TYPE.solution"
    const val RUNNER_SETTING_PROJECT_FILTER = "$RUNNER_TYPE.project.filter"
    const val RUNNER_SETTING_CUSTOM_SETTINGS_PROFILE_PATH = RUNNER_TYPE + "CustomSettingsProfile"
    const val RUNNER_SETTING_DEBUG = "$RUNNER_TYPE.debug"
    const val RUNNER_SETTING_CUSTOM_CMD_ARGS = "$RUNNER_TYPE.customCmdArgs"

    const val CONFIG_PARAMETER_SUPRESS_BUILD_IN_SETTINGS = "teamcity.dotNetTools.inspecitons.supressBuildInSettings"
    const val CONFIG_PARAMETER_DISABLE_SOLUTION_WIDE_ANALYSIS = "teamcity.dotNetTools.inspecitons.disableSolutionWideAnalysis"
}