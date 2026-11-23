# Androidアプリのリリース手順

1. **初回のみ、APK署名を設定する。** [キーストアの作成手順](KeyStore.md)に従って、署名鍵を用意し、GitHub ActionsのRepository secretsを登録します。
2. **バージョンを更新する。** `versionCode` を前のリリースより大きくし、`versionName` を配布するバージョンに合わせます。変更をコミットして `main` に反映します。
3. **リリース対象のコミットに新しいタグを付けてpushする。** 以下は `v0.0.2` の例です。既存タグの実行を再実行すると、タグが指す当時のコミットのワークフローが使われます。

   ```powershell
   git switch main
   git pull --ff-only origin main
   git tag -a v0.0.2 -m "Release v0.0.2"
   git push origin v0.0.2
   ```

4. **APKを確認する。** GitHubの **Actions → Android CI and Release** でタグの実行結果を確認します。ビルドと署名検証に成功すると、**Releases → v0.0.2 → Assets** に `app-release.apk` が公開されます。
