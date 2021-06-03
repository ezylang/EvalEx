#!/bin/bash
# expects variables to be set:
# - OSSRH_USERNAME
# - OSSRH_PASSWORD
# - GPG_KEY_NAME
# - GPG_PASSPHRASE
# expects file to exist:
# - .travis/gpg.asc

set -e

echo "starting deploy script deploy.sh ..."

# Check the variables are set
if [ -z "$OSSRH_USERNAME" ]; then
  echo "missing environment value: OSSRH_USERNAME" >&2
  exit 1
fi

if [ -z "$OSSRH_PASSWORD" ]; then
  echo "missing environment value: OSSRH_PASSWORD" >&2
  exit 1
fi

if [ -z "$GPG_KEY_NAME" ]; then
  echo "missing environment value: GPG_KEY_NAME" >&2
  exit 1
fi

if [ -z "$GPG_PASSPHRASE" ]; then
  echo "missing environment value: GPG_PASSPHRASE" >&2
  exit 1
fi

# Prepare the local keyring (requires travis to have decrypted the file
# beforehand)
echo "importing gpg keys"
gpg -v --fast-import .travis/gpg.asc

if [ -n "$TRAVIS_TAG" ]
then
    echo "on a tag -> set pom.xml <version> to $TRAVIS_TAG"
    mvn --settings "${TRAVIS_BUILD_DIR}/.travis/mvn-settings.xml" org.codehaus.mojo:versions-maven-plugin:2.1:set -DnewVersion="$TRAVIS_TAG" 1>/dev/null 2>/dev/null
else
    echo "not on a tag -> keep snapshot version in pom.xml" >&2
fi

# Run the maven deploy steps
mvn deploy -P publish -DskipTests=true --settings "${TRAVIS_BUILD_DIR}/.travis/mvn-settings.xml"
