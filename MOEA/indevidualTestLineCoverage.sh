#!/bin/bash

filename='resources/mockitousagePackageSubsetTests.csv'
filelines=`cat $filename`


for line in $filelines ; do
    
	echo $line

	./ff.sh $line

	mkdir tmp/$line
	cp -r ../mockito-2.0.44-beta/build/reports/jacoco/test/html/* tmp/$line

done
