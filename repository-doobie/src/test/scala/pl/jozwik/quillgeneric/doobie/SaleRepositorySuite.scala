package pl.jozwik.quillgeneric.doobie

import pl.jozwik.quillgeneric.doobie.repository.*
import pl.jozwik.quillgeneric.model.*

trait SaleRepositorySuite extends AbstractDoobieJdbcSpec {
  private lazy val saleRepository    = new SaleRepositoryGen(ctx)
  private lazy val personRepository  = new PersonRepository(ctx, "Person3")
  private lazy val productRepository = new ProductRepository(ctx)
  "Sale Repository " should {
    "Search empty repository" in {
      saleRepository.all.runUnsafe() shouldBe empty
    }
    "Call all operations on Sale" in {

      val personWithoutId  = Person(PersonId.empty, "firstName", "lastName", today)
      val person           = personRepository.createAndRead(personWithoutId).runUnsafe()
      val productWithoutId = Product(ProductId.empty, "productName")
      val product          = productRepository.createAndRead(productWithoutId).runUnsafe()
      val saleId           = SaleId(product.id, person.id)
      val sale             = Sale(saleId, now, dateTime, today)
      saleRepository.createAndRead(sale).runUnsafe() shouldBe sale
      saleRepository.createOrUpdateAndRead(sale).runUnsafe() shouldBe sale
      saleRepository.update(sale).runUnsafe() shouldBe 1
      saleRepository.read(saleId).runUnsafe() shouldBe Option(sale)
      saleRepository.delete(saleId).runUnsafe() shouldBe 1
      productRepository.delete(product.id).runUnsafe() shouldBe 1
      personRepository.delete(person.id).runUnsafe() shouldBe 1
    }
  }

}
