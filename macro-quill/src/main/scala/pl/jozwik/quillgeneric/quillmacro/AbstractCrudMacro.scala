package pl.jozwik.quillgeneric.quillmacro
import scala.reflect.macros.whitebox.{ Context => MacroContext }

abstract class AbstractCrudMacro {
  val c: MacroContext
  import c.universe._
  import pl.jozwik.quillgeneric.quillmacro.Keys._

  def callFilterOnIdTree[K: c.WeakTypeTag](id: Tree)(dSchema: c.Expr[_]): Tree =
    callFilterOnId[K](c.Expr[K](q"$id"))(dSchema)

  protected def callFilterOnId[K: c.WeakTypeTag](id: c.Expr[K])(dSchema: c.Expr[_]): Tree = {
    val t = weakTypeOf[K]

    t.baseClasses.find(c => compositeSet.contains(c.asClass.fullName)) match {
      case None =>
        q"$dSchema.filter(_.id == lift($id))"
      case Some(base) =>
        val query = q"$dSchema.filter(_.id.fk1 == lift($id.fk1)).filter(_.id.fk2 == lift($id.fk2))"
        base.fullName match {
          case `compositeKey4Name` =>
            q"$query.filter(_.id.fk3 == lift($id.fk3)).filter(_.id.fk4 == lift($id.fk4))"
          case `compositeKey3Name` =>
            q"$query.filter(_.id.fk3 == lift($id.fk3))"
          case `compositeKey2Name` =>
            query
          case x =>
            c.abort(NoPosition, s"$x not supported")

        }
    }
  }

  protected def callFilter[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_]): Tree = {
    val id = c.Expr[K](q"$entity.id")
    callFilterOnId[K](id)(dSchema)
  }

  def all[T: c.WeakTypeTag](dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      run($dSchema)
    """

  def createAndGenerateId[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      run($dSchema.insertValue($entity).returningGenerated(_.id))
    """

  def update[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_]): Tree = {
    val filter = callFilter[K, T](entity)(dSchema)
    q"""
      import ${c.prefix}._
      val q = $filter
      run(q.updateValue($entity))
    """
  }

  def delete[K: c.WeakTypeTag](id: c.Expr[K])(dSchema: c.Expr[_]): Tree = {
    val filter = callFilterOnId(id)(dSchema)
    q"""
      import ${c.prefix}._
      val q = $filter
      run(
         q.delete
      )
    """
  }

  def deleteByFilter(filter: Tree)(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      run(
         $dSchema.filter($filter).delete
      )
    """

  def deleteAll[T: c.WeakTypeTag](dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      run($dSchema.delete)
    """

  def searchByFilter[T: c.WeakTypeTag](filter: Tree)(offset: c.Expr[Int], limit: c.Expr[Int])(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      run(
        $dSchema.filter($filter).drop(lift($offset)).take(lift($limit))
      )
    """

  def count(filter: Tree)(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      run(
         $dSchema.filter($filter).size
      )
    """
}
