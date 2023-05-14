package pl.jozwik.quillgeneric.monix.repository

import cats.Monad
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import monix.eval.Task
import pl.jozwik.quillgeneric.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.monix.jdbc.MonixJdbcRepository.MonixJdbcContextDateQuotes
import pl.jozwik.quillgeneric.monix.jdbc.MonixJdbcRepositoryWithGeneratedId

final class PersonCustomRepositoryJdbc[D <: SqlIdiom, N <: NamingStrategy](
    protected val context: MonixJdbcContextDateQuotes[D, N],
    tableName: String
)(implicit protected val monad: Monad[Task])
  extends MonixJdbcRepositoryWithGeneratedId[PersonId, Person, D, N] {
  import context.*

  private val aliases = {
    Seq {
      alias[Person](_.birthDate, "dob")
    }
  }

  protected lazy val dynamicSchema: context.DynamicEntityQuery[Person] = context.dynamicQuerySchema[Person](tableName, aliases: _*)

  private def find(id: PersonId) =
    dynamicSchema.filter(_.id == lift(id))

  override def all: Task[Seq[Person]] =
    run(dynamicSchema)

  override def create(entity: Person, generateId: Boolean = true): Task[PersonId] =
    if (generateId) {
      run(dynamicSchema.insertValue(entity).returningGenerated(_.id))
    } else {
      run(dynamicSchema.insertValue(entity).returning(_.id))
    }

  override def read(id: PersonId): Task[Option[Person]] =
    for {
      seq <- run(dynamicSchema.filter(_.id == lift(id)))
    } yield {
      seq.headOption
    }

  override def createOrUpdate(entity: Person, generateId: Boolean = true): Task[PersonId] =
    inTransaction {
      for {
        el <- run(find(entity.id).updateValue(entity))
        id <- el match {
          case 0 =>
            create(entity, generateId)
          case _ =>
            pure[PersonId](entity.id)
        }
      } yield {
        id
      }
    }

  override def update(entity: Person): Task[Long] =
    run(find(entity.id).updateValue(entity))

  override def delete(id: PersonId): Task[Long] =
    run(find(id).delete)

  override def deleteAll: Task[Long] =
    run(dynamicSchema.delete)

}
