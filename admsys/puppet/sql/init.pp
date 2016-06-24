class sql{

 package { 'python-software-properties':
     ensure => present,
 }
 
  package { 'galera':
     ensure => present,
 }
 
  package { 'mariadb-galera-server':
     ensure => present,
 }

 file { 'galera.cnf':
     ensure => present,
     path = '/etc/mysql/conf.d/galera.cnf',
     source = [
          "puppet:///modules/sql/galera.cnf",]
 }
 
   exec { 'run_cluster':
    command => 'service mysql start --wsrep-new-cluster',
    path    => '/usr/local/bin/:/bin/',
  }

 service { 'postfix' :
     ensure => running,
     enable => true,
 }
} 
