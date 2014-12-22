import boto
import boto.ec2

import argparse


def attach_volume(
        region='us-west-2', volume_id=None, instance_id=None,
        device_name='sdb'):

    ec2 = boto.ec2.connect_to_region(region)
    volume = ec2.get_all_volumes(volume_ids=volume_id)[0]
    volume.attach(instance_id, device_name)
    return volume

def main():
    parser = argparse.ArgumentParser()

    parser.add_argument('--region',  default='us-west-2')
    parser.add_argument('--volume_id',  required=True)
    parser.add_argument('--instance_id',  required=True)
    parser.add_argument('--device_name',  default='sdb')

    args = parser.parse_args()
    v = attach_volume(
        region=args.region, volume_id=args.volume_id,
        device_name=args.device_name, instance_id=args.instance_id)
    print(v)

if __name__ == '__main__':
    main()
