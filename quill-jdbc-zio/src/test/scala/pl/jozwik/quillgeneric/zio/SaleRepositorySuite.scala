package pl.jozwik.quillgeneric.zio

import pl.jozwik.quillgeneric.model.*
import pl.jozwik.quillgeneric.zio.repository.*
import zio.interop.catz.*
trait SaleRepositorySuite extends AbstractZioJdbcSpec {
  private lazy val saleRepository    = new SaleJdbcRepositoryGen(ctx)
  private lazy val personRepository  = new PersonRepository(ctx, "Person3")
  private lazy val productRepository = new ProductGenRepository(ctx)
  "Sale Repository " should {
    "Call all operations on Sale" in {
      saleRepository.all.runUnsafe() shouldBe Seq()
      val personWithoutId  = Person(PersonId.empty, "firstName", "lastName", today)
      val person           = personRepository.createAndRead(personWithoutId).runUnsafe()
      val productWithoutId = Product(ProductId.empty, "productName")
      val product          = productRepository.createAndRead(productWithoutId).runUnsafe()
      val saleId           = SaleId(product.id, person.id)
      val sale             = Sale(saleId, now, dateTime, today)
      saleRepository.createAndRead(sale).runUnsafe() shouldBe sale
      saleRepository.updateAndRead(sale).runUnsafe() shouldBe sale
      saleRepository.createOrUpdateAndRead(sale).runUnsafe() shouldBe sale

      saleRepository.read(saleId).runUnsafe() shouldBe Option(sale)
      saleRepository.delete(saleId).runUnsafe() shouldBe 1
      productRepository.delete(product.id).runUnsafe() shouldBe 1
      personRepository.delete(person.id).runUnsafe() shouldBe 1
    }
  }

}
