package pl.jozwik.quillgeneric.monix

import pl.jozwik.quillgeneric.model.{ Configuration, ConfigurationId }
import pl.jozwik.quillgeneric.monix.repository.ConfigurationRepository

trait ConfigurationSuite extends AbstractMonixJdbcSpec {
  "ConfigurationRepository " should {
      "Call all operations on Configuration" in {
        val repository    = new ConfigurationRepository(ctx)
        val configuration = Configuration(ConfigurationId("key"), "value")
        repository
          .inTransaction {
            for {
              _      <- repository.create(configuration)
              actual <- repository.readUnsafe(configuration.id)
            } yield {
              actual shouldBe configuration
            }

          }
          .runSyncUnsafe()
      }
    }
}
