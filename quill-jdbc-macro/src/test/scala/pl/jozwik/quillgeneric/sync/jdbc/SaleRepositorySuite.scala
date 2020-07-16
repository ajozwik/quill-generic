package pl.jozwik.quillgeneric.sync.jdbc

import pl.jozwik.quillgeneric.model._
import pl.jozwik.quillgeneric.sync.jdbc.repository.{ PersonRepository, ProductRepository, SaleRepository }
import org.scalatest.TryValues._

import scala.util.{ Failure, Success }

trait SaleRepositorySuite extends AbstractJdbcSpec {

  private val repository        = new SaleRepository(ctx, "Sale")
  private val personRepository  = new PersonRepository(ctx, "Person2")
  private val productRepository = new ProductRepository(ctx, "Product")
  "Sale Repository " should {
    "Call all operations on Sale" in {
      repository.all shouldBe Success(Seq())
      val personWithoutId = Person(PersonId.empty, "firstName", "lastName", today)
      val maybePerson     = personRepository.createAndRead(personWithoutId)
      maybePerson shouldBe Symbol("success")
      val person           = maybePerson.success.value
      val productWithoutId = Product(ProductId.empty, "productName")
      val product          = productRepository.createAndRead(productWithoutId).success.value
      val saleId           = SaleId(product.id, person.id)
      val sale             = Sale(saleId, now)
      repository.createAndRead(sale) shouldBe Symbol("success")

      repository.createOrUpdateAndRead(sale) shouldBe Symbol("success")
      repository
        .inTransaction {
          for {
            actualRead <- repository.read(saleId)
            actualSeq  <- repository.searchFrom(now)
          } yield {
            actualRead shouldBe Option(sale)
            actualSeq shouldBe Seq(sale)
          }
        }
        .recoverWith {
          case th =>
            logger.error("", th)
            Failure(th)
        }
        .success
        .value
    }
  }

}
