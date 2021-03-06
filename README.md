## .NET Plugin for [<img src="https://cdn.worldvectorlogo.com/logos/teamcity.svg" height="20" align="center" alt="TeamCity" />](https://www.jetbrains.com/teamcity/)

[![official JetBrains project](http://jb.gg/badges/official.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

The TeamCity .NET plugin provides support for .NET tools in TeamCity.

It simplifies building Windows and cross-platform applications that use .NET frameworks and libraries.

# Features

The plugin provides the following features:
* .NET build runner for [.NET CLI](https://docs.microsoft.com/en-us/dotnet/core/tools/dotnet), [MSBuild](https://docs.microsoft.com/en-us/visualstudio/msbuild/msbuild), [Visual Studio Test](https://docs.microsoft.com/en-us/visualstudio/test/vstest-console-options) and [Visual Studio Devenv](https://docs.microsoft.com/en-us/visualstudio/ide/reference/devenv-command-line-switches)
* [.NET SDK](https://docs.microsoft.com/en-us/dotnet/core/sdk) and .NET tools detection on TeamCity build agents
* Auto-discovery of build steps
* On-the-fly test reporting
* Supports code coverage tools
* Cleanup of NuGet caches to meet the agent [free space requirements](https://www.jetbrains.com/help/teamcity/?Free+disk+space)
* Supports [TeamCity Docker Wrapper](https://www.jetbrains.com/help/teamcity/docker-wrapper.html)

# Download

You can [download the plugin](https://plugins.jetbrains.com/plugin/9190?pr=teamcity) and install it as [an additional TeamCity plugin](https://www.jetbrains.com/help/teamcity/?Installing+Additional+Plugins).

# Additional Resources

* TeamCity integration with .NET
  * [New approach and demo](https://blog.jetbrains.com/teamcity/2020/12/teamcity-integration-with-net-part-1-new-approach-and-demo/)
  * [Testing and building projects](https://blog.jetbrains.com/teamcity/2020/12/teamcity-integration-with-net-part-2-testing-and-building-projects/)
  * [Deploying projects](https://blog.jetbrains.com/teamcity/2020/12/teamcity-integration-with-net-part-3-deploying-projects/)

# Compatibility

The plugin is compatible with

- [TeamCity 2017.1+](https://www.jetbrains.com/teamcity/download/)
- [.NET CLI 1.0+](https://dotnet.microsoft.com/download/dotnet-core/)
- Visual Studio 2013+
- MSBuild 12+
- VSTest console 12+

**Note**: This plugin is [bundled in TeamCity 2017.2+](https://www.jetbrains.com/help/teamcity/?Upgrade+Notes)

# Configuration

## .NET CLI toolkit

To use the `dotnet` build runner, install [.NET CLI](https://dotnet.microsoft.com/download/dotnet-core/) and add the .NET CLI tools path to the `PATH` environment variable.

You can also configure the `DOTNET_HOME` environment variable for your TeamCity build agent user, for instance:

```
DOTNET_HOME=C:\Program Files\dotnet\
```

## Reported agent configuration parameters

During initialization this plugin reports the following agent configuration parameters:

| Name                  | Description                                 |
|-----------------------|-------------------------------------------- |
| DotNetCLI             | The version of found .NET CLI (dotnet) app  |
| DotNetCLI_Path        | The path to the found .NET CLI (dotnet) app |
| DotNetCoreSDKx.x_Path | The path to the found .NET Core SDK         |

# Known issues

Tests might not be reported correctly for .NET Core xunit test projects when logging verbosity levels is `minimal` or `quiet` because of [issue](https://github.com/xunit/xunit/issues/1706).

# Build

This project uses gradle as a build system. You can easily open it in [IntelliJ IDEA](https://www.jetbrains.com/idea/help/importing-project-from-gradle-model.html) or [Eclipse](http://gradle.org/eclipse/).

# Contributions

We appreciate all kinds of feedback, so please feel free to send a PR or file an issue in the [TeamCity tracker](https://youtrack.jetbrains.com/newIssue?project=TW&clearDraft=true&summary=.NET%20CLI:&c=Subsystem%20Agent%20-%20.NET&c=tag%20.NET%20Core).
