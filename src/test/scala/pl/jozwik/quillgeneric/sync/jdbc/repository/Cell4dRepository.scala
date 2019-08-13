package pl.jozwik.quillgeneric.sync.jdbc.repository

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Cell4d, Cell4dId }
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepository.JdbcContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.sync.RepositoryCompositeKey

import scala.util.Try

final class Cell4dRepository[Dialect <: SqlIdiom, Naming <: NamingStrategy](
    protected val context: JdbcContextDateQuotes[Dialect, Naming],
    protected val tableName: String = "Cell4d"
)
  extends RepositoryCompositeKey[Cell4dId, Cell4d] {

  protected def dynamicSchema: context.DynamicEntityQuery[Cell4d] = dSchema

  private implicit val dSchema: context.DynamicEntityQuery[Cell4d] = {
    import context._
    context.dynamicQuerySchema[Cell4d](tableName, alias(_.id.fk1, "x"), alias(_.id.fk2, "y"), alias(_.id.fk3, "z"), alias(_.id.fk4, "t"))
  }

  override def all: Try[Seq[Cell4d]] =
    context.all[Cell4d]

  override def create(entity: Cell4d): Try[Cell4dId] =
    context.create[Cell4dId, Cell4d](entity)

  override def createAndRead(entity: Cell4d): Try[Cell4d] =
    context.createAndRead[Cell4dId, Cell4d](entity)

  override def read(id: Cell4dId): Try[Option[Cell4d]] =
    context.read[Cell4dId, Cell4d](id)

  override def createOrUpdate(entity: Cell4d): Try[Cell4dId] =
    context.createOrUpdate[Cell4dId, Cell4d](entity)

  override def createOrUpdateAndRead(entity: Cell4d): Try[Cell4d] =
    context.createOrUpdateAndRead[Cell4dId, Cell4d](entity)

  override def update(entity: Cell4d): Try[Long] =
    context.update[Cell4dId, Cell4d](entity)

  override def updateAndRead(entity: Cell4d): Try[Cell4d] =
    context.updateAndRead[Cell4dId, Cell4d](entity)

  override def delete(id: Cell4dId): Try[Boolean] =
    context.delete[Cell4dId, Cell4d](id)

}
