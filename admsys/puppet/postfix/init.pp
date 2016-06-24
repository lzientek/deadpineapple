class postfix{

 package { 'postfix':
     ensure => present,
 }

 file { 'main.cf':
     ensure => present,
     path = '/etc/postfix/main.cf',
     source = [
          "puppet:///modules/postfix/main.cf",]
 }

 service { 'postfix' :
     ensure => running,
     enable => true,
 }
} 
