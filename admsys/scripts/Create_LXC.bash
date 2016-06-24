#!/bin/bash

TEMPLATE_PATH='/var/lib/vz/template/cache/debian-8.4-DeadpineappleEdition_amd64.tar.gz'
SRVDNS='10.42.3.1'
START_MAC='52:4A:5E'
VM_MEMORY='1024'
VM_BRIDGE="vmbr0"

fail()
{
  tput bold
  tput setaf 1
  echo "Something went wrong, stopping."
  tput sgr0
  exit 1
}

error() {   tput setaf 1; echo -n "[ FAIL ] "; tput sgr0; echo "$@"; fail; }
success() { tput setaf 2; echo -n "[  OK  ] "; tput sgr0; echo "$@"; }
info() {    tput setaf 3; echo -n "[ INFO ] "; tput sgr0; echo "$@"; }
header() {  tput setaf 3; echo -n "[ INFO ] "; echo "===== $@ ====="; tput sgr0; }

# Debug
#set -x
clear

if [ $# -ne 2 ] ; then
	error "Please enter : createLXC LXCNAME IP"
fi

header "Retrive all informations"

# Retriving next idvm from Proxmox
info "Retriving next ID for LXC"
Vmid=`pvesh get /cluster/nextid`
if [ $? -eq 0 ]; then
	success "Id for new VM : $Vmid"
elif [ $? -ne 0 ]; then
	error "Next ID can't be retrive"
fi

info "Generate a new MAC adress"
hexchars="0123456789ABCDEF"
end_mac=$( for i in {1..6} ; do echo -n ${hexchars:$(( $RANDOM % 16 )):1} ; done | sed -e 's/\(..\)/:\1/g' )
macadr=$START_MAC+$end_mac
if [ $? -eq 0 ]; then
	success "New MAC adress is : $macadr"
elif [ $? -ne 0 ]; then
	error "A new MAC can't be Generate."
fi

header "LXC creation and starting"

# Create LXC
info "Create a new LXC"
pct create $Vmid $TEMPLATE_PATH \
 -description LXC -rootfs 4 -hostname $1 -memory $VM_MEMORY -nameserver $SRVDNS \
 -net0 name=eth0,hwaddr=$macadr,ip=$2,mask=255.255.254.0,bridge=$VM_BRIDGE \
 -storage local -password
 if [ $? -eq 0 ]; then
 	success "The new LXC VM has be create, (Name : $1 ; VM ID : $Vmid )"
 elif [ $? -ne 0 ]; then
 	error "Can't create a new LXC"
 fi

#Starting LXC
info "Starting the new VM"
pct start $Vmid
if [ $? -eq 0 ]; then
 success "$Vmid is start"
elif [ $? -ne 0 ]; then
 error "$Vmid Can't be start"
fi

#exit
exit 0
