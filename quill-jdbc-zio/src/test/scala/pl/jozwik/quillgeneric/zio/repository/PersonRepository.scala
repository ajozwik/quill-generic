package pl.jozwik.quillgeneric.zio.repository

import cats.Monad
import io.getquill.*
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.zio.*
import zio.Task
final class PersonRepository[Dialect <: SqlIdiom, Naming <: NamingStrategy](
    protected val context: ZioJdbcRepository.ZioJdbcContextWithDateQuotes[Dialect, Naming],
    tableName: String = "Person"
)(implicit protected val monad: Monad[Task])
  extends ZioJdbcRepositoryWithGeneratedId[PersonId, Person, Dialect, Naming] {

  import context.*

  private val aliases =
    Seq {
      alias[Person](_.birthDate, "dob")
    }

  protected lazy val dynamicSchema: context.DynamicEntityQuery[Person] =
    context.dynamicQuerySchema[Person](tableName, aliases*)

  private def find(id: PersonId) =
    dynamicSchema.filter(_.id == lift(id))

  override def all: Task[Seq[Person]] =
    for {
      all <- run(dynamicSchema)
    } yield {
      all
    }

  override def create(entity: Person, generateId: Boolean = true): Task[PersonId] =
    if (generateId) {
      run(dynamicSchema.insertValue(entity).returningGenerated(_.id))
    } else {
      run(dynamicSchema.insertValue(entity).returning(_.id))
    }

  override def createOrUpdate(entity: Person, generateId: Boolean = true): Task[PersonId] =
    inTransaction {
      toTask {
        for {
          el <- run(find(entity.id).updateValue(entity))
          id <- el match {
            case 0 =>
              create(entity, generateId)
            case _ =>
              pure(entity.id)
          }
        } yield {
          id
        }
      }
    }

  override def read(id: PersonId): Task[Option[Person]] =
    for {
      seq <- run(find(id))
    } yield {
      seq.headOption
    }

  override def update(entity: Person): Task[Long] =
    run(find(entity.id).updateValue(entity))

  override def delete(id: PersonId): Task[Long] =
    run(find(id).delete)

  override def deleteAll: Task[Long] =
    run(dynamicSchema.delete)

}
