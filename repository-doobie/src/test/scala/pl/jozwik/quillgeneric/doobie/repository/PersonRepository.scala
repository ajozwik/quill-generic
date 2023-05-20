package pl.jozwik.quillgeneric.doobie.repository

import doobie.ConnectionIO
import io.getquill.*
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.doobie.DoobieRepository.DoobieJdbcContextWithDateQuotes
import pl.jozwik.quillgeneric.doobie.DoobieRepositoryWithTransactionWithGeneratedId
import pl.jozwik.quillgeneric.model.{ Person, PersonId }
final class PersonRepository[Dialect <: SqlIdiom, Naming <: NamingStrategy](
    protected val context: DoobieJdbcContextWithDateQuotes[Dialect, Naming],
    tableName: String = "Person"
)(implicit protected val monad: cats.Monad[ConnectionIO])
  extends DoobieRepositoryWithTransactionWithGeneratedId[PersonId, Person, Dialect, Naming] {

  import context.*

  private val aliases =
    Seq {
      alias[Person](_.birthDate, "dob")
    }

  protected lazy val dynamicSchema: context.DynamicEntityQuery[Person] =
    context.dynamicQuerySchema[Person](tableName, aliases*)

  private def find(id: PersonId) =
    dynamicSchema.filter(_.id == lift(id))
  override def all: ConnectionIO[Seq[Person]] =
    for {
      all <- run(dynamicSchema)
    } yield {
      all
    }

  override def create(entity: Person, generateId: Boolean = true): ConnectionIO[PersonId] =
    if (generateId) {
      run(dynamicSchema.insertValue(entity).returningGenerated(_.id))
    } else {
      run(dynamicSchema.insertValue(entity).returning(_.id))
    }

  override def createOrUpdate(entity: Person, generateId: Boolean = true): ConnectionIO[PersonId] = {
    inTransaction {
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

  override def read(id: PersonId): ConnectionIO[Option[Person]] =
    for {
      seq <- run(find(id))
    } yield {
      seq.headOption
    }

  override def update(entity: Person): ConnectionIO[Long] =
    run(find(entity.id).updateValue(entity))

  override def delete(id: PersonId): ConnectionIO[Long] =
    run(find(id).delete)

  override def deleteAll: ConnectionIO[Long] =
    run(dynamicSchema.delete)

}
