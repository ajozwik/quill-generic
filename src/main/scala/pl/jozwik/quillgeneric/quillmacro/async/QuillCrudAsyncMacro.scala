package pl.jozwik.quillgeneric.quillmacro.async

import scala.reflect.macros.whitebox.{ Context => MacroContext }

class QuillCrudAsyncMacro(val c: MacroContext) {

  import c.universe._

  def all[T](ex: Tree)(t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      run(quote {
        query[$t]
      })
    """

  def createOrUpdate[T](entity: Tree, generateId: Tree)(ex: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
        run(quote {
          query[$t].filter(_.id == lift(entity.id)).update(lift($entity))
        }).flatMap{result =>
          if (result == 0) {
              run(quote {
                query[$t].insert(lift($entity)).returning(_.id)
              })
          } else {
            concurrent.Future.successful(${entity}.id)
          }
        }
    """

  def create[T](entity: Tree)(ex: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
          run(quote {
            query[$t].insert(lift($entity)).returning(_.id)
          })
    """

  def createAndGenerateId[T](entity: Tree)(ex: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      run(quote {
        query[$t].insert(lift($entity)).returning(_.id)
      })
    """

  def merge[T](entity: Tree)(ex: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      run(quote {
        query[$t].update(lift($entity))
      })
    """

  def mergeByFilter[T](filter: Tree, action: Tree, actions: Tree*)(ex: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      run(quote {
       query[$t].filter($filter).update(action, actions:_*)
      })
    """

  def deleteByFilter[T](filter: Tree)(ex: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      run(quote {
        query[$t].filter($filter).delete
      }).map(_ > 0)
    """
}