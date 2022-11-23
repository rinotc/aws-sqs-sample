import Constants._
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.{DeleteQueueRequest, GetQueueUrlRequest}

import scala.util.{Failure, Success, Try, Using}

object DeleteQueue {

  def main(args: Array[String]): Unit = {
    println(s"try delete $queueName ...")
    Using(SqsClient.create()) { sqsClient =>
      Try {
        val queueUrl           = sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build()).queueUrl()
        val deleteQueueRequest = DeleteQueueRequest.builder().queueUrl(queueUrl).build()
        sqsClient.deleteQueue(deleteQueueRequest)
      } match {
        case Failure(e) => e.printStackTrace()
        case Success(_) => println("delete succeeded!!")
      }
    }
  }
}
