package pl.jozwik.quillgeneric.sync.jdbc

import org.scalatest.TryValues._
import pl.jozwik.quillgeneric.model.{ Configuration, ConfigurationId }
import pl.jozwik.quillgeneric.quillmacro.sync.SyncRepository
import pl.jozwik.quillgeneric.sync.jdbc.repository.ConfigurationRepository

import scala.util.{ Success, Try }

trait ConfigurationRepositorySuite extends AbstractJdbcSpec {
  "ConfigurationRepository" should {
    "Handle simple Configuration with custom id" in {
      val repository: SyncRepository[ConfigurationId, Configuration, Long] = new ConfigurationRepository(ctx)
      //      logger.debug("configuration")
      val entity  = Configuration(ConfigurationId("firstName"), "lastName")
      val entity2 = Configuration(ConfigurationId("nextName"), "nextName")
      repository.all shouldBe Try(Seq())
      val entityId = repository.createOrUpdate(entity)
      entityId shouldBe Symbol("success")
      val entityIdProvided = entityId.success.value
      val createdEntity    = repository.read(entityIdProvided).success.value.getOrElse(fail())
      repository.update(createdEntity) shouldBe Symbol("success")
      repository.all shouldBe Success(Seq(createdEntity))
      val newValue = "newValue"
      val modified = createdEntity.copy(value = newValue)
      repository.updateAndRead(modified) shouldBe Success(modified)
      repository.createOrUpdateAndRead(modified) shouldBe Success(modified)
      repository.read(createdEntity.id).success.value.map(_.value) shouldBe Option(newValue)
      repository.delete(createdEntity.id) shouldBe Symbol("success")
      repository.read(createdEntity.id).success.value shouldBe empty
      repository.all shouldBe Try(Seq())

      repository.createOrUpdate(entity) shouldBe Symbol("success")
      repository.createOrUpdate(entity2) shouldBe Symbol("success")
      repository.createOrUpdateAndRead(entity2) shouldBe Success(entity2)
      repository.update(entity2) shouldBe Symbol("success")
      repository.all.success.value should contain theSameElementsAs Seq(entity, entity2)
      repository.deleteAll shouldBe Symbol("success")
    }
  }
}
