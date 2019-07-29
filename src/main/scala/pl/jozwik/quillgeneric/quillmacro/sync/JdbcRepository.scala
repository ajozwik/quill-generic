package pl.jozwik.quillgeneric.quillmacro.sync

import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.quillmacro.WithId

trait JdbcRepositoryWithGeneratedId[K, T <: WithId[K], Dialect <: SqlIdiom, Naming <: NamingStrategy]
  extends RepositoryWithGeneratedId[K, T] {
  protected val context: JdbcContext[Dialect, Naming] with QuillCrudWithContext

  protected def dynamicSchema: context.DynamicEntityQuery[T]
}

trait JdbcRepository[K, T <: WithId[K], Dialect <: SqlIdiom, Naming <: NamingStrategy] extends Repository[K, T] {
  protected val context: JdbcContext[Dialect, Naming] with QuillCrudWithContext

  protected def dynamicSchema: context.DynamicEntityQuery[T]
}
