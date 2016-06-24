class ssh{

 package { 'ssh':
     ensure => present,
 }

 file { 'server.xml':
     ensure => present,
     path = '/etc/tomcat7/server.xml',
     source = [
          "puppet:///modules/JavaEE/server.xml",]
 }

 service { 'ssh' :
     ensure => running,
     enable => true,
 }
} 
