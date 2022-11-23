import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.{
  GetQueueUrlRequest,
  SendMessageRequest,
  SendMessageResponse,
  SqsException
}

import scala.io.StdIn.readLine

object Publisher {

  val sqsClient: SqsClient = SqsClient.create()
  val queueName: String    = Constants.queueName
  var queueUrl: String     = ""

  def main(args: Array[String]): Unit = {
    println("publisher started. if you want finish this process, please input `q`")

    //noinspection DuplicatedCode
    val getQueueUrlRequest = GetQueueUrlRequest
      .builder()
      .queueName(queueName)
      .build()
    queueUrl = sqsClient.getQueueUrl(getQueueUrlRequest).queueUrl()

    println(s"queue url: $queueUrl")

    var loopFlag = true
    while (loopFlag) {
      val input = readLine("input>")
      if (input == "q") {
        sendSqsMessage("quitConsumer")
        loopFlag = false
      } else if (input.isBlank) {
        println("please input something...")
      } else {
        sendSqsMessage(input)
      }
    }

    sqsClient.close()
    println("process finished.")
  }

  def sendSqsMessage(message: String): Unit = {
    try {
      val sendMessageRequest = SendMessageRequest
        .builder()
        .queueUrl(queueUrl)
        .messageBody(message)
        .delaySeconds(5)
        .build()

      val response: SendMessageResponse = sqsClient.sendMessage(sendMessageRequest)
      println("message send.")
      printSendResponse(response)
    } catch {
      case e: SqsException =>
        System.err.println(e.awsErrorDetails().errorMessage())
    }
  }

  def printSendResponse(response: SendMessageResponse): Unit = {
    val str =
      s"""
        |messageId: ${response.messageId()}
        |sequenceNumber: ${response.sequenceNumber()}
        |md5OfMessageBody: ${response.md5OfMessageBody()}
        |""".stripMargin

    println(str)
  }
}
