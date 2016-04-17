#!/bin/bash


for file in *.html
do
 # do something on "$file"
read time <<< $(./testTime.sh $file)
 echo "$file,$time" >> "testTimes.csv"
done
