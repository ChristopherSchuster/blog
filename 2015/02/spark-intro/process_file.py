from operator import add
from pyspark import SparkContext

logFile = "/opt/spark-1.2.1-bin-hadoop2.4/README.md"  # Should be some file on your system
sc = SparkContext("local", "Simple App")
logData = sc.textFile(logFile).cache()

# get set of list with odd number of words
odd_words_count = logData.filter(lambda line: len(line.split()) % 2 != 0).count()

#get number of unique words
unique_words = logData.flatMap(lambda line: line.split(' ')).map(lambda x: (x, 1)).reduceByKey(add).collect()

print "there are %s lines with odd number of words\n" % odd_words_count
print "there are %s unique words\n" % len(unique_words)
print "and the unique words are :" 
for w in unique_words:
    print "%s %s times" % w

