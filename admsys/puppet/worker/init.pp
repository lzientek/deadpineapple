include apt

class worker{
 include apt::backports

 apt::source { 'debian_backports':
     location => 'http://ftp.fr.debian.org/debian',
     release => 'jessie-backports',
     repos => 'main',
     include_src => false,
 }

 package { 'ffmpeg':
     ensure => present,
     require => apt::source['debian_backports']
 }
} 
