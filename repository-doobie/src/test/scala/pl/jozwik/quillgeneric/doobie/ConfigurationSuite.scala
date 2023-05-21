package pl.jozwik.quillgeneric.doobie

import pl.jozwik.quillgeneric.doobie.repository.ConfigurationRepository
import pl.jozwik.quillgeneric.model.{ Configuration, ConfigurationId }

trait ConfigurationSuite extends AbstractDoobieJdbcSpec {

  private lazy val repository = new ConfigurationRepository(ctx)

  "ConfigurationRepository " should {
    "All is empty" in {
      repository.all.runUnsafe() shouldBe empty
    }

    "Call all operations on Configuration" in {

      val configuration = Configuration(ConfigurationId("key"), "value")
      val task = repository
        .inTransaction {
          for {
            _      <- repository.create(configuration)
            actual <- repository.readUnsafe(configuration.id)
          } yield {
            actual shouldBe configuration
          }

        }
      task.runUnsafe()

      repository.createOrUpdateAndRead(configuration).runUnsafe() shouldBe configuration
    }
  }
}
