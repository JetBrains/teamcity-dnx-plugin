package jetbrains.buildServer.dotnet.test.dotnet

import jetbrains.buildServer.RunBuildException
import jetbrains.buildServer.agent.Environment
import jetbrains.buildServer.dotnet.*
import jetbrains.buildServer.agent.runner.PathsService
import jetbrains.buildServer.dotnet.test.agent.runner.ParametersServiceStub
import jetbrains.buildServer.util.OSType
import org.jmock.Expectations
import org.jmock.Mockery
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.io.File

class MSBuildToolResolverTest {
    private var _ctx: Mockery? = null
    private var _pathsService: PathsService? = null;
    private var _environment: Environment? = null

    @BeforeMethod
    fun setUp() {
        _ctx = Mockery()
        _environment = _ctx!!.mock(Environment::class.java)
    }

    @DataProvider
    fun testData(): Array<Array<out Any?>> {
        return arrayOf(
                arrayOf(OSType.WINDOWS, mapOf(DotnetConstants.PARAM_MSBUILD_VERSION to Tool.MSBuild15WindowsX64.id, "MSBuildTools15.0_x64_Path" to "msbuild15X64"), File("msbuild15X64", MSBuildToolResolver.MSBuildWindowsTooName).absoluteFile, null),
                arrayOf(OSType.WINDOWS, mapOf(DotnetConstants.PARAM_MSBUILD_VERSION to Tool.MSBuild12WindowsX86.id, "MSBuildTools12.0_x86_Path" to "msbuild12X86"), File("msbuild12X86", MSBuildToolResolver.MSBuildWindowsTooName).absoluteFile, null),
                arrayOf(OSType.WINDOWS, mapOf(DotnetConstants.PARAM_MSBUILD_VERSION to Tool.MSBuild14WindowsX86.id, "MSBuildTools14.0_x86_Path" to "msbuild14X86"), File("msbuild14X86", MSBuildToolResolver.MSBuildWindowsTooName).absoluteFile, null),
                arrayOf(OSType.WINDOWS, mapOf(DotnetConstants.PARAM_MSBUILD_VERSION to Tool.MSBuild15Windows.id, "MSBuildTools15.0_x64_Path" to "msbuild15X64"), File("msbuild15X64", MSBuildToolResolver.MSBuildWindowsTooName).absoluteFile, null),
                arrayOf(OSType.WINDOWS, mapOf(DotnetConstants.PARAM_MSBUILD_VERSION to Tool.MSBuild15Windows.id, "MSBuildTools15.0_x86_Path" to "msbuild15X86", "MSBuildTools15.0_x64_Path" to "msbuild15X64"), File("msbuild15X64", MSBuildToolResolver.MSBuildWindowsTooName).absoluteFile, null),
                arrayOf(OSType.WINDOWS, mapOf(DotnetConstants.PARAM_MSBUILD_VERSION to Tool.MSBuild15Windows.id, "MSBuildTools15.0_x86_Path" to "msbuild15X86"), File("msbuild15X86", MSBuildToolResolver.MSBuildWindowsTooName).absoluteFile, null),
                arrayOf(OSType.WINDOWS, mapOf(DotnetConstants.PARAM_MSBUILD_VERSION to Tool.MSBuild15Mono.id, MonoConstants.CONFIG_PATH to "mono"), File(File("mono").absoluteFile.parent, MSBuildToolResolver.MSBuildMonoWindowsToolName).absoluteFile, null),
                arrayOf(OSType.UNIX, mapOf(DotnetConstants.PARAM_MSBUILD_VERSION to Tool.MSBuild15Mono.id, MonoConstants.CONFIG_PATH to "mono"), File(File("mono").absoluteFile.parent, MSBuildToolResolver.MSBuildMonoToolName).absoluteFile, null),
                arrayOf(OSType.MAC, mapOf(DotnetConstants.PARAM_MSBUILD_VERSION to Tool.MSBuild15Mono.id, MonoConstants.CONFIG_PATH to "mono"), File(File("mono").absoluteFile.parent, MSBuildToolResolver.MSBuildMonoToolName).absoluteFile, null),
                arrayOf(OSType.WINDOWS, mapOf(DotnetConstants.PARAM_MSBUILD_VERSION to Tool.MSBuild15DotnetCore.id), File("dotnet"), null),
                arrayOf(OSType.UNIX, mapOf(DotnetConstants.PARAM_MSBUILD_VERSION to Tool.MSBuild15DotnetCore.id), File("dotnet"), null),
                arrayOf(OSType.MAC, mapOf(DotnetConstants.PARAM_MSBUILD_VERSION to Tool.MSBuild15DotnetCore.id), File("dotnet"), null),
                arrayOf(OSType.WINDOWS, mapOf(DotnetConstants.PARAM_MSBUILD_VERSION to Tool.MSBuild15Windows.id), File(""), Regex("jetbrains.buildServer.agent.ToolCannotBeFoundException: MSBuildTools15.0_x64_Path")),
                arrayOf(OSType.WINDOWS, mapOf(DotnetConstants.PARAM_MSBUILD_VERSION to Tool.MSBuild15WindowsX64.id), File(""), Regex("jetbrains.buildServer.agent.ToolCannotBeFoundException: MSBuildTools15.0_x64_Path")))
    }

    @Test(dataProvider = "testData")
    fun shouldProvideExecutableFile(
            os: OSType,
            parameters: Map<String, String>,
            expectedExecutableFile: File,
            exceptionPattern: Regex?) {
        // Given
        val instance = createInstance(parameters, File("dotnet"))

        // When
        _ctx!!.checking(object : Expectations() {
            init {
                oneOf<Environment>(_environment!!).OS
                will(returnValue(os))
            }
        })


        var actualExecutableFile: File? = null;
        try {
            actualExecutableFile = instance.executableFile
            exceptionPattern?.let {
                Assert.fail("Exception should be thrown")
            }
        }
        catch (ex: RunBuildException)
        {
            Assert.assertEquals(exceptionPattern!!.containsMatchIn(ex.message!!), true);
        }


        // Then
        if (exceptionPattern == null) {
            Assert.assertEquals(actualExecutableFile, expectedExecutableFile)
        }
    }

    private fun createInstance(parameters: Map<String, String>, executableFile: File): ToolResolver {
        return MSBuildToolResolver(_environment!!, ParametersServiceStub(parameters), DotnetToolResolverStub(executableFile, true))
    }
}