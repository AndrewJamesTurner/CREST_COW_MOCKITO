#!/bin/bash

## BRANCH_COVERAGE of given tests

sed -i '/includeTestsMatching/d' build.gradle

for var in "$@"
do

	#read trimmed <<< $(echo "$var" | tr -d "'")
    sed -i "/filter/a \ includeTestsMatching '$var'" build.gradle
done

rm -rf ./build/jacoco

gradle test 2>&1 >/dev/null
gradle jacocoTestReport 2>&1 >/dev/null

read coverage <<< $(awk -F"," '{x+=$7;y+=$6}END{print x/(x+y)}' ./build/reports/jacoco/test/jacocoTestReport.csv )


x=$(sed -n '41p' ./build/reports/tests/index.html)
read time <<< $(echo $x | sed -e 's/[^0-9. ]*//g' -e 's/ \+//g' )

echo "$coverage, $time"



