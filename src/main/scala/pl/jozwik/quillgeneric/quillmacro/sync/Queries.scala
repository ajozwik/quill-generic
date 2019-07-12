package pl.jozwik.quillgeneric.quillmacro.sync

import io.getquill.context.Context
import pl.jozwik.quillgeneric.quillmacro.WithId

import scala.language.experimental.macros
import scala.util.Try

trait Queries {
  this: Context[_, _] =>
  def all[T](implicit tableName: String): Try[Seq[T]] = macro QuillCrudMacro.all[T]

  def createOrUpdate[K, T <: WithId[K]](entity: T, generateId: Boolean)(implicit tableName: String): Try[K] = macro QuillCrudMacro.createOrUpdate[T]

  def create[K, T <: WithId[K]](entity: T)(implicit tableName: String): Try[K] = macro QuillCrudMacro.create[T]

  def createAndGenerateId[K, T <: WithId[K]](entity: T)(implicit tableName: String): Try[K] = macro QuillCrudMacro.createAndGenerateId[T]

  def update[T](entity: T)(implicit tableName: String): Try[Long] = macro QuillCrudMacro.update[T]

  def read[K, T <: WithId[K]](id: K)(implicit tableName: String): Try[Option[T]] = macro QuillCrudMacro.read[T]

  def updateByFilter[T](
    filter: T => Boolean,
    action: Function[T, (Any, Any)],
    actions: Function[T, (Any, Any)]*)(implicit tableName: String): Try[Long] = macro QuillCrudMacro.mergeByFilter[T]

  def deleteByFilter[T](filter: T => Boolean)(implicit tableName: String): Try[Boolean] = macro QuillCrudMacro.deleteByFilter[T]
}
