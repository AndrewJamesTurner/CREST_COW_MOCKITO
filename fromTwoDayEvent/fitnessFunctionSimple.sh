#!/bin/bash

## BRANCH_COVERAGE of given tests
export JAVA_HOME=/usr/lib/jvm/jre-1.8.0-openjdk-1.8.0.71-1.b15.el6_7.x86_64/
export GRADLE_HOME=/home/menendez/gradle-2.12/bin
export MOCKITO_HOME=/home/menendez/mockito
export CURRENT_FOLDER=`pwd`

cd $MOCKITO_HOME
sed -i '/includeTestsMatching/d' build.gradle

for var in "$@"
do
    sed -i "/filter/a \ includeTestsMatching \"$var\"" build.gradle
done

rm -rf ./build/jacoco

$GRADLE_HOME/gradle test 2>&1 >/dev/null
$GRADLE_HOME/gradle jacocoTestReport 2>&1 >/dev/null



read coverage <<< $(awk -F"," '{x+=$7;y+=$6}END{print x/(x+y)}' ./build/reports/jacoco/test/jacocoTestReport.csv )

#NAME=$(echo $@ | tr -d ' ') 
echo $coverage > $CURRENT_FOLDER/sol.txt
echo $coverage





