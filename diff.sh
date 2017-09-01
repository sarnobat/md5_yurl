#!/bin/bash

comm -2 -3 \
	<(cat /media/sarnobat/Unsorted/new/Photos/iPhone/Masters/md5_files.txt | sort) \
	<(cat /media/sarnobat/Unsorted/new/Photos/iPhone/Masters/md5_files.txt | grep -v 2016 | perl -pe 's{^([^\s]+)\s+}{}g' | sort)