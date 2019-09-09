package pl.jozwik.quillgeneric.sync.jdbc

import pl.jozwik.quillgeneric.model._
import pl.jozwik.quillgeneric.sync.jdbc.repository.{ PersonRepository, ProductRepository, SaleRepository }
import org.scalatest.TryValues._
import scala.util.Success

trait SaleRepositorySuite extends AbstractJdbcSpec {

  private val repository        = new SaleRepository(ctx, "Sale")
  private val personRepository  = new PersonRepository(ctx, "Person2")
  private val productRepository = new ProductRepository(ctx, "Product")
  "Sale Repository " should {
      "Call all operations on Sale" in {
        repository.all shouldBe Success(Seq())
        val personWithoutId = Person(PersonId.empty, "firstName", "lastName", today)
        val maybePerson     = personRepository.createAndRead(personWithoutId)
        maybePerson shouldBe 'success
        val person           = maybePerson.success.get
        val productWithoutId = Product(ProductId.empty, "productName")
        val product          = productRepository.createAndRead(productWithoutId).success.get
        val saleId           = SaleId(product.id, person.id)
        val sale             = Sale(saleId, now)
        repository.createAndRead(sale) shouldBe 'success

        repository.createOrUpdateAndRead(sale) shouldBe 'success

        repository.read(saleId).success.get shouldBe Option(sale)
        repository.searchFrom(now).success.get shouldBe Seq(sale)
      }
    }

}
