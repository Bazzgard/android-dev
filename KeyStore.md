# Androidリリース用キーストアの作成手順

初回リリース用の手順です。配布済みアプリを更新する場合は、初回リリースで使ったキーストアを使います。

## 1. キーストアを作る

Docker Desktopを起動し、`compose.yaml` があるフォルダでPowerShellから実行します。

```powershell
docker compose up -d android-dev
docker compose exec android-dev keytool -genkeypair `
  -keystore /tmp/release.jks -storetype JKS -alias release `
  -keyalg RSA -keysize 2048 -validity 10000
```

キーストアのパスワードと証明書の情報を対話形式で入力します。キーのパスワードを聞かれたときにEnterを押すと、キーストアと同じパスワードになります。

## 2. Windowsに保存する

Gitリポジトリの外にあるユーザーフォルダに保存します。同名のファイルがすでにある場合は、上書きする前に既存の署名鍵かどうか確認してください。

```powershell
$signingDir = Join-Path $env:USERPROFILE 'android-signing'
New-Item -ItemType Directory -Force -Path $signingDir | Out-Null
$keystorePath = Join-Path $signingDir 'release.jks'
if (Test-Path -LiteralPath $keystorePath) {
    throw "Keystore already exists: $keystorePath"
}
docker compose cp android-dev:/tmp/release.jks "$keystorePath"
```

## 3. GitHub Actionsに登録する

キーストアをBase64に変換し、クリップボードにコピーします。

```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes($keystorePath)) | Set-Clipboard
```

GitHubリポジトリの **Settings → Secrets and variables → Actions → Repository secrets → New repository secret** から、以下を1件ずつ登録します。

| Secret | 値 |
| --- | --- |
| `ANDROID_KEYSTORE_BASE64` | クリップボードにコピーしたBase64文字列 |
| `ANDROID_KEYSTORE_PASSWORD` | キーストアのパスワード |
| `ANDROID_KEY_ALIAS` | `release` |
| `ANDROID_KEY_PASSWORD` | キーのパスワード。Enterで省略した場合はキーストアと同じ値 |

キーストアとパスワードは安全な場所にバックアップしてください。キーストア本体、Base64文字列、パスワードはGitにコミットしないでください。

登録後は [Androidアプリのリリース手順](Readme.md) に進みます。
