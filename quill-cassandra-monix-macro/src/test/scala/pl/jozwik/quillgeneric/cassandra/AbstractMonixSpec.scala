package pl.jozwik.quillgeneric.cassandra

import io.getquill.{ CassandraMonixContext, SnakeCase }
import pl.jozwik.quillgeneric.quillmacro.monix.MonixWithContextDateQuotes.MonixWithContextDateQuotesUnit

trait AbstractMonixSpec extends AbstractCassandraSpec {
  protected lazy val ctx = new CassandraMonixContext(SnakeCase, "cassandraMonix") with MonixWithContextDateQuotesUnit
}
