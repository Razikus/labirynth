#!/bin/bash
cd /sources/
sed -i "s/labiryntserver/$LABSERVERIP/g" /sources/config.xml
sed -i "s/8989/$LABSERVERPORT/g" /sources/config.xml
javac -encoding ISO-8859-1 LabyrinthGame.java
cp -r /sources/* /compiled/
