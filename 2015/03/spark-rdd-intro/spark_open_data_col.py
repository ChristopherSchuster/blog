from operator import add
from pyspark import SparkContext, StorageLevel
from sets import Set
import json
import os



sc = SparkContext("local", "Open Data Colombia")


def to_json(line):
    try:
        j = json.loads(line)
        return j
    except Exception as e:
        return {'error': line}

def extract_all_keys(structure):
    if not structure:
        return []
    key_set = []
    def traverse(k_set, st):
        for key, value in st.iteritems():
            k_set.append(key)
            if dict == type(value):
                traverse(k_set, value)
            elif list == type(value):
                for v in value:
                    traverse(k_set, v)
        return k_set
    return traverse(key_set, structure)

def merge_keys(l1, l2):
    return l1 + l2

def process_files(data_dir):
    """
    Step 1. Load json files from location directory.
    Step 2. Aply mapping function to extract all keys by file
    Step 3. Count how many times a key exists on all files
    Step 4. Order dataset
    """
    input = sc.textFile(data_dir)
    data = input.map(
        to_json).map(
            extract_all_keys).flatMap(
                lambda x: [(k, 1) for k in x]).reduceByKey(
                    lambda x, y: x + y)

    print 'there are %s unique keys\n' % data.count()

    d2 = data.map(lambda x: (x[1], x[0],)).sortByKey(False)
    print d2.take(100)
    sc.stop()

def main(data_dir):
    process_files(data_dir)

if __name__ == "__main__":
    # directory where opendata catalog is stored
    open_data_dir = '/notempo/opendata/*'
    main(open_data_dir)
