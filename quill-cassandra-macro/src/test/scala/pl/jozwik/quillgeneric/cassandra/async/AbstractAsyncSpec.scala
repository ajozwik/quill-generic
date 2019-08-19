package pl.jozwik.quillgeneric.cassandra.async

import io.getquill.{ CassandraAsyncContext, SnakeCase }
import pl.jozwik.quillgeneric.cassandra.AbstractCassandraSpec
import pl.jozwik.quillgeneric.quillmacro.async.AsyncCrudWithContext.AsyncCrudWithContextDateQuotesUnit

trait AbstractAsyncSpec extends AbstractCassandraSpec {
  protected lazy val ctx = new CassandraAsyncContext(SnakeCase, "cassandra") with AsyncCrudWithContextDateQuotesUnit
}
