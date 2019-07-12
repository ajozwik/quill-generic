package pl.jozwik.quillgeneric.quillmacro.sync

import io.getquill.context.Context
import pl.jozwik.quillgeneric.quillmacro.WithId

import scala.language.experimental.macros
import scala.util.Try

trait Queries {
  this: Context[_, _] =>
  def all[T]: Try[Seq[T]] = macro QuillCrudMacro.all[T]

  def createOrUpdate[K, T](entity: T, generateId: Boolean): Try[K] = macro QuillCrudMacro.createOrUpdate[T]

  def create[K, T](entity: T): Try[K] = macro QuillCrudMacro.create[T]

  def createAndGenerateId[K, T](entity: T): Try[K] = macro QuillCrudMacro.createAndGenerateId[K, T]

  def update[T](entity: T): Try[Long] = macro QuillCrudMacro.update[T]

  def read[K, T](id: K): Try[Option[T]] = macro QuillCrudMacro.read[T]

  def updateByFilter[T](
    filter: T => Boolean,
    action: Function[T, (Any, Any)],
    actions: Function[T, (Any, Any)]*): Try[Long] = macro QuillCrudMacro.mergeByFilter[T]

  def deleteByFilter[T](filter: T => Boolean): Try[Boolean] = macro QuillCrudMacro.deleteByFilter[T]
}
