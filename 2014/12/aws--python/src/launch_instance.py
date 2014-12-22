import boto
import boto.ec2

import argparse
import time

from create_security_group import get_or_create_security_group
from create_key_pair import get_or_create_key_pair

def launch_instance(
        ami=None, instance_type='t1.micro', key_name=None, key_dir='~/.ssh',
        group_name=None, ssh_port=22, cidr_ip=None, tag=None,
        region='us-west-2'):
    """
    Launch an instance and wait for it to start running.
    """
    group = get_or_create_security_group(region, group_name)
    key = get_or_create_key_pair(key_name=key_name, key_dir=key_dir, region=region)

    # Add a rule to the security group to authorize SSH traffic
    # on the specified port.
    ec2 = boto.ec2.connect_to_region(region)
    try:
        group.authorize('tcp', ssh_port, ssh_port, cidr_ip)
    except ec2.ResponseError, e:
        if e.code == 'InvalidPermission.Duplicate':
            print 'Security Group: %s already authorized' % group_name
        else:
            raise

    # http://docs.pythonboto.org/en/latest/ref/ec2.html?highlight=run_instances#boto.ec2.connection.EC2Connection.run_instances
    reservation = ec2.run_instances(ami, key_name=key_name,
                                     security_groups=[group_name],
                                     instance_type=instance_type)


    # Find the actual Instance object inside the Reservation object
    # returned by EC2.
    instance = reservation.instances[0]

    # The instance has been launched but it's not yet up and
    # running. Let's wait for its state to change to 'running'.
    print 'waiting for instance'
    while instance.state != 'running':
        print ('.')
        time.sleep(5)
        instance.update()
    print ('done')

    # label the instance
    instance.add_tag(tag)

    return instance


def main():
    parser = argparse.ArgumentParser()

    parser.add_argument('--ami', required=True)
    parser.add_argument('--instance_type', required=False, default='t1.micro')
    parser.add_argument('--key_name', required=True)
    parser.add_argument('--key_dir', required=False, default='~/.ssh')
    parser.add_argument('--tag', required=True)
    parser.add_argument('--group_name', required=True)
    parser.add_argument('--ssh_port', type=int, required=False, default=22)
    parser.add_argument('--cidr_ip', required=True)
    parser.add_argument('--region',  default='us-west-2')
    args = parser.parse_args()

    instance = launch_instance(
        ami=args.ami, instance_type=args.instance_type,
        key_name=args.key_name, key_dir=args.key_dir,
        group_name=args.group_name,
        ssh_port=args.ssh_port, cidr_ip=args.cidr_ip,
        tag=args.tag, region=args.region)

    print(instance)

if __name__ == '__main__':
    main()
