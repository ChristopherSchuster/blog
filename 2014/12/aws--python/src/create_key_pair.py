import boto
import boto.ec2

import argparse


def get_or_create_key_pair(key_name=None, key_dir='~/.ssh', region='us-west-2'):
    try:
        ec2 = boto.ec2.connect_to_region(region)
        key = ec2.get_all_key_pairs(keynames=[key_name])
        print key
    except ec2.ResponseError, e:
        print e
        if e.code == 'InvalidKeyPair.NotFound':
            # Create an SSH key to use when logging into instances.
            key =  ec2.create_key_pair(key_name)
            if not key.save(key_dir):
                print('Key could not be created\n')
                raise
        else:
            raise
    return key


def main():
    parser = argparse.ArgumentParser()

    parser.add_argument('--key_name', required=True)
    parser.add_argument('--key_dir', required=False)
    parser.add_argument('--region',  default='us-west-2')
    args = parser.parse_args()

    key = get_or_create_key_pair(
        key_name=args.key_name, key_dir=args.key_dir, region=args.region)

    print(key)

if __name__ == '__main__':
    main()
