import boto
import boto.ec2

import argparse


def get_or_create_security_group(region ,group_name):
    """
    Search by group_name, if doesn't exits, it is created
    """
    try:
        ec2 = boto.ec2.connect_to_region(region)
        group = ec2.get_all_security_groups(groupnames=[group_name])[0]
    except ec2.ResponseError, e:
        if e.code == 'InvalidGroup.NotFound':
            group = ec2.create_security_group(
                group_name, 'group {} for region {}'.format(group_name, region))
        else:
            raise
    return group


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--name', required=True)
    parser.add_argument('--region',  default='us-west-2')
    args = parser.parse_args()
    print (get_or_create_security_group(args.region, args.name))


if __name__ == '__main__':
    main()
