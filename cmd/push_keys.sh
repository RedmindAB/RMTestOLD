#!/bin/sh

# A script to push a key to the only argument, a remote server.

# Check if an argument was given.
if [ ! "$1" ] ; then
echo "Please specify a hostname to distribute the key to."
exit 1
fi

# Check if all the local files are here.
if [ ! -f ~/.ssh/id_dsa.pub ] ; then
echo "The local file ~/.ssh/id_dsa.pub is missing. Please create it."
exit 1
fi

# This command send the key, create a .ssh dir if required and set the
# correct permissions.
cat ~/.ssh/id_dsa.pub | ssh -q "$1" "if [ ! -d ~/.ssh/ ] ; then mkdir ~/.ssh ; fi ; chmod 700 ~/.ssh/ ; cat - >> ~/.ssh/authorized_keys ; chmod 600 ~/.ssh/authorized_keys"
