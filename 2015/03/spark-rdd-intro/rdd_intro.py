from pyspark import SparkContext, StorageLevel
from math import sqrt

sc = SparkContext("local", "Rdd intro")

def is_prime(n):
    if n in (1, 2 ,3):
        return True

    if n == 4:
        data = [2]
    else:
        data = [x for x in xrange(2, n/2)]

    n = n * 1.0
    for d in data:
        if n % d == 0.0:
            return False
    return True

def get_fibonacci(n):
    return ((1+sqrt(5))**n-(1-sqrt(5))**n)/(2**n*sqrt(5))


def main():
    data = [x for x in xrange(1, 100)]
    distData = sc.parallelize(data)
    print 'first element %s \n' % distData.first()
    print 'numer of elements %s \n' % distData.count()
    primeData = distData.filter(is_prime)
    print 'number of prime elements %s ' % primeData.count()
    primeData.persist(StorageLevel.MEMORY_ONLY)
    fibonacciData = primeData.map(get_fibonacci)
    print fibonacciData.collect()

if __name__ == "__main__":
    main()
