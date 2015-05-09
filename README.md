# kwh
Datalogger tool box to retrieve microgrid data from remote sites as part of KiloWatt for Humanity projets. The goal of this project is to create turnkey software package that can be used to retrieve incoming metric measures from microgrid projects designed and implemented by KiloWatt for Humanity in off-grid locations. More information on current projects at kw4h.org.

## Graphite Installation
#### Install Dependencies<br />
run as root "graphite-install.sh"<br />

#### Configure carbon.conf<br />
cd /opt/graphite/conf<br />
sudo cp carbon.conf.example carbon.conf //keep default settings<br />
<br />
#### Configure storage-schemas.conf<br />
cd /opt/graphite/conf<br />
sudo cp storage-schemas.conf.example storage-schemas.conf<br />
Open "storage-schemas.conf" and remote section [default_1min_for_1day]. Instead, insert the following section:
[kwh]<br />
pattern = .*<br />
retentions = 60s:5y<br />
Note that "kwh" is just a label and has no specific meaning. The "kwh" section is configured to store data every 60 sec for 5 years.
