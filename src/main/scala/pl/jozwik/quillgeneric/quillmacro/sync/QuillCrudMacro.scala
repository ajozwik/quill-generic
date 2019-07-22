package pl.jozwik.quillgeneric.quillmacro.sync

import scala.reflect.macros.whitebox.{ Context => MacroContext }

class QuillCrudMacro(val c: MacroContext) {

  import c.universe._

  def all[T](tableName: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
          dynamicQuerySchema[$t]($tableName)
        )
      }
    """

  def createOrUpdate[T](entity: Tree, generateId: Tree)(tableName: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
       transaction{
          val result = run(
            dynamicQuerySchema[$t]($tableName).filter(_.id == entity.id).updateValue($entity)
          )
          if (result == 0) {
              run(
                dynamicQuerySchema[$t]($tableName).insertValue($entity).returning(_.id)
              )
          } else {
            $entity.id
          }
        }
      }
    """

  def create[T](entity: Tree)(tableName: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
          run(
            dynamicQuerySchema[$t]($tableName).insertValue($entity)
          )
          $entity.id
       }
    """

  def createAndGenerateId[T](entity: Tree)(tableName: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
          run(
           dynamicQuerySchema[$t]($tableName).insertValue($entity).returningGenerated(_.id)
          )
       }
    """

  def update[T](entity: Tree)(tableName: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
            dynamicQuerySchema[$t]($tableName).updateValue($entity)
        )
      }
    """

  def read[T](id: Tree)(tableName: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
           dynamicQuerySchema[$t]($tableName).filter(_.id == lift($id))
        ).headOption
      }
    """

  def mergeByFilter[T](filter: Tree, action: Tree, actions: Tree*)(tableName: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
           dynamicQuerySchema[$t]($tableName).filter($filter).update(action, actions:_*)
        )
      }
    """

  def deleteByFilter[T](filter: Tree)(tableName: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
           dynamicQuerySchema[$t]($tableName).filter($filter).delete
        ) > 0
      }
    """
}