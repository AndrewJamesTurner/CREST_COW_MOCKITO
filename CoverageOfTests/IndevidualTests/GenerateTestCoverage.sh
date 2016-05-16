#!/bin/bash

filenames='../SolutionsInPaper/*'


for f in $filenames ; do

	#tests='cat config.txt'

	#tests=$(<$f)
	#tests=`cat $f`

	#echo $tests

	python mockread.py (cat $f)

	mv out.csv $f

	

done


