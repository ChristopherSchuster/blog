import boto
import boto.ec2

import argparse


def revoque_from_security_group(
        ip_protocol='tcp', region='us-west-2', name=None, from_port=None,
        to_port=None, cidr_ip=None):
    """
    Authorize cidr_ip access to a certain security group
    """
    ec2 = boto.ec2.connect_to_region(region)
    group = ec2.get_all_security_groups(groupnames=[name])[0]
    return group.authorize(ip_protocol=ip_protocol, from_port=from_port, to_port=to_port, cidr_ip=cidr_ip)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--name', required=True)
    parser.add_argument('--region',  default='us-west-2')
    parser.add_argument('--ip_protocol', default='tcp')
    parser.add_argument('--from_port', required=True)
    parser.add_argument('--to_port', required=True)
    parser.add_argument('--cidr_ip', required=True)
    args = parser.parse_args()
    print (revoque_from_security_group(
        ip_protocol=args.ip_protocol, region=args.region, name=args.name,
        from_port=args.from_port, to_port=args.to_port, cidr_ip=args.cidr_ip))


if __name__ == '__main__':
    main()
