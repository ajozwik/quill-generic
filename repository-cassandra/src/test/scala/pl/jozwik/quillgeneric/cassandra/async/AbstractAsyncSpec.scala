package pl.jozwik.quillgeneric.cassandra.async

import io.getquill.{CassandraAsyncContext, SnakeCase}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import pl.jozwik.quillgeneric.cassandra.AbstractCassandraSpec
import pl.jozwik.quillgeneric.repository.DateQuotes

import scala.concurrent.ExecutionContext

trait AbstractAsyncSpec extends AbstractCassandraSpec with ScalaFutures {

  protected lazy val ctx                      = new CassandraAsyncContext(SnakeCase, "cassandra") with DateQuotes
  protected implicit val ec: ExecutionContext = ExecutionContext.global
  private val sleep                           = 15

  protected implicit val defaultPatience: PatienceConfig =
    PatienceConfig(timeout = Span(2, Seconds), interval = Span(sleep, Millis))

}
