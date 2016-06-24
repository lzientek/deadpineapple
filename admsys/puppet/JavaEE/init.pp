class JavaEE{

 package { 'tomcat7':
     ensure => present,
 }

 file { 'server.xml':
     ensure => present,
     path = '/etc/tomcat7/server.xml',
     source = [
          "puppet:///modules/JavaEE/server.xml",]
 }
 
 file { 'tomcat-users.xml':
     ensure => present,
     path = '/etc/tomcat7/tomcat-users.xml',
     source = [
          "puppet:///modules/JavaEE/tomcat-users.xml",]
 }

 service { 'tomcat7' :
     ensure => running,
     enable => true,
 }
} 
