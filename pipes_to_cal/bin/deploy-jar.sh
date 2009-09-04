#! /bin/sh
mvn deploy:deploy-file -Durl=file://`pwd`/vendor/lib -DrepositoryId=basedir -Dfile=$1.jar -DgroupId=$2 -DartifactId=$1 -Dversion=$3 -Dpackaging=jar

