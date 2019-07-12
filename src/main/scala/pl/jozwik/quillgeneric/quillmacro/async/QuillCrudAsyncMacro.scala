package pl.jozwik.quillgeneric.quillmacro.async

import scala.reflect.macros.whitebox.{ Context => MacroContext }

class QuillCrudAsyncMacro(val c: MacroContext) {

  import c.universe._

  def all[T](tableName: Tree, ex: Tree)(t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      run(dynamicQuerySchema[$t]($tableName))
    """

  def createOrUpdate[T](entity: Tree, generateId: Tree)(tableName: Tree, ex: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
        run(
          dynamicQuerySchema[$t]($tableName).filter(_.id == entity.id).updateValue($entity)
        ).flatMap{result =>
          if (result == 0) {
              run(
                dynamicQuerySchema[$t]($tableName).insertValue($entity).returning(_.id)
              )
          } else {
            concurrent.Future.successful(${entity}.id)
          }
        }
    """

  def create[T](entity: Tree)(tableName: Tree, ex: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
          run(
             dynamicQuerySchema[$t]($tableName).insertValue($entity).returning(_.id)
          )
    """

  def createAndGenerateId[T](entity: Tree)(tableName: Tree, ex: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      run(
        dynamicQuerySchema[$t]($tableName).insertValue($entity).returning(_.id)
      )
    """

  def merge[T](entity: Tree)(tableName: Tree, ex: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      run(
        dynamicQuerySchema[$t]($tableName).updateValue($entity)
      )
    """

  def mergeByFilter[T](filter: Tree, action: Tree, actions: Tree*)(tableName: Tree, ex: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      run(
       dynamicQuerySchema[$t]($tableName).filter($filter).update(action, actions:_*)
      )
    """

  def read[T](id: Tree)(tableName: Tree, ex: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      run(
          dynamicQuerySchema[$t]($tableName).filter(_.id == ${id})
      ).map(_.headOption)
    """

  def deleteByFilter[T](filter: Tree)(tableName: Tree, ex: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      run(
        dynamicQuerySchema[$t]($tableName).filter($filter).delete
      ).map(_ > 0)
    """
}