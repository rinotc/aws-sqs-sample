import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.{
  DeleteMessageRequest,
  GetQueueUrlRequest,
  Message,
  ReceiveMessageRequest
}

import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.util.{Failure, Success, Try}

object Consumer {

  val sqsClient: SqsClient = SqsClient.create()
  val queueName            = "sample-task-queue"
  var queueUrl: String     = ""

  def main(args: Array[String]): Unit = {
    println("consumer started")

    //noinspection DuplicatedCode
    val getQueueUrlRequest = GetQueueUrlRequest
      .builder()
      .queueName(queueName)
      .build()
    queueUrl = sqsClient.getQueueUrl(getQueueUrlRequest).queueUrl()
    println(s"queue url: $queueUrl")

    var loopFlag = true
    while (loopFlag) {
      println("wait receive message...")
      receiveMessage() match {
        case Failure(e) =>
          e.printStackTrace()
        case Success(messages) =>
          messages.foreach { message =>
            if (message.body() == "quitConsumer") {
              println("received quit message.")
              loopFlag = false
            } else {
              printMessage(message)
            }
            deleteMessage(message)
          }
      }
    }

    println("quit consumer...")
    sqsClient.close()
  }

  def receiveMessage(): Try[Seq[Message]] = Try {
    val receiveMessageRequest = ReceiveMessageRequest
      .builder()
      .queueUrl(queueUrl)
      .waitTimeSeconds(10)
      .maxNumberOfMessages(1)
      .build()
    sqsClient.receiveMessage(receiveMessageRequest).messages().asScala.toSeq
  }

  def deleteMessage(message: Message): Unit = Try {
    val deleteMessageRequest = DeleteMessageRequest
      .builder()
      .queueUrl(queueUrl)
      .receiptHandle(message.receiptHandle())
      .build()
    sqsClient.deleteMessage(deleteMessageRequest)
  } match {
    case Failure(e) => e.printStackTrace()
    case Success(_) => ()
  }

  def printMessage(message: Message): Unit = {
    val str =
      s"""
        |messageId: ${message.messageId()}
        |body: ${message.body()}
        |""".stripMargin

    println(str)
  }
}
