from fabric.operations import get
from fabric.api import run


def setup_luks(device):
    cmd  = 'sudo cryptsetup -y --cipher blowfish luksFormat {}'.format(device)
    run(cmd)

def verify_luks_device(device):
    cmd = 'sudo file -s {0}'.format(device)
    run(cmd)

def open_luks_device(device, storedev):
    cmd = 'sudo cryptsetup luksOpen {} {}'.format(device, storedev)
    run(cmd)

def format_luks_device(storedev):
    run('sudo mkfs.ext4 -m 0 /dev/mapper/{}'.format(storedev))


def mount_luks_device(storedev, mount_point, owner):
    run('sudo mkdir -p {}'.format(mount_point))
    run('sudo mount /dev/mapper/{} {}'.format(storedev, mount_point))
    run('sudo chown {} {}'.format(owner, mount_point))


def unmount_luks_device(storedev, mount_point):
    run('sudo umount {}'.format(mount_point))
    run('sudo cryptsetup luksClose {}'.format(storedev))


def backup_luks_header_device(device):
    run('sudo cryptsetup luksHeaderBackup --header-backup-file /home/ubuntu/luksbackup {}'.format(device))
    run('sudo chown ubuntu /home/ubuntu/luksbackup')
    get('/home/ubuntu/luksbackup', './')
    run('sudo rm /home/ubuntu/luksbackup')

def install_chef_server():
    run('sudo apt-get install ruby')
    run(' wget  https://web-dl.packagecloud.io/chef/stable/packages/ubuntu/trusty/chef-server-core_12.0.1-1_amd64.deb')
    run('sudo dpkg -i /home/ubuntu/chef-server-core_12.0.1-1_amd64.deb')
    run('sudo chef-server-ctl reconfigure')

def uninstall_chef_server():
    run('sudo dpkg  --purge  chef-server-core')


def show_host_type():
    run('uname -s')
