[#ftl]
[#assign soapAvailable=docsxml.soap.wsdls.wsdl.endpointInterface?size > 0/]
[#assign restAvailable=docsxml.rest.resources.resource?size > 0/]
[#function scrubPathToFilename path]
    [#return "path_" + path?string?replace("/", "_")?replace(":","-")?replace("{", "-")?replace("}", "-") + ".html"/]
[/#function]
[#function tocNeeded(nodelist)]
    [#return nodelist?size > 2/]
[/#function]
[#function attributeExists(node)]
    [#if node?size??]
    [#--if there is a 'size' property, then it's not an attribute--]
        [#return node?size > 0/]
    [#else]
        [#return node?node_type = "attribute"/]
    [/#if]
[/#function]
[#function isDeprecated element]
    [#return (getTagValues(element, "deprecated")?size > 0)/]
[/#function]
[#function getTagValues element tagName]
    [#assign tagvalues = [] /]
    [#list element.tag as tag]
        [#if tag.@name=tagName]
            [#assign tagvalues = tagvalues + [ tag ] /]
        [/#if]
    [/#list]
    [#if element?node_name = "resource"]
        [#assign operationtags = [] /]
        [#list element.operation as operation]
            [#list operation.tag as tag]
                [#if tag.@name=tagName]
                    [#assign operationtags = operationtags + [ tag ] /]
                [/#if]
            [/#list]
        [/#list]
        [#if operationtags?size = element.operation?size]
            [#assign tagvalues = tagvalues + [ operationtags[0] ]/]
        [/#if]
    [/#if]
    [#return tagvalues/]
[/#function]

[#--set up the subnavigation menus--]
[#assign nav_sections = { "Data Model" : "model.html"} /]
[#if soapAvailable]
    [#assign nav_sections = nav_sections + { "SOAP" : "soap.html" }/]
[/#if]
[#if restAvailable]
    [#assign nav_sections = nav_sections + { "REST" : "rest.html" }/]
[/#if]
[#if ((downloadsxml??) && (downloadsxml.download?size > 0))]
    [#assign nav_sections = nav_sections + { "Files and Libraries" : "downloads.html"} /]
[/#if]

[#assign nav_resource_groups = {}/]
[#list docsxml.rest.groups.group?sort_by("name") as group]
    [#assign nav_resource_groups = nav_resource_groups + { group.name : "resource_" + group.name + ".html"}/]
[/#list]

[#assign nav_paths = {}/]
[#list docsxml.rest.resources.resource?sort_by("@name") as resource]
    [#assign nav_paths = nav_paths + {resource.@name?string : scrubPathToFilename(resource.@name)}/]
[/#list]

[#assign nav_wsdls = {}/]
[#assign nav_eis_by_ns = {}/]
[#list docsxml.soap.wsdls.wsdl as wsdl]
    [#assign nav_wsdls = nav_wsdls + {wsdl.@namespaceId?string : "soap_" + wsdl.@namespaceId?string + ".html"}/]
    [#assign nav_eis_by_ns = nav_eis_by_ns + {wsdl.@namespaceId?string : {}}/]
    [#list wsdl.endpointInterface?sort_by("@name") as endpointInterface]
        [#assign tmp = nav_eis_by_ns[wsdl.@namespaceId?string] + {endpointInterface.@name?string : "soap_" + wsdl.@namespaceId?string + "_" + endpointInterface.@name?string + ".html"}/]
        [#assign nav_eis_by_ns = nav_eis_by_ns + {wsdl.@namespaceId?string : tmp}/]
    [/#list]
[/#list]

[#assign nav_models = {}/]
[#assign nav_typedefs_by_ns = {}/]
[#assign nav_elements_by_ns = {}/]
[#list docsxml.data.schema as schema]
    [#assign nav_models = nav_models + {schema.@namespaceId?string : schema.@namespaceId?string + ".html"}/]
    [#assign nav_elements_by_ns = nav_elements_by_ns + {schema.@namespaceId?string : {}}/]
    [#list schema.elements.element?sort_by("@name") as element]
        [#assign tmp = nav_elements_by_ns[schema.@namespaceId?string] + { element.@name?string + " element" : "el_" + schema.@namespaceId?string + "_" + element.@name?string + ".html"}/]
        [#assign nav_elements_by_ns = nav_elements_by_ns + {schema.@namespaceId?string : tmp}/]
    [/#list]
    [#assign nav_typedefs_by_ns = nav_typedefs_by_ns + {schema.@namespaceId?string : {}}/]
    [#list schema.types.type?sort_by("@name") as type]
        [#assign tmp = nav_typedefs_by_ns[schema.@namespaceId?string] + {type.@name?string : schema.@namespaceId?string + "_" + type.@name?string + ".html"}/]
        [#assign nav_typedefs_by_ns = nav_typedefs_by_ns + {schema.@namespaceId?string : tmp}/]
    [/#list]
[/#list]

[#macro boilerplate title=docsxml.@title!"Web API" subnav=[{"title" : "Home", "href" : indexPageName}] codeblocks=true]
<!doctype html>
<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7 ]> <html class="no-js ie6" lang="en"> <![endif]-->
<!--[if IE 7 ]>    <html class="no-js ie7" lang="en"> <![endif]-->
<!--[if IE 8 ]>    <html class="no-js ie8" lang="en"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
<head>
    <meta charset="utf-8">

    <!-- Always force latest IE rendering engine (even in intranet) & Chrome Frame
         Remove this if you use the .htaccess -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

    <title>${title}</title>

    <!-- Mobile viewport optimized: j.mp/bplateviewport -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- CSS: implied media="all" -->
    <link rel="stylesheet" href="css/style.css?v=2">
    <link rel="stylesheet" href="css/prettify.css">
    [#list additionalCssFiles as additionalCssFile]
        <link rel="stylesheet" href="${additionalCssFile}">
    [/#list]

    <!-- All JavaScript at the bottom, except for Modernizr which enables HTML5 elements & feature detects -->
    <script src="js/libs/modernizr-1.7.min.js"></script>

</head>

<body class="home">

<div class="container">
    <header>
        <div id="header" class="column first last span-20">
            <span id="site-name" class="column span-18 append-1 prepend-1 first last"><a href="${indexPageName}">${title}</a></span>
            <span id="site-logo"><a href="http://thistech.com/"><img src="images/thistech-logo.png" height="75px" class="logo"></a></span>

            <div id="primary" class="column span-18 append-1 prepend-1 first last">
                <ul class="navigation">
                    [#if soapAvailable]
                        <li id="nav-soap"><a href="soap.html">SOAP</a></li>
                    [/#if]
                    [#if restAvailable]
                        <li id="nav-rest"><a href="rest.html">REST</a></li>
                    [/#if]
                    <li id="nav-data"><a href="model.html">Data Model</a></li>
                    [#if ((downloadsxml??) && (downloadsxml.download?size > 0))]
                        <li id="nav-downloads"><a href="downloads.html">Files and Libraries</a></li>
                    [/#if]
                </ul>
            </div>
            <div>
                <ul class="xbreadcrumbs" id="breadcrumbs">
                    [#list subnav as crumb]
                        <li[#if !crumb_has_next] class="current"[/#if]>
                            <a href="${crumb.href}"[#if crumb_index = 0] class="home"[/#if]>${crumb.title}</a>[#if crumb_has_next] &gt;[/#if]
                            [#if crumb.subnav??]
                                <ul>
                                    [#list crumb.subnav?keys as label]
                                        <li><a href="${crumb.subnav[label]}">${label}</a></li>
                                    [/#list]
                                </ul>
                            [/#if]
                        </li>
                    [/#list]
                </ul>
            </div>
        </div>
    </header>
    <div id="main" class="column first last span-20">
        [#nested/]

        <div class="clear" />
    </div>
    <footer>
        <div id="footer">
            [#if attributeExists(docsxml.@copyright)]
                Copyright &copy; <script type="text/javascript" language="javascript">d = new Date;document.write(d.getFullYear());</script>
                <span property="cc:attributionName">${docsxml.@copyright}</span><br/>
            [/#if]
            Generated by <a href="http://enunciate.codehaus.org">Enunciate</a>.
        </div>
    </footer>
</div> <!--! end of #container -->

<!-- JavaScript at the bottom for fast page loading -->

<!-- Grab Google CDN's jQuery, with a protocol relative URL; fall back to local if necessary -->
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.js"></script>
<script>window.jQuery || document.write("<script src='js/libs/jquery-1.5.1.min.js'>\x3C/script>")</script>

<!--manage the navigation menu-->
<script src="js/libs/xbreadcrumbs.js"></script>
<script>
    $(function() {
        $('#breadcrumbs').xBreadcrumbs();
    });
</script>

    [#if codeblocks]

    <!-- prettify code blocks. see http://code.google.com/p/google-code-prettify/ -->
    <script src="js/libs/prettify/prettify.js"></script>
    <script>
        $(function() {
            prettyPrint();
        });
    </script>

    [/#if]
<!--[if lt IE 7 ]>
<script src="js/libs/dd_belatedpng.js"></script>
<script>DD_belatedPNG.fix("img, .png_bg"); // Fix any <img> or .png_bg bg-images. Also, please read goo.gl/mZiyb </script>
<![endif]-->

[#--
  todo: uncomment to support google analytics measurements...
  <!-- mathiasbynens.be/notes/async-analytics-snippet Change UA-XXXXX-X to be your site's ID -->
  <script>
    var _gaq=[["_setAccount","UA-XXXXX-X"],["_trackPageview"]];
    (function(d,t){var g=d.createElement(t),s=d.getElementsByTagName(t)[0];g.async=1;
    g.src=("https:"==location.protocol?"//ssl":"//www")+".google-analytics.com/ga.js";
    s.parentNode.insertBefore(g,s)}(document,"script"));
  </script>
--]
</body>
</html>
[/#macro]
[@file name=indexPageName charset="utf-8"]
    [@boilerplate]
        [#if docsxml.documentation?size > 0]
        <h1>Introduction</h1>

        <p>${docsxml.documentation}</p>

        [/#if]
        [#if restAvailable]

        <h1>REST Resources</h1>
        <p>
            This API supports a <a href="http://en.wikipedia.org/wiki/Representational_State_Transfer">Representational State Transfer (REST)</a>
            model for accessing a set of resources through a fixed set of operations. The following resources are accessible through the RESTful model:
        </p>
        <ul>
            [#if ((groupRestResources!"byDocumentationGroup") != "byPath") && docsxml.rest.groups.group?size > 0]
                [#list docsxml.rest.groups.group?sort_by("name") as group]
                    [@processResourceGroup group=group/]
                    <li><a href="resource_${group.name}.html">${group.name}</a></li>
                [/#list]
            [#else]
                [#list docsxml.rest.resources.resource?sort_by("@name") as resource]
                    [@processResource resource=resource/]
                    <li[#if isDeprecated(resource)] class="deprecated"[/#if]><a href="${scrubPathToFilename(resource.@name)}">${resource.@name}</a></li>
                [/#list]
            [/#if]
        </ul>
            [#if ((downloadsxml??) && (downloadsxml.download?size > 0))]
            <p>
                The REST resources expose a data model that is supported by a set of client-side libraries that are made available on the
                <a href="downloads.html">files and libraries</a> page.
            </p>
            [/#if]
            [#if attributeExists(docsxml.rest.@wadl)]
            <p>
                There is also a <a href="${docsxml.rest.@wadl}">WADL document</a> describing the REST API.
            </p>
            [/#if]
        [/#if]
        [#if soapAvailable]

        <h1>SOAP Endpoints</h1>
        <p>
            This API is exposed through a set of <a href="http://www.ws-i.org/Profiles/BasicProfile-1.0.html">WSI Basic Profile</a>
            -compliant SOAP v1.1 endpoints. The API supports <a href="http://www.w3.org/TR/2005/REC-xop10-20050125/">XML-binary Optimized Pacakging (XOP)</a>
            and <a href="http://www.w3.org/TR/2004/WD-soap12-mtom-20040608/">SOAP Message Transmission Optimization Mechanism (MTOM)</a>
            for transmission of binary data. The SOAP API is described by the following endpoints:
        </p>
            [#list docsxml.soap.wsdls.wsdl as wsdl]
                [@processWsdl wsdl=wsdl/]
            <h2>Namespace "${wsdl.@namespaceId}"</h2>
            <table>
                <tr>
                    <th>Namespace URI</th>
                    <td>[#if wsdl.@namespace?length > 0]${wsdl.@namespace}[#else](default namespace)[/#if]</td>
                </tr>
                [#if attributeExists(wsdl.@file)]
                    <tr>
                        <th>WSDL</th>
                        <td><a href="${wsdl.@file}">${wsdl.@file}</a></td>
                    </tr>
                [/#if]
            </table>
            <h3>Endpoints</h3>
            <ul>
                [#list wsdl.endpointInterface?sort_by("@name") as endpointInterface]
                    [@processEndpointInterface endpointInterface=endpointInterface/]
                    <li[#if isDeprecated(endpointInterface)] class="deprecated"[/#if]><a href="soap_${wsdl.@namespaceId}_${endpointInterface.@name}.html">${endpointInterface.@name}</a></li>
                [/#list]
            </ul>
            [/#list]
            [#if ((downloadsxml??) && (downloadsxml.download?size > 0))]
            <p>
                The SOAP API is also accessible by a set of client-side libraries that can be downloaded from the <a href="downloads.html">files and libraries page</a>.
            </p>
            [/#if]
        [/#if]

    <h1>Data Model</h1>
    <p>All endpoints act on a common set of data. The data can be represented with difference media (i.e., "MIME") types, depending on the endpoint that consumes and/or produces the data. The data can be described by <a href="http://www.w3.org/XML/Schema">XML Schema</a>, which definitively describes the XML representation of the data.</p>
    <p>This section describes the data using terms based on XML Schema. Data can be grouped by namespace, with a schema document describing the elements and types of the namespace. Generally speaking, types define the structure of the data and elements are instances of a type. For example, elements are usually produced by (or consumed by) a REST endpoint, and the structure of each element is described by its type.</p>

        [#list docsxml.data.schema as schema]
            [@processSchema schema=schema/]

        <h2>Namespace "${schema.@namespaceId}"</h2>
        <table>
            <tr>
                <th>Namespace URI</th>
                <td>[#if schema.@namespace?length > 0]${schema.@namespace}[#else](default namespace)[/#if]</td>
            </tr>
            [#if attributeExists(schema.@file)]
                <tr>
                    <th>XSD</th>
                    <td><a href="${schema.@file}">${schema.@file}</a></td>
                </tr>
            [/#if]
        </table>
            [#if schema.elements.element?size > 0]

            <h3>Data Elements</h3>

            <ul>
                [#list schema.elements.element?sort_by("@name") as element]
                    <li[#if isDeprecated(element)] class="deprecated"[/#if]><a href="el_${schema.@namespaceId}_${element.@name}.html">${element.@name}</a></li>
                [/#list]
            </ul>
            [/#if]
            [#if schema.types.type?size > 0]

            <h3>Data Types</h3>
            <ul>
                [#list schema.types.type?sort_by("@name") as type]
                    <li[#if isDeprecated(type)] class="deprecated"[/#if]><a href="${schema.@namespaceId}_${type.@name}.html">${type.@name}</a></li>
                [/#list]
            </ul>
            [/#if]
        [/#list]
        [#if docsxml.data.jsonSchema.type?size > 0]

        <h1>JSON Data Types</h1>
        <ul>
            [#list docsxml.data.jsonSchema.type as type]
                [@processJsonType type=type/]
                <li[#if isDeprecated(type)] class="deprecated"[/#if]><a href="json_${type?parent.@schemaId}_${type.@name}.html">${type.@name}</a></li>
            [/#list]
        </ul>
        [/#if]
    [/@boilerplate]
[/@file]
[@file name="model.html" charset="utf-8"]
    [@boilerplate title="Data Model" subnav=[{"title" : "Home", "href" : indexPageName}, {"title" : "Data Model" , "href" : "model.html", "subnav" : nav_sections}]]
    <h1>Data Model</h1>
    <p>All endpoints act on a common set of data. The data can be represented with difference media (i.e., "MIME") types, depending on the endpoint that consumes and/or produces the data. The data can be described by <a href="http://www.w3.org/XML/Schema">XML Schema</a>, which definitively describes the XML representation of the data.</p>
    <p>This section describes the data using terms based on XML Schema. Data can be grouped by namespace, with a schema document describing the elements and types of the namespace. Generally speaking, types define the structure of the data and elements are instances of a type. For example, elements are usually produced by (or consumed by) a REST endpoint, and the structure of each element is described by its type.</p>

        [#list docsxml.data.schema as schema]

        <h2>Namespace "${schema.@namespaceId}"</h2>
        <table>
            <tr>
                <th>Namespace URI</th>
                <td>[#if schema.@namespace?length > 0]${schema.@namespace}[#else](default namespace)[/#if]</td>
            </tr>
            [#if attributeExists(schema.@file)]
                <tr>
                    <th>XSD</th>
                    <td><a href="${schema.@file}">${schema.@file}</a></td>
                </tr>
            [/#if]
        </table>
            [#if schema.elements.element?size > 0]

            <h3>Data Elements</h3>

            <ul>
                [#list schema.elements.element?sort_by("@name") as element]
                    <li[#if isDeprecated(element)] class="deprecated"[/#if]><a href="el_${schema.@namespaceId}_${element.@name}.html">${element.@name}</a></li>
                [/#list]
            </ul>
            [/#if]
            [#if schema.types.type?size > 0]

            <h3>Data Types</h3>
            <ul>
                [#list schema.types.type?sort_by("@name") as type]
                    <li[#if isDeprecated(type)] class="deprecated"[/#if]><a href="${schema.@namespaceId}_${type.@name}.html">${type.@name}</a></li>
                [/#list]
            </ul>
            [/#if]
        [/#list]
    [/@boilerplate]
[/@file]
[#if ((downloadsxml??) && (downloadsxml.download?size > 0))]
    [@file name="downloads.html" charset="utf-8"]
        [@boilerplate title="Files and Libraries" subnav=[{"title" : "Home", "href" : indexPageName}, { "title" : "Files and Libraries" , "href" : "downloads.html" , "subnav" : nav_sections}] codeblocks=true]
        <h1>Files and Libraries</h1>

        <p>The following files and libraries are available:</p>

        <ul>
            [#list downloadsxml.download as download]
                <li><a href="#${download.@name}">${download.@name}</a></li>
            [/#list]
        </ul>
            [#list downloadsxml.download as download]
            <a name="${download.@name}"></a>
            <h2>${download.@name}</h2>
                [#list download.created as created]
                <p class="note">Created ${created}</p>
                [/#list]
                [#list download.description as description]
                <p>${description}</p>
                [/#list]
            <h3>Files</h3>
            <table>
                <tr>
                    <th>Name</th>
                    <th>Size</th>
                    [#if download.files.file?size > 1]
                        <th>Description</th>
                    [/#if]
                </tr>
                [#list download.files.file as file]
                    <tr>
                        <td><a href="${file.@name}">${file.@name}</a></td>
                        <td>${file.@size}</td>
                        [#if file_index > 0 || file_has_next]
                            <td>${file}</td>
                        [/#if]
                    </tr>
                [/#list]
            </table>
            [/#list]
        [/@boilerplate]
    [/@file]
[/#if]
[#if restAvailable]
    [@file name="rest.html" charset="utf-8"]
        [@boilerplate title="REST" subnav=[{"title" : "Home", "href" : indexPageName}, {"title" : "REST" , "href" : "rest.html", "subnav" : nav_sections}]]
        <h1>REST Resources</h1>
        <p>
            This API supports a <a href="http://en.wikipedia.org/wiki/Representational_State_Transfer">Representational State Transfer (REST)</a>
            model for accessing a set of resources through a fixed set of operations. The following resources are accessible through the RESTful model:
        </p>
        <ul>
            [#if ((groupRestResources!"byDocumentationGroup") != "byPath") && docsxml.rest.groups.group?size > 0]
                [#list docsxml.rest.groups.group?sort_by("name") as group]
                    <li><a href="resource_${group.name}.html">${group.name}</a></li>
                [/#list]
            [#else]
                [#list docsxml.rest.resources.resource?sort_by("@name") as resource]
                    <li[#if isDeprecated(resource)] class="deprecated"[/#if]><a href="${scrubPathToFilename(resource.@name)}">${resource.@name}</a></li>
                [/#list]
            [/#if]
        </ul>
            [#if ((downloadsxml??) && (downloadsxml.download?size > 0))]
            <p>
                The REST resources expose a data model that is supported by a set of client-side libraries that are made available on the
                <a href="downloads.html">files and libraries</a> page.
            </p>
            [/#if]
            [#if attributeExists(docsxml.rest.@wadl)]
            <p>
                There is also a <a href="${docsxml.rest.@wadl}">WADL document</a> describing the REST API.
            </p>
            [/#if]
        [/@boilerplate]
    [/@file]
[/#if]
[#if soapAvailable]
    [@file name="soap.html" charset="utf-8"]
        [@boilerplate title="SOAP" subnav=[{"title" : "Home", "href" : indexPageName}, {"title" : "SOAP" , "href" : "soap.html", "subnav" : nav_sections}]]
        <h1>SOAP Endpoints</h1>
        <p>
            This API is exposed through a set of <a href="http://www.ws-i.org/Profiles/BasicProfile-1.0.html">WSI Basic Profile</a>
            -compliant SOAP v1.1 endpoints. The API supports <a href="http://www.w3.org/TR/2005/REC-xop10-20050125/">XML-binary Optimized Pacakging (XOP)</a>
            and <a href="http://www.w3.org/TR/2004/WD-soap12-mtom-20040608/">SOAP Message Transmission Optimization Mechanism (MTOM)</a>
            for transmission of binary data. The SOAP API is fully described by the following endpoints:
        </p>
            [#list docsxml.soap.wsdls.wsdl as wsdl]
            <h2>Namespace "${wsdl.@namespaceId}"</h2>
            <table>
                <tr>
                    <th>Namespace URI</th>
                    <td>[#if wsdl.@namespace?length > 0]${wsdl.@namespace}[#else](default namespace)[/#if]</td>
                </tr>
                [#if attributeExists(wsdl.@file)]
                    <tr>
                        <th>WSDL</th>
                        <td><a href="${wsdl.@file}">${wsdl.@file}</a></td>
                    </tr>
                [/#if]
            </table>
            <h3>Endpoints</h3>
            <ul>
                [#list wsdl.endpointInterface?sort_by("@name") as endpointInterface]
                    <li[#if isDeprecated(endpointInterface)] class="deprecated"[/#if]><a href="soap_${wsdl.@namespaceId}_${endpointInterface.@name}.html">${endpointInterface.@name}</a></li>
                [/#list]
            </ul>
            [/#list]
            [#if ((downloadsxml??) && (downloadsxml.download?size > 0))]
            <p>
                The SOAP API is also accessible by a set of client-side libraries that can be downloaded from the <a href="downloads.html">files and libraries page</a>.
            </p>
            [/#if]
        [/@boilerplate]
    [/@file]
[/#if]
[#macro processWsdl wsdl]
    [@file name="soap_" + wsdl.@namespaceId + ".html" charset="utf-8"]
        [@boilerplate title="SOAP: " + wsdl.@namespaceId subnav=[{"title" : "Home", "href" : indexPageName}, {"title" : "SOAP" , "href" : "soap.html", "subnav" : nav_sections }, { "title" : wsdl.@namespaceId?string , "href" : "soap_" + wsdl.@namespaceId + ".html" }]]
        <h1>${wsdl.@namespaceId}</h1>
        <table>
            <tr>
                <th>Namespace URI</th>
                <td>[#if wsdl.@namespace?length > 0]${wsdl.@namespace}[#else](default namespace)[/#if]</td>
            </tr>
            [#if attributeExists(wsdl.@file)]
                <tr>
                    <th>WSDL</th>
                    <td><a href="${wsdl.@file}">${wsdl.@file}</a></td>
                </tr>
            [/#if]
        </table>
        <h3>Endpoints</h3>
        <ul>
            [#list wsdl.endpointInterface?sort_by("@name") as endpointInterface]
                <li[#if isDeprecated(endpointInterface)] class="deprecated"[/#if]><a href="soap_${wsdl.@namespaceId}_${endpointInterface.@name}.html">${endpointInterface.@name}</a></li>
            [/#list]
        </ul>
        [/@boilerplate]
    [/@file]
[/#macro]
[#macro processResourceGroup group]
    [@file name="resource_" + group.name + ".html" charset="utf-8"]
        [@boilerplate title=group.name subnav=[{"title" : "Home", "href" : indexPageName}, {"title" : "REST" , "href" : "rest.html", "subnav" : nav_sections}, {"title" : group.name , "href" : "resource_" + group.name + ".html", "subnav" : nav_resource_groups}]]
        <h1>${group.name}</h1>
        <p>
            [#list group.documentation as documentation]
            ${documentation}
            [/#list]
            [#assign resources=[] /]
            [#list docsxml.rest.resources.resource?sort_by("@name") as resource]
                [#list resource.groups.group as rg]
                    [#if rg = group.name]
                        [#assign resources=resources + resource /]
                        [#break/]
                    [/#if]
                [/#list]
            [/#list]
            The following resources are part of this group:
        </p>
        <ul>
            [#list resources as resource]
                <li[#if isDeprecated(resource)] class="deprecated"[/#if]><a href="#${scrubPathToFilename(resource.@name)}">${resource.@name}</a></li>
            [/#list]
        </ul>
            [#list resources as resource]
            <a name="${scrubPathToFilename(resource.@name)}"></a>
                [@processRawResource resource=resource/]
            [/#list]
        [/@boilerplate]
    [/@file]
[/#macro]
[#macro processResource resource]
    [#assign resource_filename=scrubPathToFilename(resource.@name)/]
    [@file name=resource_filename charset="utf-8"]
        [@boilerplate title=group subnav=[{"title" : "Home", "href" : indexPageName}, { "title" : "REST" , "href" : "rest.html", "subnav" : nav_sections }, {"title" : resource.@name , "href" : resource_filename, "subnav" : nav_paths}]]
            [@processRawResource resource=resource/]
        [/@boilerplate]
    [/@file]
[/#macro]
[#macro processRawResource resource]
<h1>${resource.@name}</h1>
    [#if isDeprecated(resource)]
    <p class="alert">This resource has been deprecated.</p>
    [/#if]
    [#if attributeExists(resource.@subcontext) && !disableRestMountpoint!false]
    <p class="note">Mount Point: <a href="${apiRelativePath}${resource.@subcontext}${resource.@name}">${resource.@subcontext}${resource.@name}</a></p>
    [/#if]
    [#assign versionTags = getTagValues(resource, "version") /]
    [#if versionTags?size > 0]
    <p class="note">Version: ${versionTags[0]}</p>
    [/#if]
    [#if tocNeeded(resource.operation)]
    <p>
        The following operations are supported on this resource:
    </p>
    <ul>
        [#list resource.operation as operation]
            <li[#if isDeprecated(operation)] class="deprecated"[/#if]><a href="#${operation.@name}">${operation.@name}</a></li>
            [#list operation.alias as alias]
                <li[#if isDeprecated(operation)] class="deprecated"[/#if]><a href="#${operation.@name}">${alias}</a></li>
            [/#list]
        [/#list]
    </ul>
    [/#if]
    [#list resource.operation as operation]
    <a name="${operation.@name}"></a>
    <h2>${operation.@name}</h2>
        [#list operation.alias as alias]
        <p class="note">Alias: ${alias}</p>
        [/#list]
        [#assign sinceTags = getTagValues(operation, "since") /]
        [#if sinceTags?size > 0]
        <p class="note">Available Since: ${sinceTags[0]}</p>
        [/#if]
        [#assign versionTags = getTagValues(operation, "version") /]
        [#if versionTags?size > 0]
        <p class="note">Version: ${versionTags[0]}</p>
        [/#if]
        [#if isDeprecated(operation)]
        <p class="alert">This operation has been deprecated. [#list operation.tag as tag][#if tag.@name="deprecated"] ${tag}[/#if][/#list]</p>
        [/#if]
        [#list operation.documentation as documentation]
        <p>${documentation}</p>
        [/#list]
        [#list operation.parameter as parameter]
            [#if parameter_index = 0]
            <h3>Parameters</h3>
            <table>
                <tr>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Type</th>
                    <th>Default</th>
                </tr>
            [/#if]
            <tr>
                <td>[#if attributeExists(parameter.@name)]${parameter.@name}[/#if]</td>
                <td>${parameter}</td>
                <td>[#if attributeExists(parameter.@type)]${parameter.@type}[/#if]</td>
                <td>[#if attributeExists(parameter.@defaultValue)]${parameter.@defaultValue}[/#if]</td>
            </tr>
            [#if !parameter_has_next]
            </table>
            [/#if]
        [/#list]
        [#list operation.inValue as inValue]
        <h3>Request Body</h3>
        <table>
            [#assign custom=true]
            [#list inValue.xmlElement as xmlElement]
                [#if attributeExists(xmlElement.@elementName)]
                    [#assign custom=false/]
                    <tr>
                        <th>Element</th>
                        [#if attributeExists(xmlElement.@elementSchemaId)]
                            <td><a href="el_${xmlElement.@elementSchemaId}_${xmlElement.@elementName}.html">${xmlElement.@elementName}</a></td>
                        [#else]
                            <td>${xmlElement.@elementName}</td>
                        [/#if]
                    </tr>
                [/#if]
            [/#list]
            [#list inValue.jsonElementRef as jsonElement]
                [#if attributeExists(jsonElement.@elementName)]
                    [#assign custom=false/]
                    <tr>
                        <th>Element</th>
                        [#if attributeExists(jsonElement.@elementSchemaId)]
                            <td><a href="json_${jsonElement.@elementSchemaId}_${jsonElement.@elementName}.html">${jsonElement.@elementName}</a></td>
                        [#else]
                            <td>${jsonElement.@elementName}</td>
                        [/#if]
                    </tr>
                [/#if]
            [/#list]
            [#if custom]
                <tr>
                    <th>Element</th>
                    <td>(custom)</td>
                </tr>
            [/#if]
            <tr>
                <th>Media Types</th>
                <td>[#list operation.contentType as contentType][#if contentType.@consumable = "true"]${contentType.@type}[#if contentType_has_next]<br/>[/#if][/#if][/#list]</td>
            </tr>
        </table>
            [#list inValue.documentation as documentation]
            <p>${documentation}</p>
            [/#list]
        [/#list]
        [#list operation.outValue as outValue]
        <h3>Response Body</h3>
        <table>
            [#assign custom=true]
            [#list outValue.xmlElement as xmlElement]
                [#if attributeExists(xmlElement.@elementName)]
                    [#assign custom=false/]
                    <tr>
                        <th>Element</th>
                        [#if attributeExists(xmlElement.@elementSchemaId)]
                            <td><a href="el_${xmlElement.@elementSchemaId}_${xmlElement.@elementName}.html">${xmlElement.@elementName}</a></td>
                        [#else]
                            <td>${xmlElement.@elementName}</td>
                        [/#if]
                    </tr>
                [/#if]
            [/#list]
            [#list outValue.jsonElementRef as jsonElement]
                [#if attributeExists(jsonElement.@elementName)]
                    [#assign custom=false/]
                    <tr>
                        <th>Element</th>
                        [#if attributeExists(jsonElement.@elementSchemaId)]
                            <td><a href="json_${jsonElement.@elementSchemaId}_${jsonElement.@elementName}.html">${jsonElement.@elementName}</a></td>
                        [#else]
                            <td>${jsonElement.@elementName}</td>
                        [/#if]
                    </tr>
                [/#if]
            [/#list]
            [#if custom]
                <tr>
                    <th>Element</th>
                    <td>(custom)</td>
                </tr>
            [/#if]
            <tr>
                <th>Media Types</th>
                <td>[#list operation.contentType as contentType][#if contentType.@produceable = "true"]${contentType.@type}[#if contentType_has_next]<br/>[/#if][/#if][/#list]</td>
            </tr>
        </table>
            [#list outValue.documentation as documentation]
            <p>${documentation}</p>
            [/#list]
        [/#list]
        [#list operation.statusCode as statusCode]
            [#if statusCode_index = 0]
            <h3>Status Codes</h3>
            <table>
                <tr>
                    <th>Code</th>
                    <th>Description</th>
                </tr>
            [/#if]
            <tr>
                <td>${statusCode.@code}</td>
                <td>${statusCode}</td>
            </tr>
            [#if !statusCode_has_next]
            </table>
            [/#if]
        [/#list]
        [#list operation.warning as warning]
            [#if warning_index = 0]
            <h3>Warnings</h3>
            <table>
                <tr>
                    <th>Code</th>
                    <th>Description</th>
                </tr>
            [/#if]
            <tr>
                <td>${warning.@code}</td>
                <td>${warning}</td>
            </tr>
            [#if !warning_has_next]
            </table>
            [/#if]
        [/#list]
        [#list operation.responseHeader as responseHeader]
            [#if responseHeader_index = 0]
            <h3>Response Headers</h3>
            <table>
                <tr>
                    <th>Name</th>
                    <th>Description</th>
                </tr>
            [/#if]
            <tr>
                <td>${responseHeader.name}</td>
                <td>${responseHeader.documentation}</td>
            </tr>
            [#if !responseHeader_has_next]
            </table>
            [/#if]
        [/#list]
    [/#list]
[/#macro]
[#macro processEndpointInterface endpointInterface]
    [@file name="soap_" + endpointInterface?parent.@namespaceId + "_" + endpointInterface.@name + ".html" charset="utf-8"]
        [@boilerplate title=endpointInterface.@name subnav=[{"title" : "Home", "href" : indexPageName}, {"title" : "SOAP" , "href" : "soap.html", "subnav" : nav_sections }, {"title" : endpointInterface?parent.@namespaceId , "href" : "soap_" + endpointInterface?parent.@namespaceId + ".html", "subnav" : nav_wsdls }, {"title" : endpointInterface.@name , "href" : "soap_" + endpointInterface.@name + ".html", "subnav" : nav_eis_by_ns[endpointInterface?parent.@namespaceId?string]}]]
        <h1>${endpointInterface.@name}</h1>
            [#if isDeprecated(endpointInterface)]
            <p class="alert">This endpoint has been deprecated.[#list endpointInterface.tag as tag][#if tag.@name="deprecated"] ${tag}[/#if][/#list]</p>
            [/#if]
        <table>
            <tr>
                <th>Namespace</th>
                <td>[#if (endpointInterface?parent).@namespace?length > 0]${(endpointInterface?parent).@namespace}[#else](default namespace)[/#if]</td>
            </tr>
            [#if attributeExists((endpointInterface?parent).@file)]
                <tr>
                    <th>WSDL</th>
                    <td><a href="${(endpointInterface?parent).@file}">${(endpointInterface?parent).@file}</a></td>
                </tr>
            [/#if]
            [#if attributeExists(endpointInterface.@path)]
                <tr>
                    <th>Path</th>
                    <td><a href="${apiRelativePath}${endpointInterface.@path}">${endpointInterface.@path}</a></td>
                </tr>
            [/#if]
            [#assign versionTags = getTagValues(endpointInterface, "version") /]
            [#if versionTags?size > 0]
                <tr>
                    <th>Version</th>
                    <td>${versionTags[0]}</td>
                </tr>
            [/#if]
        </table>
        <p>
            [#if endpointInterface.documentation?size > 0]
                [#list endpointInterface.documentation as documentation]
                ${documentation}
                [/#list]
            [/#if]
            [#if tocNeeded(endpointInterface.method)]
                The following methods are available on this endpoint:
            </p>
            <ul>
                [#list endpointInterface.method as method]
                    <li[#if isDeprecated(method)] class="deprecated"[/#if]><a href="#${method.@name}">${method.@name}</a></li>
                [/#list]
            </ul>
            [/#if]
            [#list endpointInterface.method as method]
            <a name="${method.@name}"></a>
            <h2>${method.@name}</h2>
                [#if isDeprecated(method)]
                <p class="alert">This method has been deprecated.[#list method.tag as tag][#if tag.@name="deprecated"] ${tag}[/#if][/#list]</p>
                [/#if]
                [#assign sinceTags = getTagValues(method, "since") /]
                [#if sinceTags?size > 0]
                <p class="note">Available Since: ${sinceTags[0]}</p>
                [/#if]
                [#assign versionTags = getTagValues(method, "version") /]
                [#if versionTags?size > 0]
                <p class="note">Version: ${versionTags[0]}</p>
                [/#if]
                [#list method.documentation as documentation]
                <p>${documentation}</p>
                [/#list]
                [#list method["parameter[@input='true']"] as parameter]
                    [#if parameter_index = 0]
                    <h3>Input Parameters</h3>
                    <table>
                        <tr>
                            <th>Name</th>
                            <th>Type</th>
                            <th>Description</th>
                        </tr>
                    [/#if]
                    <tr>
                        <td>${parameter.@name}</td>
                        <td>[#if attributeExists(parameter.@xmlTypeName)][#if attributeExists(parameter.@xmlTypeSchemaId)]<a href="${parameter.@xmlTypeSchemaId}_${parameter.@xmlTypeName}.html">${parameter.@xmlTypeName}</a>[#else]${parameter.@xmlTypeName}[/#if][#else](unknown)[/#if]</td>
                        <td>${parameter}</td>
                    </tr>
                    [#if !parameter_has_next]
                    </table>
                    [/#if]
                [/#list]
                [#list method["parameter[@output='true']"] as parameter]
                    [#if parameter_index = 0]
                    <h3>Output Parameters</h3>
                    <table>
                        <tr>
                            <th>Name</th>
                            <th>Type</th>
                            <th>Description</th>
                        </tr>
                    [/#if]
                    <tr>
                        <td>${parameter.@name}</td>
                        <td>[#if attributeExists(parameter.@xmlTypeName)][#if attributeExists(parameter.@xmlTypeSchemaId)]<a href="${parameter.@xmlTypeSchemaId}_${parameter.@xmlTypeName}.html">${parameter.@xmlTypeName}</a>[#else]${parameter.@xmlTypeName}[/#if][#else](unknown)[/#if]</td>
                        <td>${parameter}</td>
                    </tr>
                    [#if !parameter_has_next]
                    </table>
                    [/#if]
                [/#list]
                [#list method.result as result]
                <h3>Return Value</h3>

                    [#if attributeExists(result.@xmlTypeName)]
                    <table>
                        <tr>
                            <th>Type</th>
                            <td>[#if attributeExists(result.@xmlTypeSchemaId)]<a href="${result.@xmlTypeSchemaId}_${result.@xmlTypeName}.html">${result.@xmlTypeName}</a>[#else]${result.@xmlTypeName}[/#if]</td>
                        </tr>
                    </table>
                    [/#if]

                <p>${result}</p>
                [/#list]
                [#list method.fault as fault]
                    [#if fault_index = 0]
                    <h3>Faults</h3>
                    <table>
                        <tr>
                            <th>Name</th>
                            <!--todo: add the parameter type and whether its a collection-->
                            <th>Description</th>
                        </tr>
                    [/#if]
                    <tr>
                        <td>${fault.@name}</td>
                        <td>${fault}</td>
                    </tr>
                    [#if !fault_has_next]
                    </table>
                    [/#if]
                [/#list]
            [/#list]
        [/@boilerplate]
    [/@file]
[/#macro]
[#macro processSchema schema]
    [@file name=schema.@namespaceId + ".html" charset="utf-8"]
        [@boilerplate title="Namespace: " + schema.@namespaceId subnav=[{"title" : "Home", "href" : indexPageName}, {"title" : "Data Model" , "href" : "model.html", "subnav" : nav_sections}, {"title" : schema.@namespaceId , "href" : schema.@namespaceId + ".html", "subnav" : nav_models}]]
        <h1>Namespace ${schema.@namespaceId}</h1>
        <table>
            <tr>
                <th>Namespace</th>
                <td>[#if schema.@namespace?length > 0]${schema.@namespace}[#else](default namespace)[/#if]</td>
            </tr>
            [#if attributeExists(schema.@file)]
                <tr>
                    <th>XML Schema</th>
                    <td><a href="${schema.@file}">${schema.@file}</a></td>
                </tr>
            [/#if]
        </table>
            [#if schema.elements.element?size > 0]

            <h2>Elements</h2>

            <ul>
                [#list schema.elements.element?sort_by("@name") as element]
                    [@processElement element=element/]
                    <li[#if isDeprecated(element)] class="deprecated"[/#if]><a href="el_${schema.@namespaceId}_${element.@name}.html">${element.@name}</a></li>
                [/#list]
            </ul>
            [/#if]
            [#if schema.types.type?size > 0]

            <h2>Types</h2>

            <ul>
                [#list schema.types.type?sort_by("@name") as type]
                    [@processType type=type/]
                    <li[#if isDeprecated(type)] class="deprecated"[/#if]><a href="${schema.@namespaceId}_${type.@name}.html">${type.@name}</a></li>
                [/#list]
            </ul>
            [/#if]
        [/@boilerplate]
    [/@file]
[/#macro]
[#macro processElement element]
    [#assign schema=element?parent?parent/]
    [@file name="el_" + schema.@namespaceId + "_" + element.@name + ".html" charset="utf-8"]
        [@boilerplate title=element.@name subnav=[{"title" : "Home", "href" : indexPageName}, {"title" : "Data Model" , "href" : "model.html", "subnav" : nav_sections}, {"title" : schema.@namespaceId , "href" : schema.@namespaceId + ".html", "subnav" : nav_models}, {"title" : element.@name + " element" , "href" : "el_" + schema.@namespaceId + "_" + element.@name + ".html", "subnav" : nav_elements_by_ns[schema.@namespaceId?string]}] codeblocks=true]
        <h1>${element.@name} element</h1>
            [#if isDeprecated(element)]
            <p class="alert">This element has been deprecated.[#list element.tag as tag][#if tag.@name="deprecated"] ${tag}[/#if][/#list]</p>
            [/#if]
        <table>
            [#if attributeExists(element.@typeName) && attributeExists(element.@typeSchemaId)]
                <tr>
                    <th>Type</th>
                    <td><a href="${element.@typeSchemaId}_${element.@typeName}.html">${element.@typeName}</a></td>
                </tr>
            [/#if]
            <tr>
                <th>Namespace</th>
                <td>[#if schema.@namespace?length > 0]${schema.@namespace}[#else](default namespace)[/#if]</td>
            </tr>
            [#if attributeExists(schema.@file)]
                <tr>
                    <th>XML Schema</th>
                    <td><a href="${schema.@file}">${schema.@file}</a></td>
                </tr>
            [/#if]
            [#assign versionTags = getTagValues(element, "version") /]
            [#if versionTags?size > 0]
                <tr>
                    <th><Available Version</th>
                    <td>${versionTags[0]}</td>
                </tr>
            [/#if]
        </table>
            [#list element.documentation as documentation]
            <p>${documentation}</p>
            [/#list]
            [#list element.examplexml as examplexml]
            <h2>Example XML</h2>
            <code class="prettyprint lang-xml">${examplexml?string?xhtml}</code>
            [/#list]
            [#list element.examplejson as examplejson]
            <h2>Example JSON</h2>
            <code class="prettyprint lang-js">${examplejson?string?xhtml}</code>
            [/#list]
        [/@boilerplate]
    [/@file]
[/#macro]
[#macro processType type]
    [#assign schema=type?parent?parent/]
    [@file name=schema.@namespaceId + "_" + type.@name + ".html" charset="utf-8"]
        [@boilerplate title=type.@name subnav=[{"title" : "Home", "href" : indexPageName}, {"title" : "Data Model" , "href" : "model.html", "subnav" : nav_sections}, {"title" : schema.@namespaceId , "href" : schema.@namespaceId + ".html", "subnav" : nav_models} , {"title" : type.@name , "href" : schema.@namespaceId + "_" + type.@name + ".html", "subnav" : nav_typedefs_by_ns[schema.@namespaceId?string]}] codeblocks=true]
        <h1>${type.@name}</h1>
            [#if isDeprecated(type)]
            <p class="alert">This type has been deprecated.[#list type.tag as tag][#if tag.@name="deprecated"] ${tag}[/#if][/#list]</p>
            [/#if]
        <table>
            [#if attributeExists(type.@extendsType)]
                [#list docsxml.data.schema.types.type as candidate]
                    [#if attributeExists(candidate.@id) && (candidate.@id?string = type.@extendsType?string)]
                        <tr>
                            <th>Extends</th>
                            <td><a href="${candidate?parent?parent.@namespaceId}_${candidate.@name}.html">${candidate.@name}</a></td>
                        </tr>
                        [#break/]
                    [/#if]
                [/#list]
            [/#if]
            <tr>
                <th>Namespace</th>
                <td>[#if schema.@namespace?length > 0]${schema.@namespace}[#else](default namespace)[/#if]</td>
            </tr>
            [#if attributeExists(schema.@file)]
                <tr>
                    <th>XML Schema</th>
                    <td><a href="${schema.@file}">${schema.@file}</a></td>
                </tr>
            [/#if]
            [#assign versionTags = getTagValues(type, "version") /]
            [#if versionTags?size > 0]
                <tr>
                    <th>Version</th>
                    <td>${versionTags[0]}</td>
                </tr>
            [/#if]
        </table>
            [#list type.documentation as documentation]
            <p>${documentation}</p>
            [/#list]

            [#if type.values.item?size > 0]
                [#list type.values.item as value]
                    [#if value_index = 0]
                    <h2>Possible Values</h2>
                    <table>
                        <tr>
                            <th>Value</th>
                            <th>Description</th>
                        </tr>
                    [/#if]
                    <tr>
                        <td[#if isDeprecated(value)] class="deprecated"[/#if]>${value.@value}</td>
                        <td>
                            [#list value.documentation as documentation]
            ${documentation}
          [/#list]
                        </td>
                    </tr>
                    [#if !value_has_next]
                    </table>
                    [/#if]
                [/#list]
            [#else]
            <h2>XML</h2>
                [#list type.attributes.attribute as attribute]
                    [#if attribute_index = 0]
                    <h3>Attributes</h3>
                    <table>
                        <tr>
                            <th>Name</th>
                            <th>Type</th>
                            <th>Description</th>
                        </tr>
                    [/#if]
                    <tr>
                        <td>[#if attributeExists(attribute.@name)]${attribute.@name}[/#if]</td>
                        <td>[#if attributeExists(attribute.@typeSchemaId)][#if attributeExists(attribute.@typeName)][#assign typename=attribute.@typeName/][#else][#assign typename="(anonymous)"/][/#if]<a href="${attribute.@typeSchemaId}_${typename}.html">${typename}</a>[#elseif attributeExists(attribute.@typeName)]${attribute.@typeName}[/#if]</td>
                        <td>${attribute}</td>
                    </tr>
                    [#if !attribute_has_next]
                    </table>
                    [/#if]
                [/#list]
                [#list type.elements.element as element]
                    [#if element_index = 0]
                    <h3>Elements</h3>
                    <table>
                        <tr>
                            <th>Name / Type</th>
                            <th>Min/Max Occurs</th>
                            <th>Description</th>
                        </tr>
                    [/#if]
                    <tr>
                        <td>
                            [#list element.choice as choice]
                                [#if choice_index > 0]<br/>or [/#if][#if attributeExists(choice.@schemaId)]<a href="el_${choice.@schemaId}_${choice.@name}.html">${choice.@name}</a>[#else]${choice.@name}[/#if][#if attributeExists(choice.@typeSchemaId)][#if attributeExists(choice.@typeName)][#assign typename=choice.@typeName/][#else][#assign typename="(anonymous)"/][/#if] (<a href="${choice.@typeSchemaId}_${typename}.html">${typename}</a>)[#elseif attributeExists(choice.@typeName)] (${choice.@typeName})[/#if]
                            [/#list]
                        </td>
                        <td>[#if attributeExists(element.@minOccurs)]${element.@minOccurs}[/#if]/[#if attributeExists(element.@maxOccurs)]${element.@maxOccurs}[/#if]</td>
                        <td>
                            [#list element.documentation as documentation]
            ${documentation}
          [/#list]
                        </td>
                    </tr>
                    [#if !element_has_next]
                    </table>
                    [/#if]
                [/#list]
                [#list type.value as value]
                <h3>Value</h3>
                <table>
                    <tr>
                        <th>Type</th>
                        <td>[#if attributeExists(value.@typeSchemaId)][#if attributeExists(value.@typeName)][#assign typename=value.@typeName/][#else][#assign typename="(anonymous)"/][/#if]<a href="${value.@typeSchemaId}_${typename}.html">${typename}</a>[#elseif attributeExists(value.@typeName)]${value.@typeName}[/#if]</td>
                    </tr>
                </table>
                <p>${value}</p>
                [/#list]

                [#if includeExampleJson!true]
                <h2>JSON</h2>
                <table>
                    <tr>
                        <th>Property</th>
                        <th>Type</th>
                        <th>Description</th>
                    </tr>
                    [#list type.attributes.attribute as attribute]
                        <tr>
                            <td>[#if attributeExists(attribute.@jsonName)]${attribute.@jsonName}[/#if]</td>
                            <td>[#if attributeExists(attribute.@typeSchemaId)][#if attributeExists(attribute.@typeName)][#assign typename=attribute.@typeName/][#else][#assign typename="(anonymous)"/][/#if]<a href="${attribute.@typeSchemaId}_${typename}.html">${typename}</a>[#elseif attributeExists(attribute.@typeName)]${attribute.@typeName}[/#if]</td>
                            <td>${attribute}</td>
                        </tr>
                    [/#list]
                    [#list type.elements.element as element]
                        <tr>
                            <td>[#if attributeExists(element.@jsonName)]${element.@jsonName}[/#if]</td>
                            <td>
                                [#list element.choice as choice]
                                    [#if element.@maxOccurs = "unbounded"]array of [/#if][#if choice_index > 0]<br/>or [/#if][#if attributeExists(choice.@schemaId)]<a href="el_${choice.@schemaId}_${choice.@name}.html">${choice.@name}</a>[#else]${choice.@name}[/#if][#if attributeExists(choice.@typeSchemaId)][#if attributeExists(choice.@typeName)][#assign typename=choice.@typeName/][#else][#assign typename="(anonymous)"/][/#if] (<a href="${choice.@typeSchemaId}_${typename}.html">${typename}</a>)[#elseif attributeExists(choice.@typeName)] (${choice.@typeName})[/#if]
                                [/#list]
                            </td>
                            <td>
                                [#list element.documentation as documentation]
            ${documentation}
            [/#list]
                            </td>
                        </tr>
                    [/#list]
                    [#list type.value as value]
                        <tr>
                            <td>[#if attributeExists(value.@jsonName)]${value.@jsonName}[/#if]</td>
                            <td>[#if attributeExists(value.@typeSchemaId)][#if attributeExists(value.@typeName)][#assign typename=value.@typeName/][#else][#assign typename="(anonymous)"/][/#if]<a href="${value.@typeSchemaId}_${typename}.html">${typename}</a>[#elseif attributeExists(value.@typeName)]${value.@typeName}[/#if]</td>
                            <td>${value}</td>
                        </tr>
                    [/#list]
                </table>
                [/#if]
            [/#if]
        [/@boilerplate]
    [/@file]
[/#macro]
[#macro processJsonType type]
    [@file name="json_" + type?parent.@schemaId + "_" + type.@name + ".html" charset="utf-8"]
        [@boilerplate title=type.@name subnav=[{"title" : "Home", "href" : indexPageName}, {"title" : "JSON" , "href" : "#"}, {"title" : type.@name , "href" : "json_" + type?parent.@schemaId + "_" + type.@name + ".html"}] codeblocks=true]
        <h1>${type.@name}</h1>

        <table>
            [#if attributeExists(type?parent.@schemaId)]
                <tr>
                    <th>JSON Schema ID</th>
                    <td>${type?parent.@schemaId}</td>
                </tr>
            [/#if]
            [#if attributeExists(type?parent.@file)]
                <tr>
                    <th>JSON Schema File</th>
                    <td><a href="${type?parent.@file}">${type?parent.@file}</a></td>
                </tr>
            [/#if]
            [#assign sinceTags = getTagValues(type, "since") /]
            [#assign versionTags = getTagValues(type, "version") /]
            [#if versionTags?size > 0]
                <tr>
                    <th>Available Version</th>
                    <td>${versionTags[0]}</td>
                </tr>
            [/#if]
        </table>
            [#list type.documentation as documentation]
            <p>${documentation}</p>
            [/#list]
            [#list type.property as property]
                [#if property_index = 0]
                <h2>Properties</h2>
                <table>
                    <tr>
                        <th>Name</th>
                        <th>Type</th>
                        <th>Description</th>
                    </tr>
                [/#if]
                <tr>
                    <td>[#if attributeExists(property.@name)]${property.@name}[/#if]</td>
                    <td>[#if property.@isList = "true"]List of [/#if][#if attributeExists(property.@typeSchemaId)][#if attributeExists(property.@typeName)][#assign typename=property.@typeName/][#else][#assign typename="(anonymous)"/][/#if]<a href="json_${property.@typeSchemaId}_${typename}.html">${typename}</a>[#elseif attributeExists(property.@typeName)]${property.@typeName}[/#if]</td>
                    <td>[#if attributeExists(property.@documentation)]${property.@documentation}[/#if]</td>
                </tr>
                [#if !property_has_next]
                </table>
                [/#if]
            [/#list]
            [#list type.enumValue as enumValue]
                [#if enumValue_index = 0]
                <h2>Possible Values</h2>
                <table>
                    <tr>
                        <th>Value</th>
                        <th>Description</th>
                    </tr>
                [/#if]
                <tr>
                    <td>[#list enumValue.value as value]${value} [/#list]</td>
                    <td>[#list enumValue.documentation as documentation]${documentation} [/#list]</td>
                </tr>
                [#if !enumValue_has_next]
                </table>
                [/#if]
            [/#list]
        [/@boilerplate]
    [/@file]
[/#macro]
