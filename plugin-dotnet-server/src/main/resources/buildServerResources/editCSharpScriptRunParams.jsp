<%@ page import="jetbrains.buildServer.runner.SimpleRunnerConstants" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>
<jsp:useBean id="propertyNames" class="jetbrains.buildServer.script.CSharpScriptConstantsBean"/>

<props:workingDirectory />

<jsp:include page="/tools/editToolUsage.html?toolType=${propertyNames.cltToolTypeName}&versionParameterName=${propertyNames.cltPath}&class=longField"/>

<%--<tr class="advancedSetting">
  <th class="noBorder"><label for="${propertyNames.frameworkVersion}">Framework:</label></th>
  <td>
    <props:selectProperty name="${propertyNames.frameworkVersion}" enableFilter="true" className="mediumField">
      <c:forEach var="item" items="${propertyNames.frameworkVersions}">
        <props:option value="${item.tfm}"><c:out value="${item.description}"/></props:option>
      </c:forEach>
    </props:selectProperty>
    <span class="error" id="error_${propertyNames.frameworkVersion}"></span>
    <span class="smallNote" id="defaultNote_${propertyNames.frameworkVersion}">Select a version of .NET Framework.</span>
  </td>
</tr>--%>

<props:selectSectionProperty name="${propertyNames.scriptType}" title="Script type:">

  <props:selectSectionPropertyContent value="${propertyNames.typeCustom}" caption="${propertyNames.typeCustomDescription}">
    <tr id="script.content.container">
      <th>
        <label for="${propertyNames.scriptContent}">C# script:<l:star/></label>
      </th>
      <td class="codeHighlightTD">
        <props:multilineProperty name="${propertyNames.scriptContent}" className="longField" cols="58" rows="10" expanded="true" linkTitle="Enter C# script content"
                                 note="A C# script code"
                                 highlight="shell" />
      </td>
    </tr>
  </props:selectSectionPropertyContent>

  <props:selectSectionPropertyContent value="${propertyNames.typeFile}" caption="${propertyNames.typeFileDescription}">
    <tr id="script.content.container">
      <th>
        <label for="${propertyNames.scriptFile}">C# script file:<l:star/></label>
      </th>
      <td class="codeHighlightTD">
        <props:textProperty name="${propertyNames.scriptFile}" className="longField">
          <jsp:attribute name="afterTextField">
            <bs:vcsTree fieldId="${propertyNames.scriptFile}"/>
          </jsp:attribute>
        </props:textProperty>
        <span id="error_${propertyNames.scriptFile}" class="error"></span>
        <%--<span class="smallNote">To support annotation-based references to Maven dependencies, the provided file must have the <i>*.main.kts</i> extension. <bs:help file="csharp+Script#script-file"/></span>--%>
      </td>
    </tr>
  </props:selectSectionPropertyContent>
</props:selectSectionProperty>

<tr class="advancedSetting">
  <th>
    <label for="${propertyNames.args}">Script parameters:</label>
  </th>
  <td>
    <props:textProperty name="${propertyNames.args}" className="longField" expandable="true"/>
  </td>
</tr>

<tr class="advancedSetting">
  <th class="noBorder"><label for="${propertyNames.nugetPackageSources}">NuGet package sources:</label></th>
  <td>
    <c:set var="note">Specifies NuGet package sources to use during the restore.</c:set>
    <props:multilineProperty name="${propertyNames.nugetPackageSources}" className="longField" expanded="true"
                             cols="60" rows="3" linkTitle="Sources" note="${note}"/>
    <bs:projectData type="NuGetFeedUrls" sourceFieldId="queryString"
                    targetFieldId="${propertyNames.nugetPackageSources}" popupTitle="Select TeamCity NuGet feeds"/>
    <span class="error" id="error_${propertyNames.nugetPackageSources}"></span>
  </td>
</tr>
