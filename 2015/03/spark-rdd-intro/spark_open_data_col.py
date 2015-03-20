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
    input = sc.textFile(data_dir)
    data = input.map(
        to_json).map(
            extract_all_keys).flatMap(
                lambda x: [(k, 1) for k in x]).reduceByKey(
                    lambda x, y: x + y)
    data.persist(StorageLevel.MEMORY_AND_DISK)
    data.map(lambda x: json.dumps(x)).saveAsTextFile('./keys.json')
    # data.map(
    #     extract_all_keys).flatMap(
    #         lambda x: [(k, 1) for k in x]).reduceByKey(
    #             lambda x, y: x + y).map(
    #                 lambda x: json.dumps(x)).saveAsTextFile('./keys.json')
    #keys_rdd.persist(StorageLevel.MEMORY_AND_DISK)
    #keys_rdd.map(lambda x: json.dumps(x)).saveAsTextFile('./keys.json')
    #r = data.map(extract_all_keys).reduce(merge_keys)
    #r = data.map(extract_all_keys).collect()
    #map(lambda x: json.dumps(x)).saveAsTextFile('./keys.json')
    #r = keys_rdd.collect()

    #print 'there are %s keys, some of them can be duplicated \n' % len(r)
    #print r[0]
    #print type(r)
    #result_keys_rdd = sc.parallelize(r)
    #print result_keys_rdd.first()
    sc.stop()

def main(data_dir):
    process_files(data_dir)

if __name__ == "__main__":
    # directory where opendata catalog is stored
    open_data_dir = '/notempo/opendata/*'
    main(open_data_dir)
