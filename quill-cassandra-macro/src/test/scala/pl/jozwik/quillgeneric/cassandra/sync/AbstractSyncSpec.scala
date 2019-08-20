package pl.jozwik.quillgeneric.cassandra.sync

import io.getquill.{ CassandraSyncContext, SnakeCase }
import pl.jozwik.quillgeneric.cassandra.AbstractCassandraSpec
import pl.jozwik.quillgeneric.quillmacro.sync.CrudWithContext.CrudWithContextDateQuotesUnit

trait AbstractSyncSpec extends AbstractCassandraSpec {
  protected lazy val ctx = new CassandraSyncContext(SnakeCase, "cassandra") with CrudWithContextDateQuotesUnit
}
