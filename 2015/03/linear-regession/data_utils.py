from decimal import *


import json
import csv

def total_by_period(jf):
    """
    Process dedicated internet statistics in Colombia from json file downloaded from
    http://estrategiaticolombia.co/estadisticas/opendata/dat_dedicado_general.json
    And generate total  by period
    """
    i = 0
    data_dict = {}

    def proccess_record(line):
        """
        Process every line inside file in order to get a consolidate
        """
        j_line = json.loads(line)[0]
        period_key = '{}-{}'.format(j_line['ANHO'], j_line['PERIODO'])
        j_line['SUSCRIPTORES'] = Decimal(j_line['SUSCRIPTORES'])
        if period_key not in data_dict:
            record = {
                    'total': j_line['SUSCRIPTORES']
            }
            data_dict[period_key]  = record

        else:
            val = j_line['SUSCRIPTORES']
            data_dict[period_key]['total'] += val

    with open(jf, 'r') as f:
        for line in f:
            i +=1

            try:
                proccess_record(line)
            except Exception as e:
                print e
                print 'error processing line {}'.format(i)

            if i > 10000:
                f.close()
                break

    # We must construct an array in format (x, y) where by every period y we have
    # total by period y indexes must be numbers so we get the y axis and
    # create a list of indexes, every item in this list represent a period
    # one period represents 3 months
    period_list = []
    keys = data_dict.keys()
    keys.sort()
    period_index = 0
    with open('total_by_period.csv', 'w') as csvfile:
        spamwriter = csv.writer(csvfile, delimiter=',')
        #every period represents 3 monts
        for n_index in range(len(keys)):
            val = data_dict[keys[n_index]]
            row = (period_index, str(val['total']))
            spamwriter.writerow(row)
            period_index += 3




def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--task', default='total_by_period')
    parser.add_argument('--file', default='./dat_dedicado_general.json')
    args = parser.parse_args()

    task_dict = {
        'total_by_period': total_by_period,
    }

    task_dict[args.task](args.file)

if __name__ == "__main__":
    main()
