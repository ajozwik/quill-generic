package pl.jozwik.quillgeneric.cassandra.async

import io.getquill.{ CassandraAsyncContext, SnakeCase }
import pl.jozwik.quillgeneric.cassandra.AbstractCassandraSpec
import pl.jozwik.quillgeneric.quillmacro.async.AsyncCrudWithContext.AsyncCrudWithContextDateQuotesUnit

import scala.concurrent.ExecutionContext

trait AbstractAsyncSpec extends AbstractCassandraSpec {

  protected lazy val ctx                      = new CassandraAsyncContext(SnakeCase, "cassandra") with AsyncCrudWithContextDateQuotesUnit
  protected implicit val ec: ExecutionContext = ExecutionContext.global
}
