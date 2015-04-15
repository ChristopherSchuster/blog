#!/bin/bash 
# Random Word Generator 
for (( i=1; i <= $1; i++ ))
do
  line=$(shuf -n 15 $FILE /usr/share/dict/words)
  l2=$line$'\n'
  echo $l2 | sed 's/ /,/g'
done
