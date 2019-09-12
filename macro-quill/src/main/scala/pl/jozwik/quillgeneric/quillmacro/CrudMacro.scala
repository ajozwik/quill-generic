package pl.jozwik.quillgeneric.quillmacro

import scala.reflect.macros.whitebox.{ Context => MacroContext }

class CrudMacro(val c: MacroContext) extends AbstractCrudMacro {

  import c.universe._

  def createAndGenerateIdOrUpdate[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_]): Tree = {
    val filter = callFilter[K, T](entity)(dSchema)
    q"""
      import ${c.prefix}._
      val id = $entity.id
      val q = $filter
      val result = run(
        q.updateValue($entity)
      )
      if (result == 0) {
        run($dSchema.insertValue($entity).returningGenerated(_.id))
      } else {
        id
      }
    """
  }

  def createWithGenerateIdOrUpdateAndRead[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_]): Tree = {
    val filter = callFilter[K, T](entity)(dSchema)
    q"""
      import ${c.prefix}._
      val id = $entity.id
      val q = $filter
      val result = run(
        q.updateValue($entity)
      )
      val newId =
        if (result == 0) {
          run($dSchema.insertValue($entity).returningGenerated(_.id))
        } else {
          id
        }
      run($dSchema.filter(_.id == lift(newId)))
      .headOption
      .getOrElse(throw new NoSuchElementException(s"$$newId"))
    """
  }

  def createWithGenerateIdAndRead[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      val newId = run($dSchema.insertValue($entity).returningGenerated(_.id))
      val q = $dSchema.filter(_.id == lift(newId))
      run(q)
      .headOption
      .getOrElse(throw new NoSuchElementException(s"$$newId"))
    """

  def createOrUpdate[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_]): Tree = {
    val filter = callFilter[K, T](entity)(dSchema)
    q"""
      import ${c.prefix}._
      val id = $entity.id
      val q = $filter
      val result = run(q.updateValue($entity))
      if(result == 0){
          run($dSchema.insertValue($entity))
      }
      id
    """
  }

  def createOrUpdateAndRead[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_]): Tree = {
    val filter = callFilter[K, T](entity)(dSchema)
    q"""
      import ${c.prefix}._
      val id = $entity.id
      val q = $filter
      val result = run(
          q.updateValue($entity)
       )
       if(result == 0){
         run($dSchema.insertValue($entity))
       }
       run(q)
       .headOption
       .getOrElse(throw new NoSuchElementException(s"$$id"))
    """
  }

  def create[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      run(
        $dSchema.insertValue($entity)
      )
      $entity.id
    """

  def createAndRead[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_]): Tree = {
    val filter = callFilter[K, T](entity)(dSchema)
    q"""
      import ${c.prefix}._
      val id = $entity.id
      run($dSchema.insertValue($entity))
      val q = $filter
      run(q)
      .headOption
      .getOrElse(throw new NoSuchElementException(s"$$id"))
    """
  }

  def updateAndRead[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_]): Tree = {
    val filter = callFilter[K, T](entity)(dSchema)
    q"""
      import ${c.prefix}._
      val q = $filter
      run(q.updateValue($entity))
      run(q)
      .headOption
      .getOrElse{
        val id = $entity.id
        throw new NoSuchElementException(s"$$id")
       }
    """
  }

  def read[K: c.WeakTypeTag, T: c.WeakTypeTag](id: c.Expr[K])(dSchema: c.Expr[_]): Tree = {
    val filter = callFilterOnId[K](id)(dSchema)
    q"""
      import ${c.prefix}._
      val q = $filter
      run(q)
      .headOption
    """
  }

  def readUnsafe[K: c.WeakTypeTag, T: c.WeakTypeTag](id: c.Expr[K])(dSchema: c.Expr[_]): Tree = {
    val filter = callFilterOnId[K](id)(dSchema)
    q"""
      import ${c.prefix}._
      val q = $filter
      run(q)
      .headOption
      .getOrElse(throw new NoSuchElementException(s"$$id"))
    """
  }

}
