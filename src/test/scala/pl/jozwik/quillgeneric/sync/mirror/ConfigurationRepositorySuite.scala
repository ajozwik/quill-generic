package pl.jozwik.quillgeneric.sync.mirror

import pl.jozwik.quillgeneric.model.{ Configuration, ConfigurationId }
import pl.jozwik.quillgeneric.sync.mirror.repository.ConfigurationRepository

trait ConfigurationRepositorySuite extends AbstractMirrorSpec {
  "ConfigurationRepository" should {
      "Handle simple Configuration with custom id" in {
        val repository = new ConfigurationRepository(ctx)
        val entity     = Configuration(ConfigurationId("firstName"), "lastName")
        logger.debug(repository.all.string)
        repository.createOrUpdate(entity)
        repository.read(entity.id)
        repository.update(entity).string
        repository.updateAndRead(entity).string
        repository.createOrUpdateAndRead(entity).string
        repository.delete(entity.id).string
        repository.createOrUpdate(entity)

      }
    }
}
