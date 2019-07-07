package pl.jozwik.quillgeneric.sync

import io.getquill.context.Context
import pl.jozwik.quillgeneric.quillmacro.QuillMacro

import scala.language.experimental.macros

trait Queries {
  this: Context[_, _] =>
  def all[T]: Seq[T] = macro QuillMacro.all[T]

  def insertOrUpdate[T](entity: T, filter: T => Boolean): Long = macro QuillMacro.insertOrUpdate[T]

  def create[T](entity: T): Long = macro QuillMacro.create[T]

  def merge[T](entity: T): Long = macro QuillMacro.merge[T]

  def mergeById[T](
    filter: T => Boolean,
    action: Function[T, (Any, Any)],
    actions: Function[T, (Any, Any)]*): Long = macro QuillMacro.mergeByFilter[T]

  def deleteByFilter[T](filter: (T) => Boolean): Long = macro QuillMacro.deleteByFilter[T]
}
