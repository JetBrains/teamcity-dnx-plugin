package jetbrains.buildServer.dotnet

import jetbrains.buildServer.agent.AgentPropertiesProvider
import jetbrains.buildServer.agent.AgentProperty
import jetbrains.buildServer.agent.ToolInstanceType
import jetbrains.buildServer.agent.Version
import jetbrains.buildServer.agent.runner.ToolInstanceProvider

class VisualStudioTestAgentPropertiesProvider(
        private val _visualStudioTestInstanceProvider: ToolInstanceProvider)
    : AgentPropertiesProvider {
    override val desription = "Visual Studio Test Console"

    override val properties: Sequence<AgentProperty> get() =
        _visualStudioTestInstanceProvider
                .getInstances()
                .filter { it.toolType == ToolInstanceType.VisualStudioTest }
                .map {
                    console ->
                    AgentProperty(ToolInstanceType.VisualStudioTest, "teamcity.dotnet.vstest.${console.baseVersion.major}${Version.Separator}${console.baseVersion.minor}", console.installationPath.path)
                }
}