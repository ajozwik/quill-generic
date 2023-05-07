package pl.jozwik.quillgeneric.monad

import io.getquill.H2JdbcContext
import org.scalatest.{ BeforeAndAfterAll, TryValues }
import pl.jozwik.quillgeneric.AbstractSpec
import pl.jozwik.quillgeneric.monad.repository.AddressRepository
import pl.jozwik.quillgeneric.quillmacro.sync.CrudWithContextDateQuotes

trait AbstractJdbcSpec extends AbstractSpec with BeforeAndAfterAll with TryValues {

  lazy protected val ctx = new H2JdbcContext(strategy, "h2") with CrudWithContextDateQuotes[Long]

  protected lazy val addressRepository = new AddressRepository(ctx, "Address")

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }

}
