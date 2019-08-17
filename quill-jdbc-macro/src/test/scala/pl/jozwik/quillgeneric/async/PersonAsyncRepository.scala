package pl.jozwik.quillgeneric.async

import com.github.mauricio.async.db.Connection
import io.getquill.context.async.AsyncContext
import io.getquill.context.sql.idiom.SqlIdiom
import io.getquill.{ MysqlAsyncContext, NamingStrategy, SnakeCase }
import pl.jozwik.quillgeneric.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.quillmacro.async.{ AsyncRepository, QuillCrudWithAsyncContext }

import scala.concurrent.{ ExecutionContext, Future }

object AsyncObject {
  lazy val personAsyncRepository = new PersonAsyncRepository(new MysqlAsyncContext(SnakeCase, "async.mysql") with QuillCrudWithAsyncContext)
}

class PersonAsyncRepository[D <: SqlIdiom, N <: NamingStrategy, C <: Connection](context: AsyncContext[D, N, C] with QuillCrudWithAsyncContext)
  extends AsyncRepository[PersonId, Person] {

  override def all(implicit ex: ExecutionContext): Future[Seq[Person]] =
    context.all[Person]

  override def createOrUpdate(entity: Person, generateId: Boolean = false)(implicit ex: ExecutionContext): Future[PersonId] =
    context.createOrUpdate[PersonId, Person](entity, generateId)

  override def create(person: Person, generateId: Boolean = false)(implicit ex: ExecutionContext): Future[PersonId] =
    if (generateId) {
      context.createAndGenerateId[PersonId, Person](person)
    } else {
      context.create[PersonId, Person](person)
    }

  override def read(id: PersonId)(implicit ex: ExecutionContext): Future[Option[Person]] =
    context.read[PersonId, Person](id)

  override def update(person: Person)(implicit ex: ExecutionContext): Future[Long] =
    context.merge[Person](person)

  override def update(id: PersonId, action: Person => (Any, Any), actions: Function[Person, (Any, Any)]*)(implicit ex: ExecutionContext): Future[Long] =
    context.mergeById[Person](_.id == id, action, actions: _*)

  override def delete(id: PersonId)(implicit ex: ExecutionContext): Future[Boolean] =
    context.deleteByFilter[Person](_.id == id)

  override protected val tableName: String = "Person"
}
