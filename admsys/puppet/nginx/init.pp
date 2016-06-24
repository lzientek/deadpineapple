class nginx{

 package { 'nginx':
     ensure => present,
 }

 file { 'tomcat.conf':
     ensure => present,
     path = '/etc/nginx/conf.d/tomcat.conf',
     source = [
          "puppet:///modules/nginx/tomcat.conf",]
 }
 
 file { 'nginx.conf':
     ensure => present,
     path = '/etc/nginx/nginx.conf',
     source = [
          "puppet:///modules/nginx/nginx.conf",]
 }

 
 file { 'deadpineapple.fr.key':
     ensure => present,
     path = '/etc/nginx/ssl/deadpineapple.fr.key',
     source = [
          "puppet:///modules/nginx/deadpineapple.fr.key",]
 }

 file { 'deadpineapple.fr.cert':
     ensure => present,
     path = '/etc/nginx/ssl/deadpineapple.fr.cert',
     source = [
          "puppet:///modules/nginx/deadpineapple.fr.cert",]
 }

 service { 'nginx' :
     ensure => running,
     enable => true,
 }
} 
