include bind

class dns{

 bind::server::conf { '/etc/named.conf':
   listen_on_addr    => [ 'any' ],
   listen_on_v6_addr => [ 'any' ],
   forwarders        => [ '8.8.8.8', '8.8.4.4' ],
   allow_query       => [ 'localnets' ],
   zones             => {
     'deadpineapple.lan' => [
       'type master',
       'file "deadpineapple.lan"',
     ],
     '1.42.10.in-addr.arpa' => [
       'type master',
       'file "1.42.10.in-addr.arpa"',
     ],
   },
 }

 bind::server::file { [ 'deadpineapple.lan', '1.42.10.in-addr.arpa' ]:
   source_base => 'puppet:///modules/dns/',
 }

 package { 'bind9':
     ensure => present,
 }
} 
