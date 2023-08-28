# Notice Box a.k.a 詫び石配りプラグイン

コマンドとインベントリGUIを用いてユーザーにお知らせを送信することが出来ます

Spigot 1.20

## 🐄 前提プラグイン

- Vault
- Vaultに対応した経済プラグイン

## 💡 使い方

### 💬 コマンド

| コマンド | 引数 | 説明 | パーミッション |
| --- | --- | --- | --- |
| /noticebox add |  | お知らせを作成するための本を取得します | `noticebox.add` |
| /noticebox remove |  | お知らせを削除するためのGUIを表示します | `noticebox.remove` |
| /noticebox open |  | お知らせ一覧のGUIを表示します | `noticebox.open` |
| /noticebox open | `<player>` | `<player>`に対してお知らせ一覧のGUIを表示します | `noticebox.open-other` |

### 👦 お知らせ作成の流れ

1. `/noticebox add`を実行し本を取得する
2. 本を開きお知らせの本文を入力する
3. 本のタイトル部分にお知らせのタイトルを入力し署名する
4. 表示されたGUIで報酬等の設定を行い右下の作成ボタンをクリックする

## 🖋 備考

- 本の署名時に入力可能なタイトルは15文字までです。より長いタイトルを入力したい場合は署名後に表示されるGUIの「タイトルを設定」をご利用ください。

## このプロジェクトは以下を使用して開発されています

- [CommandAPI](https://github.com/JorelAli/CommandAPI) by Jorel Ali

- [InventoryGui](https://github.com/Phoenix616/InventoryGui) by Max Lee

- [AnvilGUI](https://github.com/WesJD/AnvilGUI) by Wesley Smith
