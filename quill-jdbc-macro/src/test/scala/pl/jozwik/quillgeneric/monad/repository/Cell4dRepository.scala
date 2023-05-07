package pl.jozwik.quillgeneric.monad.repository

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Cell4d, Cell4dId }
import pl.jozwik.quillgeneric.monad.TryJdbcRepository.*
import pl.jozwik.quillgeneric.monad.TryJdbcRepositoryCompositeKey

import scala.util.Try

final class Cell4dRepository[Dialect <: SqlIdiom, Naming <: NamingStrategy](
    protected val context: JdbcContextDateQuotes[Dialect, Naming],
    protected val tableName: String = "Cell4d"
)
  extends TryJdbcRepositoryCompositeKey[Cell4dId, Cell4d, Dialect, Naming] {

  protected def dynamicSchema: context.DynamicEntityQuery[Cell4d] = dSchema

  private implicit val dSchema: context.DynamicEntityQuery[Cell4d] = {
    import context._
    context.dynamicQuerySchema[Cell4d](tableName, alias(_.id.fk1, "x"), alias(_.id.fk2, "y"), alias(_.id.fk3, "z"), alias(_.id.fk4, "t"))
  }

  override def all: Try[Seq[Cell4d]] = Try {
    context.all[Cell4d]
  }

  override def create(entity: Cell4d): Try[Cell4dId] = Try {
    context.create[Cell4dId, Cell4d](entity)
  }

  override def read(id: Cell4dId): Try[Option[Cell4d]] = Try {
    context.read[Cell4dId, Cell4d](id)
  }

  override def createOrUpdate(entity: Cell4d): Try[Cell4dId] = Try {
    context.transaction {
      context.createOrUpdate[Cell4dId, Cell4d](entity)
    }
  }

  override def update(entity: Cell4d): Try[Long] = Try {
    context.update[Cell4dId, Cell4d](entity)
  }

  override def delete(id: Cell4dId): Try[Long] = Try {
    context.delete[Cell4dId, Cell4d](id)
  }

  override def deleteAll: Try[Long] = Try {
    context.deleteAll
  }

}
