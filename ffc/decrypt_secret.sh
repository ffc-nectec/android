#!/bin/sh

# max NOTE
# Ref: https://docs.github.com/en/actions/security-guides/encrypted-secrets
# enscrypt command = gpg --symmetric --cipher-algo AES256 my_secret.json
# Decrypt the file
# mkdir $HOME/secrets
# --batch to prevent interactive command
# --yes to assume "yes" for questions
gpg --quiet --batch --yes --decrypt --passphrase="$LARGE_SECRET_PASSPHRASE" \
--output $HOME/ffc/google-services.json $HOME/ffc/google-services.json.gpg
