package pl.jozwik.quillgeneric.async

import com.github.mauricio.async.db.Connection
import io.getquill.context.async.AsyncContext
import io.getquill.context.sql.idiom.SqlIdiom
import io.getquill.{ MysqlAsyncContext, NamingStrategy, SnakeCase }
import pl.jozwik.quillgeneric.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.quillmacro.async.{ AsyncRepository, QueriesAsync }

import scala.concurrent.{ ExecutionContext, Future }

object AsyncObject {
  lazy val personAsyncRepository = new PersonAsyncRepository(new MysqlAsyncContext(SnakeCase, "async.mysql") with QueriesAsync)
}

class PersonAsyncRepository[D <: SqlIdiom, N <: NamingStrategy, C <: Connection](ctx: AsyncContext[D, N, C] with QueriesAsync)
  extends AsyncRepository[PersonId, Person] {

  import ctx._

  override def all(implicit ex: ExecutionContext): Future[Seq[Person]] =
    ctx.all[Person]

  override def createOrUpdate(entity: Person, generateId: Boolean = false)(implicit ex: ExecutionContext): Future[PersonId] =
    ctx.createOrUpdate[PersonId, Person](entity, generateId)

  override def create(person: Person, generateId: Boolean = false)(implicit ex: ExecutionContext): Future[PersonId] =
    if (generateId) {
      ctx.createAndGenerateId[PersonId, Person](person)
    } else {
      ctx.create[PersonId, Person](person)
    }

  override def read(id: PersonId)(implicit ex: ExecutionContext): Future[Seq[Person]] =
    ctx.run(query[Person].filter(_.id == lift(id)))

  override def update(person: Person)(implicit ex: ExecutionContext): Future[Long] =
    ctx.merge[Person](person)

  override def update(id: PersonId, action: Person => (Any, Any), actions: Function[Person, (Any, Any)]*)(implicit ex: ExecutionContext): Future[Long] =
    ctx.mergeById[Person](_.id == lift(id), action, actions: _*)

  override def delete(id: PersonId)(implicit ex: ExecutionContext): Future[Boolean] =
    ctx.deleteByFilter[Person](_.id == lift(id))
}
