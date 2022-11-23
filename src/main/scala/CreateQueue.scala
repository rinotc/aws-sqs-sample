import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.{CreateQueueRequest, GetQueueUrlRequest}

import scala.util.{Failure, Success, Try, Using}
import Constants._

object CreateQueue {

  def main(args: Array[String]): Unit = {
    println("create queue.")

    Using(SqsClient.create()) { sqsClient =>
      val createQueueRequest = CreateQueueRequest
        .builder()
        .queueName(queueName)
        .build()

      Try {
        sqsClient.createQueue(createQueueRequest)
        val getQueueUrlRequest = GetQueueUrlRequest.builder().queueName(queueName).build()
        sqsClient.getQueueUrl(getQueueUrlRequest)
      } match {
        case Failure(e) => e.printStackTrace()
        case Success(response) =>
          val str =
            s"""
              |queue created.
              |url: ${response.queueUrl()}""".stripMargin
          println(str)
      }
    }
  }
}
