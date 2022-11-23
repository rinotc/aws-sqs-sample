# SQS Sample Scala

AWS SDK v2 を利用して簡単にSQSを試すサンプル

## キューの名前

利用されるキューの名前は `Constants` に定義している

`sample-queue`

です。

AWSのクレデンシャル情報はローカルにセットされているデフォルトのものを利用します。
※設定済み前提

## それぞれの説明

- `CreateQueue`: `sample-queue` という名前のQueueを作成する
- `DeleteQueue`: `sample-queue` という名前のQueueを削除する
- `Publisher`: 標準入力で受け取った文字列をQueueに投げる
- `Consumer`: QueueからメッセージをConsumeし、出力する

## CreateQueue

`sample-queue` という名前のqueueが作られます。

```shell
sbt "runMain CreateQueue"
```

## DeleteQueue

`sample-queue` という名前のqueueを削除します。

```shell
sbt "runMain DeleteQueue"
```

## Publisher

`input>` に適当な文字列を入力してください。
`q` と入力すると、Consumer も終了するメッセージを投げて、終了します。

```shell
sbt "runMain Publisher"
#... logback などの出力
publisher started. if you want finish this process, please input `q`
queue url: <queue url>
input>
```

## Consumer

Publisher側で投げたメッセージを受け取って出力します。
Publisher側で `q` を入力すると、SQS経由で終了メッセージを受けて、タスクを終了します。

```shell
sbt "runMain Consumer"
# ... logback などの出力
consumer started
queue url: <queue url>
wait receive message...
```