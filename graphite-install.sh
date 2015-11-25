################################################################
### Script to install Graphite and its dependencies on Linux ###
################################################################
#
#
# Description: This script aims at installing Graphite and its
#              dependencies to default location
#
# History:
# 5/8/2015 - Created. [Matthieu Bach]
#
#
################################################################
#
########################
# Install dependencies #
########################
#
# Update and Upgrade packages
#
sudo apt-get update && sudo apt-get -y upgrade

#
# Install needed packages
#
sudo apt-get install --assume-yes apache2 apache2-mpm-worker apache2-utils apache2.2-bin apache2.2-common libapr1 libaprutil1 libaprutil1-dbd-sqlite3 build-essential python3.2 python-dev libpython3.2 python3-minimal libapache2-mod-wsgi libaprutil1-ldap memcached python-cairo-dev python-django python-ldap python-memcache python-pysqlite2 sqlite3 erlang-os-mon erlang-snmp rabbitmq-server bzr expect libapache2-mod-python python-setuptools

#
# Install few more python components
#
sudo easy_install django-tagging zope.interface twisted txamqp

# Install the python pip command as it will be used to install Graphite
sudo apt-get install --assume-yes python-pip

# Install Whisper, the Graphite database with the pip command
sudo pip install whisper

# Install Carbon
sudo pip install carbon

# Install Graphite web-app
sudo pip install graphite-web
