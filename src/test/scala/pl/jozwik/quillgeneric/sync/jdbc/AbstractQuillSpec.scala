package pl.jozwik.quillgeneric.sync.jdbc

import io.getquill.{ H2JdbcContext, NamingStrategy, SnakeCase, UpperCase }
import org.scalatest.BeforeAndAfterAll
import pl.jozwik.quillgeneric.AbstractSpec
import pl.jozwik.quillgeneric.model.AddressId
import pl.jozwik.quillgeneric.quillmacro.quotes.DateQuotes
import pl.jozwik.quillgeneric.quillmacro.sync.CrudWithContext
import pl.jozwik.quillgeneric.sync.jdbc.repository.AddressRepository

trait AbstractQuillSpec extends AbstractSpec with BeforeAndAfterAll {

  private val strategy = NamingStrategy(SnakeCase, UpperCase)

  protected val (offset, limit) = (0, 100)
  protected val generateId      = true
  protected val addressId       = AddressId(1)

  lazy protected val ctx = new H2JdbcContext(strategy, "h2") with CrudWithContext with DateQuotes

  protected lazy val addressRepository = new AddressRepository(ctx, "Address")

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }

}
