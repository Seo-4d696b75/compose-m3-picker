# How to publish

開発環境・バージョン毎に必要な作業の備忘録

以下の状態を前提とする

- GPGによる署名方法は準備済み（鍵を生成済み）
- Central Portalにアカウントを作成済み・namespaceも取得済み

## 公開作業

毎回行う作業内容

### バージョン更新

- libs.versions.toml `versions.publish` を更新する
- 署名付きのタグを打つ `git tag -s $name -m $message`

### 公開スクリプトの実行

```shell
./publish.sh YOUR_KEY_ID YOUR_PASSPHRASE
```

スクリプトは以下の処理を自動で行います：
- GPG秘密鍵のエクスポート（メモリ内のみ）
- 環境変数経由でGradleへ署名情報を渡す
- Maven Central APIへの公開

### 確認

https://central.sonatype.com/publishing/deployments

## 開発環境のセットアップ

初回のみ必要な準備作業

### GPG公開鍵の送信

Gitコミットへの署名だけでは不十分。
Maven Centralへの公開には、鍵サーバーへの公開鍵の登録が必要

```shell
gpg --keyserver keyserver.ubuntu.com --send-keys $id
```

### UserToken取得

username と password (token) を取得する

https://central.sonatype.com/usertoken

`~/.gradle/gradle.properties`ファイルに記載しておくのが一般的

```properties
SONATYPE_USERNAME=$username
SONATYPE_PASSWORD=$password
```

### 署名用の鍵ID確認

公開スクリプト実行時に必要な鍵IDを確認する

```shell
gpg --list-secret-keys --keyid-format=short
```

出力例の `sec rsa4096/ABCD1234` の `/` 以降の8文字（Short形式）を使用

**注意：** 秘密鍵は実行時にメモリ内で動的にエクスポートされるため、ファイルへの保存は不要です