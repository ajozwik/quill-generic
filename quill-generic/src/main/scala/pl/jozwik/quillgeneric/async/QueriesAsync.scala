package pl.jozwik.quillgeneric.async

import io.getquill.context.async.AsyncContext
import pl.jozwik.quillgeneric.quillmacro.AllAsyncMacro

import scala.concurrent.{ ExecutionContext, Future }
import scala.language.experimental.macros

trait QueriesAsync {
  this: AsyncContext[_, _, _] =>
  def all[T](tblName: String)(implicit ex: ExecutionContext): Future[List[T]] = macro AllAsyncMacro.all[T]

  def insertOrUpdate[T](entity: T, filter: T => Boolean)(ex: ExecutionContext): Future[Unit] = macro AllAsyncMacro.insertOrUpdate[T]

  def create[T](entity: T)(implicit ex: ExecutionContext): Future[Long] = macro AllAsyncMacro.create[T]

  def merge[T](entity: T)(implicit ex: ExecutionContext): Future[Long] = macro AllAsyncMacro.merge[T]

  def mergeById[T](
    filter: T => Boolean,
    action: Function[T, (Any, Any)],
    actions: Function[T, (Any, Any)])(implicit ex: ExecutionContext): Future[Long] = macro AllAsyncMacro.mergeByFilter[T]

  def deleteByFilter[T](filter: (T) => Boolean)(implicit ex: ExecutionContext): Future[Long] = macro AllAsyncMacro.deleteByFilter[T]
}
