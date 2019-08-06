package pl.jozwik.quillgeneric.sync

import io.getquill.{ H2JdbcContext, SnakeCase }
import org.scalatest.BeforeAndAfterAll
import pl.jozwik.quillgeneric.AbstractSpec
import pl.jozwik.quillgeneric.model.AddressId
import pl.jozwik.quillgeneric.quillmacro.quotes.DateQuotes
import pl.jozwik.quillgeneric.quillmacro.sync.QuillCrudWithContext
import pl.jozwik.quillgeneric.sync.repository.AddressRepository

trait AbstractQuillSpec extends AbstractSpec with BeforeAndAfterAll {
  lazy protected val ctx = new H2JdbcContext(SnakeCase, "h2") with QuillCrudWithContext with DateQuotes
  protected lazy val addressRepository = new AddressRepository(ctx, "Address")
  protected val (offset, limit) = (0, 100)
  protected val generateId = true
  protected val addressId = AddressId(1)

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }
}
