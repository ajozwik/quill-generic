package pl.jozwik.quillgeneric.sync

import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Person2, PersonId }
import pl.jozwik.quillgeneric.quillmacro.sync.{ Queries, Repository }

import scala.util.Try

final class Person2Repository[Dialect <: SqlIdiom, Naming <: NamingStrategy](
    protected val context: JdbcContext[Dialect, Naming] with Queries,
    protected val tableName: String)
  extends Repository[Option[PersonId], Person2] {

  override def all: Try[Seq[Person2]] =
    context.all[Person2]

  override def create(entity: Person2, generateId: Boolean = false): Try[Option[PersonId]] =
    if (generateId) {
      context.createAndGenerateId[Option[PersonId], Person2](entity)
    } else {
      context.create[Option[PersonId], Person2](entity)
    }

  override def read(id: Option[PersonId]): Try[Option[Person2]] =
    context.read[Option[PersonId], Person2](id)

  override def createOrUpdate(entity: Person2, generateId: Boolean = false): Try[Option[PersonId]] =
    context.createOrUpdate[Option[PersonId], Person2](entity, generateId)

  override def update(entity: Person2): Try[Long] =
    context.update[Person2](entity)

  override def update(id: Option[PersonId], action: Person2 => (Any, Any), actions: Function[Person2, (Any, Any)]*): Try[Long] =
    context.updateByFilter[Person2](_.id == id, action, actions: _*)

  override def delete(id: Option[PersonId]): Try[Boolean] =
    context.deleteByFilter[Person2](_.id == id)

}
