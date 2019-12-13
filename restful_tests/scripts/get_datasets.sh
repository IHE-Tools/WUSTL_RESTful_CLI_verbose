#!/bin/sh

echo Data files will be retrieved and stored in /tmp/cli
mkdir -p /tmp/cli

for i in \
	ftp://ftp.ihe.net/Connectathon/test_data/RAD-profiles/XDS-I.b_test_data/XDS-ImageCache/ImageCache-20190325.zip	\
 ; do
 wget -P /tmp/cli $i
done
