<!doctype html>
<html>
<head>
    <meta content="IE=edge" http-equiv="X-UA-Compatible">
    <meta charset="utf-8">
    <meta content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" name="viewport">
    <!-- Use title if it's in the page YAML frontmatter -->
    <title>$page.title$$if(!page.home.active)$ · $page.home.title$$endif$</title>
    <link rel="shortcut icon"  href="http://akka.io/resources/favicon.ico">
    <link href="$page.base$css/site.css" rel="stylesheet" />
    <script src="$page.base$js/all.js"></script>
</head>

<body class="documentationActors">
$pageHeader()$
<section class="wrapper">
    <div class="row">
        <div class="contentsContainer">
            <div class="box">
                $page.content$
                $page.navigation$

               <!--  <h1>Introduction</h1>

                <span class="underLine"></span>
                <ul>
                    <li><a href="">What is Akka?</a></li>
                    <li><a href="">Why Akka?</a></li>
                    <li><a href="">Getting Started</a></li>
                    <li><a href="">The Obligatory Hello World</a></li>
                    <li><a href="">Use-case and Deployment Scenarios</a></li>
                    <li><a href="">Examples of use-cases for Akka</a></li>
                </ul>
            </div>
            <div class="box">
                <h1>General</h1>
                <span class="underLine"></span>
                <ul>
                    <li><a href="">Terminology, Concepts</a></li>
                    <li><a href="">Actor Systems</a></li>
                    <li><a href="">What is an Actor?</a></li>
                    <li><a href="">Supervision and Monitoring</a></li>
                    <li><a href="">Actor References, Paths and Addresses</a></li>
                    <li><a href="">Location Transparency</a></li>
                    <li><a href="">Akka and the Java Memory Model</a></li>
                    <li><a href="">Message Delivery Reliability</a></li>
                    <li><a href="">Configuration</a></li>
                </ul>
            </div>
            <div class="box">
                <h1>Actors</h1>
                <span class="underLine"></span>
                <ul>
                    <li><a href="documentationActors.html">Actors</a></li>
                    <li><a href="documentationActorsOld.html">Actors 1.3.1</a></li>
                    <li><a href="">Typed Actors</a></li>
                    <li><a href="">Fault Tolerance</a></li>
                    <li><a href="">Dispatchers</a></li>
                    <li><a href="">Mailboxes</a></li>
                    <li><a href="">Routing</a></li>
                    <li><a href="">Building Finite State Machine Actors</a></li>
                    <li><a href="">Persistence</a></li>
                    <li><a href="">Persistence - Schema Evolution</a></li>
                    <li><a href="">Persistence Query</a></li>
                    <li><a href="">Persistence Query for LevelDB</a></li>
                    <li><a href="">Testing Actor Systems</a></li>
                </ul>
            </div>
            <div class="box">
                <h1>Actors (Java with Lambda Support)</h1>
                <span class="underLine"></span>
                <ul>
                    <li><a href="">Actors (Java with Lambda Support)</a></li>
                    <li><a href="">Fault Tolerance (Java with Lambda Support)</a></li>
                    <li><a href="">FSM (Java with Lambda Support)</a></li>
                    <li><a href="">Persistence (Java with Lambda Support)</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="contentsContainer">
            <div class="box">
                <h1>Futures and Agents</h1>
                <span class="underLine"></span>
                <ul>
                    <li><a href="">Futures</a></li>
                    <li><a href="">Agents</a></li>
                </ul>
            </div>
            <div class="box">
                <h1>Networking</h1>
                <span class="underLine"></span>
                <ul>
                    <li><a href="">Cluster Specification</a></li>
                    <li><a href="">Cluster Usage</a></li>
                    <li><a href="">Cluster Singleton</a></li>
                    <li><a href="">Distributed Publish Subscribe in Cluster</a></li>
                    <li><a href="">Cluster Client</a></li>
                    <li><a href="">Cluster Sharding</a></li>
                    <li><a href="">Cluster Metrics Extension</a></li>
                    <li><a href="">Distributed Data</a></li>
                    <li><a href="">Remoting</a></li>
                    <li><a href="">Remoting (codename Artery)</a></li>
                    <li><a href="">Serialization</a></li>
                    <li><a href="">I/O</a></li>
                    <li><a href="">Using TCP</a></li>
                    <li><a href="">Using UDP</a></li>
                    <li><a href="">Camel</a></li>
                </ul>
            </div>
            <div class="box">
                <h1>Utilities</h1>
                <span class="underLine"></span>
                <ul>
                    <li><a href="">Event Bus</a></li>
                    <li><a href="">Logging</a></li>
                    <li><a href="">Scheduler</a></li>
                    <li><a href="">Duration</a></li>
                    <li><a href="">Circuit Breaker</a></li>
                    <li><a href="">Akka Extensions</a></li>
                    <li><a href="">Use-case and Deployment Scenarios</a></li>
                </ul>
            </div>
            <div class="box">
                <h1>Streams</h1>
                <span class="underLine"></span>
                <ul>
                    <li><a href="">Introduction</a></li>
                    <li><a href="">Quick Start Guide</a></li>
                    <li><a href="">Reactive Tweets</a></li>
                    <li><a href="">Design Principles behind Akka Streams</a></li>
                    <li><a href="">Basics and working with Flows</a></li>
                    <li><a href="">Working with Graphs</a></li>
                    <li><a href="">Modularity, Composition and Hierarchy</a></li>
                    <li><a href="">Buffers and working with rate</a></li>
                    <li><a href="">Dynamic stream handling</a></li>
                    <li><a href="">Custom stream processing</a></li>
                    <li><a href="">Integration</a></li>
                    <li><a href="">Error Handling</a></li>
                    <li><a href="">Working with streaming IO</a></li>
                    <li><a href="">Pipelining and Parallelism</a></li>
                    <li><a href="">Testing streams</a></li>
                    <li><a href="">Overview of built-in stages and their semantics</a></li>
                    <li><a href="">Streams Cookbook</a></li>
                    <li><a href="">Configuration</a></li>
                    <li><a href="">Migration Guide 1.0 to 2.x</a></li>
                    <li><a href="">Migration Guide 2.0.x to 2.4.x</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="contentsContainer">
            <div class="box">
                <h1>HowTo: Common Patterns</h1>
                <span class="underLine"></span>
                <ul>
                    <li><a href="">Scheduling Periodic Messages</a></li>
                    <li><a href="">Single-Use Actor Trees with High-Level Error Reporting</a></li>
                </ul>
            </div>
            <div class="box">
                <h1>Experimental Modules</h1>
                <span class="underLine"></span>
                <ul>
                    <li><a href="">Multi Node Testing</a></li>
                    <li><a href="">Actors (Java with Lambda Support)</a></li>
                    <li><a href="">FSM (Java with Lambda Support)</a></li>
                    <li><a href="">Persistence Query</a></li>
                    <li><a href="">External Contributions</a></li>
                </ul>
            </div>
            <div class="box">
                <h1>Information for Akka Developers</h1>
                <span class="underLine"></span>
                <ul>
                    <li><a href="">Building Akka</a></li>
                    <li><a href="">Multi JVM Testing</a></li>
                    <li><a href="">I/O Layer Design</a></li>
                    <li><a href="">Developer Guidelines</a></li>
                    <li><a href="">Documentation Guidelines</a></li>
                </ul>
            </div>
            <div class="box">
                <h1>Project Information</h1>
                <span class="underLine"></span>
                <ul>
                    <li><a href="">Migration Guides</a></li>
                    <li><a href="">Issue Tracking</a></li>
                    <li><a href="">Licenses</a></li>
                    <li><a href="">Sponsors</a></li>
                    <li><a href="">Project</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="contentsContainer">
            <div class="box">
                <h1>Additional Information</h1>
                <span class="underLine"></span>
                <ul>
                    <li><a href="">Binary Compatibility Rules</a></li>
                    <li><a href="">Frequently Asked Questions</a></li>
                    <li><a href="">Books</a></li>
                    <li><a href="">Videos</a></li>
                    <li><a href="">Akka in OSGi</a></li>
                </ul>
    -->
            </div>
        </div>
    </div>
</section>

$pageFooter()$
$jsImports()$

</body>
</html>