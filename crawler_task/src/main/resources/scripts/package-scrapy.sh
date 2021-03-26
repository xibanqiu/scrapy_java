#!/bin/bash

if [ -z "$1"  ]
then
 echo '请输入 git version'
 exit 0;
fi

PROJECT_PAHT=/usr/local/maven/neptune-scrapy
CP_PATH=/opt/neptune-scrapy-stocksnap/

cd $PROJECT_PAHT
git fetch --all
git reset --hard origin/$1
git checkout $1

git status
mvn clean package -DskipTests -Pdev -Dspider=stocksnap
#mvn clean package -DskipTests
cp -f $PROJECT_PAHT/target/*.jar $CP_PATH
