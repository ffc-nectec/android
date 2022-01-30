#!/bin/sh

# max NOTE
# Ref: https://docs.github.com/en/actions/security-guides/encrypted-secrets
# enscrypt command = gpg --symmetric --cipher-algo AES256 my_secret.json
# Decrypt the file
# mkdir $HOME/secrets
# --batch to prevent interactive command
# --yes to assume "yes" for questions
pwd
echo "$GET_ENV_TEST"
echo "_____Decrypt_______"
gpg --quiet --batch --yes --decrypt --passphrase="$LARGE_SECRET_PASSPHRASE" \
--output ffc/google-services.json ffc/google-services.json.gpg
