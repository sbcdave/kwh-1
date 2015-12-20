# kwh
Datalogger tool box to retrieve microgrid data from remote sites as part of KiloWatt for Humanity projets. The goal of this project is to create turnkey software package that can be used to retrieve incoming metric measures from microgrid projects designed and implemented by KiloWatt for Humanity in off-grid locations. More information on current projects at kw4h.org.

## Graphite Installation
#### Install Dependencies<br />
run as root "graphite-install.sh"<br />

#### Configure carbon.conf<br />
`cd /opt/graphite/conf`<br />
`sudo cp carbon.conf.example carbon.conf` //keep default settings<br />

#### Configure storage-schemas.conf<br />
`cd /opt/graphite/conf`<br />
`sudo cp storage-schemas.conf.example storage-schemas.conf`<br />
Open "storage-schemas.conf" and remove section [default_1min_for_1day]. Instead, insert the following section:
[kwh]<br />
pattern = .*<br />
retentions = 60s:5y<br />
Note that "kwh" is just a label and has no specific meaning. The "kwh" section is configured to store data every 60 sec for 5 years.

#### Configure Graphite database
`cd /opt/graphite/webapp/graphite/`<br />
`sudo python manage.py syncdb`<br />
You will be prompted to create a superuser account. Type "yes" to continue and then follow the prompts.<br />
Copy the local settings example file to the production version while you are in this directory:<br />
`sudo cp local_settings.py.example local_settings.py`<br />

#### Configure Apache
sudo cp /opt/graphite/examples/example-graphite-vhost.conf /etc/apache2/sites-available/default<br />
sudo cp /opt/graphite/conf/graphite.wsgi.example /opt/graphite/conf/graphite.wsgi<br />
sudo chown -R www-data:www-data /opt/graphite/storage<br />
sudo mkdir -p /etc/httpd/wsgi<br />
edit the Apache configuration file we copied earlier:
`sudo vi /etc/apache2/sites-available/default`<br />
`...`
`...`
`WSGISocketPrefix /etc/httpd/wsgi`

`<VirtualHost *:80>`
	`ServerName Your.Domain.Name.Here`
	`...`
	`...`

Modify ports.conf and update the IP address to listen

#### Install Grafana
$ wget https://grafanarel.s3.amazonaws.com/builds/grafana_2.0.2_amd64.deb
$ sudo apt-get install -y adduser libfontconfig
$ sudo dpkg -i grafana_2.0.2_amd64.deb
$ sudo apt-get update
$ sudo apt-get install grafana

#### Upgrade Grafana
Add the following line to your /etc/apt/sources.list file.<br />
`deb https://packagecloud.io/grafana/stable/debian/ wheezy main`<br />

Then to upgrade Grafana whenever a new version has been released, follow the following steps:<br />
`curl https://packagecloud.io/gpg.key | sudo apt-key add -`<br />
`sudo apt-get update`<br />
`sudo apt-get install grafana`<br />
