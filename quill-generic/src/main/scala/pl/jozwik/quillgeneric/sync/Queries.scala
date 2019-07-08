package pl.jozwik.quillgeneric.sync

import io.getquill.context.Context
import pl.jozwik.quillgeneric.quillmacro.QuillMacro

import scala.language.experimental.macros
import scala.util.Try

trait Queries {
  this: Context[_, _] =>
  def all[T]: Try[Seq[T]] = macro QuillMacro.all[T]

  def insertOrUpdate[K, T](entity: T): Try[K] = macro QuillMacro.insertOrUpdate[T]

  def create[K, T](entity: T): Try[K] = macro QuillMacro.create[T]

  def createAndGenerateId[K, T](entity: T): Try[K] = macro QuillMacro.createAndGenerateId[T]

  def update[T](entity: T): Try[Long] = macro QuillMacro.update[T]

  def read[K, T](id: K): Try[Option[T]] = macro QuillMacro.read[T]

  def updateById[T](
    filter: T => Boolean,
    action: Function[T, (Any, Any)],
    actions: Function[T, (Any, Any)]*): Try[Long] = macro QuillMacro.mergeByFilter[T]

  def deleteByFilter[T](filter: T => Boolean): Try[Boolean] = macro QuillMacro.deleteByFilter[T]
}
