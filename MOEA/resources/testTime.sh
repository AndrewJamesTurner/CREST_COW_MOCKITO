#!/bin/bash

x=$(sed -n '44p' $@)

echo $x | sed -e 's/[^0-9. ]*//g' -e 's/ \+//g' 

 
