import boto
import boto.ec2

import argparse


def create_volume(
        region='us-west-2', zone='us-west-2a', size_gb=None, snapshot=None,
        volume_type='standard', iops=None, dry_run=False):

    ec2 = boto.ec2.connect_to_region(region)
    v = ec2.create_volume(
        size_gb, zone, snapshot=snapshot, volume_type=volume_type, iops=iops,
        dry_run=dry_run)
    return v


def main():
    parser = argparse.ArgumentParser()

    parser.add_argument('--size_gb', type=int,required=True)
    parser.add_argument('--region',  default='us-west-2')
    parser.add_argument('--zone', default='us-west-2a')
    parser.add_argument('--volume_type',  default='standard')
    parser.add_argument('--snapshot')
    args = parser.parse_args()
    v = create_volume(
        region=args.region, zone=args.zone, size_gb=args.size_gb,
        snapshot=args.snapshot, volume_type=args.volume_type)
    print(v)

if __name__ == '__main__':
    main()
