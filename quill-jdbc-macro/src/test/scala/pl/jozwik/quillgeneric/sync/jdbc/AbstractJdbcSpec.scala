package pl.jozwik.quillgeneric.sync.jdbc

import io.getquill.H2JdbcContext
import org.scalatest.BeforeAndAfterAll
import pl.jozwik.quillgeneric.AbstractSpec
import pl.jozwik.quillgeneric.quillmacro.sync.CrudWithContext.CrudWithContextDateQuotesLong
import pl.jozwik.quillgeneric.sync.jdbc.repository.AddressRepository

trait AbstractJdbcSpec extends AbstractSpec with BeforeAndAfterAll {

  lazy protected val ctx = new H2JdbcContext(strategy, "h2") with CrudWithContextDateQuotesLong

  protected lazy val addressRepository = new AddressRepository(ctx, "Address")

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }

}
