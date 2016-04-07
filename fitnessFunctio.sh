#!/bin/bash

## BRANCH_COVERAGE of given tests

sed -i '/includeTestsMatching/d' build.gradle

for var in "$@"
do
    sed -i "/filter/a \ includeTestsMatching \"$var\"" build.gradle
done

gradle clean 2>&1 >/dev/null
gradle test 2>&1 >/dev/null
gradle jacocoTestReport 2>&1 >/dev/null



read coverage <<< $(awk -F"," '{x+=$7;y+=$6}END{print x/y}' ./build/reports/jacoco/test/jacocoTestReport.csv )

#NAME=$(echo $@ | tr -d ' ') 
echo $coverage > sol.txt





