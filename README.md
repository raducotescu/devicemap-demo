# Apache Sling DeviceMap demo bundle #
This project provides a demo bundle that includes Apache DeviceMap (the ddr module) for server-side client detection.

### How To ###
* Clone Apache Sling

        git clone https://github.com/apache/sling.git
* Switch to the `trunk` branch

        cd sling/
        git checkout trunk
* Build Sling

        mvn clean package -DskipTests
* Launch Sling

        cd launchpad/builder/target/
        java -jar org.apache.sling.launchpad-7-SNAPSHOT-standalone.jar

* In another terminal instal the W3C Simple DDR API in your Maven repository

        wget http://www.w3.org/TR/2008/WD-DDR-Simple-API-20080404/DDR-Simple-API.jar
        mvn install:install-file -Dfile=DDR-Simple-API.jar -DgroupId=org.w3c.ddr.simple -DartifactId=DDR-Simple-API -Dversion=2008-04-04 -Dpackaging=jar

* Checkout the Apache DeviceMap project and build the OpenDDR module

        svn co http://svn.apache.org/repos/asf/incubator/devicemap/trunk/ devicemap
        cd devicemap/openddr/java
        mvn clean install

* Clone this project and install the bundle in your running Sling instance

        git clone https://github.com/raducotescu/devicemap-demo.git
        cd devicemap-demo/
        mvn clean package sling:install

* Go to [http://localhost:8080/content/demo/index.html](http://localhost:8080/content/demo/index.html) with various devices

