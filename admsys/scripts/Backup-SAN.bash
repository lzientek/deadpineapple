#!/bin/bash

LOGS='/var/log/S3-Backup.log'

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

rm -rf /var/log/S3-Backup.log

rm -rf /tmp/storage_save.tar.gz >> /var/log/S3-Backup.log

tar zcvf /tmp/storage_save.tar.gz /mnt/storage1 >> /var/log/S3-Backup.log

s3cmd put /tmp/storage_save.tar.gz s3://SaveBucket/`date +%d%m%y`.tar.gz >> /var/log/S3-Backup.log

if [ $? -eq 0 ]; then
	success "Backupfinish `date +%d%m%y`"
elif [ $? -ne 0 ]; then
	error "BackupFailed please check log : $LOGS"
fi
