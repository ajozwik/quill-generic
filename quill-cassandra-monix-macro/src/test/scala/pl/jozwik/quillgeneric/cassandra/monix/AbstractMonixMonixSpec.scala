package pl.jozwik.quillgeneric.cassandra.monix

import io.getquill.{ CassandraMonixContext, SnakeCase }
import monix.execution.Scheduler
import pl.jozwik.quillgeneric.quillmacro.monix.MonixWithContextDateQuotes.MonixWithContextDateQuotesUnit

trait AbstractMonixMonixSpec extends AbstractCassandraMonixSpec {
  protected lazy val ctx                      = new CassandraMonixContext(SnakeCase, "cassandraMonix") with MonixWithContextDateQuotesUnit
  protected implicit val scheduler: Scheduler = Scheduler.Implicits.global
}
