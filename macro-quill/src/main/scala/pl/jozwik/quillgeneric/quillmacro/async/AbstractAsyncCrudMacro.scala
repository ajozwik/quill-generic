package pl.jozwik.quillgeneric.quillmacro.async

import pl.jozwik.quillgeneric.quillmacro.FilterCrudMacro

import scala.concurrent.ExecutionContext
import scala.reflect.macros.whitebox.{ Context => MacroContext }
@SuppressWarnings(Array("org.wartremover.warts.Any"))
trait AbstractAsyncCrudMacro extends FilterCrudMacro {
  val c: MacroContext
  import c.universe._

  def all[T: c.WeakTypeTag](dSchema: c.Expr[_], ex: c.Expr[ExecutionContext]): Tree =
    q"""
      import ${c.prefix}._
      run($dSchema)
    """

  def createAndGenerateId[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_], ex: c.Expr[ExecutionContext]): Tree =
    q"""
      import ${c.prefix}._
      run($dSchema.insertValue($entity).returningGenerated(_.id))
    """

  def update[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_], ex: c.Expr[ExecutionContext]): Tree = {
    val filter = callFilter[K, T](entity)(dSchema)
    q"""
      import ${c.prefix}._
      val q = $filter
      run(q.updateValue($entity))
    """
  }

  def delete[K: c.WeakTypeTag](id: c.Expr[K])(dSchema: c.Expr[_], ex: c.Expr[ExecutionContext]): Tree = {
    val filter = callFilterOnId(id)(dSchema)
    q"""
      import ${c.prefix}._
      val q = $filter
      run(
         q.delete
      )
    """
  }

  def deleteByFilter(filter: Tree)(dSchema: c.Expr[_], ex: c.Expr[ExecutionContext]): Tree =
    q"""
      import ${c.prefix}._
      run(
         $dSchema.filter($filter).delete
      )
    """

  def deleteAll[T: c.WeakTypeTag](dSchema: c.Expr[_], ex: c.Expr[ExecutionContext]): Tree =
    q"""
      import ${c.prefix}._
      run($dSchema.delete)
    """

  def searchByFilter[T: c.WeakTypeTag](filter: Tree)(offset: c.Expr[Int], limit: c.Expr[Int])(dSchema: c.Expr[_], ex: c.Expr[ExecutionContext]): Tree =
    q"""
      import ${c.prefix}._
      run(
        $dSchema.filter($filter).drop(lift($offset)).take(lift($limit))
      )
    """

  def count(filter: Tree)(dSchema: c.Expr[_], ex: c.Expr[ExecutionContext]): Tree =
    q"""
      import ${c.prefix}._
      run(
         $dSchema.filter($filter).size
      )
    """
}
