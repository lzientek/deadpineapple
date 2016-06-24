node 'SRVSQL*'{
 include sql
 include ssh
}

node 'SRVDNS*' {
 include dns
 includes ssh
}

node 'WEBTOM*' {
 include JavaEE
 include ssh
}

node 'WORKER*' {
 include worker
 include ssh
}

node 'WEBLB' {
 include nginx
 include ssh
}

node 'SRVMAIL' {
 include postfix
 include ssh
}

node 'SRVMQ' {
 include rabbitMQ
 include ssh
}
