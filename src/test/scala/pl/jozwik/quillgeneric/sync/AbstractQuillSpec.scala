package pl.jozwik.quillgeneric.sync

import java.time.LocalDateTime

import io.getquill.{ H2JdbcContext, SnakeCase }
import org.scalatest.BeforeAndAfterAll
import pl.jozwik.quillgeneric.AbstractSpec
import pl.jozwik.quillgeneric.model.AddressId
import pl.jozwik.quillgeneric.quillmacro.quotes.DateQuotes
import pl.jozwik.quillgeneric.quillmacro.sync.CrudWithContext
import pl.jozwik.quillgeneric.sync.repository.AddressRepository

trait AbstractQuillSpec extends AbstractSpec with BeforeAndAfterAll {

  protected val (offset, limit) = (0, 100)
  protected val generateId = true
  protected val addressId = AddressId(1)
  protected val now = LocalDateTime.now()

  lazy protected val ctx = new H2JdbcContext(SnakeCase, "h2") with CrudWithContext with DateQuotes

  protected lazy val addressRepository = new AddressRepository(ctx, "Address")

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }

}
