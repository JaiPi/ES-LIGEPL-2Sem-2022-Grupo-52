# ES-LIGEPL-2Sem-2022-Grupo-52
Projecto de Engenharia de Software 2022 - Grupo 52

JFreeChart
==========

CONTRIBUTORS
------------
André Branco
Gonçalo Duarte
Jaime Pignatelli
João Faustino
Ricardo
Tomás Duarte



Version 2.0.0, not yet released.

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.jfree/jfreechart/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.jfree/jfreechart)

Overview
--------
JFreeChart is a comprehensive free chart library for the Java(tm) platform that 
can be used on the client-side (JavaFX and Swing) or the server side (with
export to multiple formats including SVG, PNG and PDF).

![JFreeChart sample](http://jfree.org/jfreechart/images/coffee_prices.png)

The home page for the project is:

http://www.jfree.org/jfreechart

JFreeChart requires JDK 11 or later (check the 1.5.x branch for Java 8 support).  
If JavaFX support is required, you need to also include the JFreeChart-FX extensions:

https://github.com/jfree/jfreechart-fx

The library is licensed under the terms of the GNU Lesser General Public 
License (LGPL) version 2.1 or later.


Using JFreeChart
----------------
To use JFreeChart in your projects, add the following dependency to your build tool:

    <dependency>
        <groupId>org.jfree</groupId>
        <artifactId>jfreechart</artifactId>
        <version>1.5.3</version>
    </dependency>


Building JFreeChart
-------------------
You can build JFreeChart using Maven by issuing the following command from the root directory of the project:

    mvn clean install

The build requires JDK 11 or later.


Demos
-----
A small set of demo applications can be found in the following projects here
at GitHub:

* [JFree-Demos](https://github.com/jfree/jfree-demos "JFree-Demos Project Page at GitHub")
* [JFree-FXDemos](https://github.com/jfree/jfree-fxdemos "JFree-FXDemos Project Page at GitHub")
