#!/bin/bash

set -e

# 使用方法の表示
if [ $# -ne 2 ]; then
    echo "Usage: $0 <KEY_ID> <PASSPHRASE>"
    echo "Example: $0 ABCD1234 your_passphrase"
    exit 1
fi

KEY_ID=$1
PASSPHRASE=$2

echo "🔑 Exporting GPG secret key..."

# GPG秘密鍵をarmor形式でエクスポートして環境変数に設定
export SIGNING_KEY_ID="$KEY_ID"
export SIGNING_PASSWORD="$PASSPHRASE"
export SIGNING_SECRET_KEY=$(gpg --batch --yes --passphrase="$PASSPHRASE" --armor --export-secret-keys "$KEY_ID")

if [ -z "$SIGNING_SECRET_KEY" ]; then
    echo "❌ Failed to export secret key. Please check your KEY_ID and PASSPHRASE."
    exit 1
fi

echo "✅ Secret key exported successfully"
echo "📦 Publishing to Maven Central..."

# Gradleタスクの実行
./gradlew lib:publishMavenCentralAPI

echo "✅ Publishing completed!"
echo "🔍 Check the deployment status at: https://central.sonatype.com/publishing/deployments"

