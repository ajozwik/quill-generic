package pl.jozwik.quillgeneric.mirror

import pl.jozwik.quillgeneric.model.{ Configuration, ConfigurationId }
import pl.jozwik.quillgeneric.mirror.repository.ConfigurationRepository

trait ConfigurationRepositorySuite extends AbstractMirrorSpec {
  "ConfigurationRepository" should {
      "Handle simple Configuration with custom id" in {
        val repository = new ConfigurationRepository(ctx)
        val entity     = Configuration(ConfigurationId("firstName"), "lastName")
        logToConsole(repository.all.string)
        logToConsole(repository.createOrUpdate(entity))
        logToConsole(repository.read(entity.id).string)
        logToConsole(repository.update(entity).string)
        logToConsole(repository.updateAndRead(entity).string)
        logToConsole(repository.createOrUpdateAndRead(entity).string)
        logToConsole(repository.delete(entity.id).string)

      }
    }
}
