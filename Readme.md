# Androidアプリのリリース手順

1. **初回のみ、APK署名を設定する。** リリース用キーストアを用意し、GitHub ActionsのSecretsにキーストアをBase64化した値、キーストアのパスワード、キーのエイリアス、キーのパスワードを登録します。[ワークフロー](.github/workflows/android.yml)にキーストアを復元してGradleへ署名情報を渡す処理を追加し、[アプリのGradle設定](MyAndroidApp/app/build.gradle.kts)の `release` ビルドに `signingConfig` を設定します。現時点ではこの連携が未実装です。キーストアと認証情報はGitにコミットしないでください。
2. **バージョンを更新する。** `versionCode` を前のリリースより大きくし、`versionName` を配布するバージョンに合わせます。変更をコミットして `main` に反映します。
3. **リリース対象のコミットにタグを付けてpushする。** 以下はバージョン `1.0.0` の例です。

   ```powershell
   git switch main
   git pull --ff-only origin main
   git tag -a v0.0.1 -m "Release v0.0.1"
   git push origin v0.0.1
   ```

4. **APKを確認する。** GitHubの **Actions → Android CI and Release** でタグの実行結果を確認します。ビルドと署名検証に成功すると、**Releases → v1.0.0 → Assets** に `app-release.apk` が公開されます。
