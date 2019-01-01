#!/bin/bash
set -ex

cd misc
curl -o BuildTools.jar https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar

## 1.13.2
if [ ! -f ../src/integration-test/resources/servers/CraftBukkit-1.13.2.jar ] || [ ! -f ../src/integration-test/resources/servers/Spigot-1.13.2.jar ]; then
    echo Building 1.13.2 CraftBukkit/Spigot servers
    java -Xmx1024M -jar BuildTools.jar --rev 1.13.2
    mv craftbukkit-1.13.2.jar ../src/integration-test/resources/servers/CraftBukkit-1.13.2.jar
    mv spigot-1.13.2.jar ../src/integration-test/resources/servers/Spigot-1.13.2.jar
fi
if [ ! -f ../src/integration-test/resources/servers/Paperclip-1.13.2.jar ]; then
    echo Downloading Paperclip 1.13.2
    curl -Lo ../src/integration-test/resources/servers/Paperclip-1.13.2.jar https://papermc.io/ci/job/Paper-1.13/488/artifact/paperclip-488.jar
fi

## 1.13.1
if [ ! -f ../src/integration-test/resources/servers/CraftBukkit-1.13.1.jar ] || [ ! -f ../src/integration-test/resources/servers/Spigot-1.13.1.jar ]; then
    echo Building 1.13.1 CraftBukkit/Spigot servers
    java -Xmx1024M -jar BuildTools.jar --rev 1.13.1
    mv craftbukkit-1.13.1.jar ../src/integration-test/resources/servers/CraftBukkit-1.13.1.jar
    mv spigot-1.13.1.jar ../src/integration-test/resources/servers/Spigot-1.13.1.jar
fi

## 1.13
if [ ! -f ../src/integration-test/resources/servers/CraftBukkit-1.13.jar ] || [ ! -f ../src/integration-test/resources/servers/Spigot-1.13.jar ]; then
    echo Building 1.13 CraftBukkit/Spigot servers
    java -Xmx1024M -jar BuildTools.jar --rev 1.13
    mv craftbukkit-1.13.jar ../src/integration-test/resources/servers/CraftBukkit-1.13.jar
    mv spigot-1.13.jar ../src/integration-test/resources/servers/Spigot-1.13.jar
fi

## 1.12.2
if [ ! -f ../src/integration-test/resources/servers/CraftBukkit-1.12.2.jar ] || [ ! -f ../src/integration-test/resources/servers/Spigot-1.12.2.jar ]; then
    echo Building 1.12.2 CraftBukkit/Spigot servers
    java -Xmx1024M -jar BuildTools.jar --rev 1.12.2
    mv craftbukkit-1.12.2.jar ../src/integration-test/resources/servers/CraftBukkit-1.12.2.jar
    mv spigot-1.12.2.jar ../src/integration-test/resources/servers/Spigot-1.12.2.jar
fi
if [ ! -f ../src/integration-test/resources/servers/Glowstone-1.12.2.jar ]; then
    echo Downloading Glowstone 1.12.2
    curl -Lo ../src/integration-test/resources/servers/Glowstone-1.12.2.jar https://github.com/GlowstoneMC/Glowstone/releases/download/2018.0.1/glowstone.jar
fi
if [ ! -f ../src/integration-test/resources/servers/Paperclip-1.12.2.jar ]; then
    echo Downloading Paperclip 1.12.2
    curl -Lo ../src/integration-test/resources/servers/Paperclip-1.12.2.jar https://papermc.io/ci/job/Paper/1594/artifact/paperclip-1594.jar
fi
