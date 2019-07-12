package pl.jozwik.quillgeneric.quillmacro.async

import io.getquill.context.async.AsyncContext

import scala.concurrent.{ ExecutionContext, Future }
import scala.language.experimental.macros

trait QueriesAsync {
  this: AsyncContext[_, _, _] =>
  def all[T](implicit ex: ExecutionContext): Future[List[T]] = macro QuillCrudAsyncMacro.all[T]

  def create[K, T](entity: T)(implicit ex: ExecutionContext): Future[K] = macro QuillCrudAsyncMacro.create[T]

  def createAndGenerateId[K, T](entity: T)(implicit ex: ExecutionContext): Future[K] = macro QuillCrudAsyncMacro.createAndGenerateId[T]

  def createOrUpdate[K, T](
    entity: T,
    generateId: Boolean)(implicit ex: ExecutionContext): Future[K] = macro QuillCrudAsyncMacro.createOrUpdate[T]

  def merge[T](entity: T)(implicit ex: ExecutionContext): Future[Long] = macro QuillCrudAsyncMacro.merge[T]

  def mergeById[T](
    filter: T => Boolean,
    action: Function[T, (Any, Any)],
    actions: Function[T, (Any, Any)]*)(implicit ex: ExecutionContext): Future[Long] = macro QuillCrudAsyncMacro.mergeByFilter[T]

  def deleteByFilter[T](filter: (T) => Boolean)(implicit ex: ExecutionContext): Future[Boolean] = macro QuillCrudAsyncMacro.deleteByFilter[T]
}
