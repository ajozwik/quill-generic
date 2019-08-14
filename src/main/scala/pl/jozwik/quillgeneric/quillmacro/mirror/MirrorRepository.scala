package pl.jozwik.quillgeneric.quillmacro.mirror

import io.getquill.NamingStrategy
import io.getquill.idiom.Idiom
import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, WithId }

trait MirrorTypes[K, T <: WithId[K], D <: Idiom, N <: NamingStrategy] {
  protected val context: MirrorContextDateQuotes[D, N]
  type QueryMirror = context.QueryMirror[T]

  type ActionMirror = context.ActionMirror
}

trait MirrorRepository[K, T <: WithId[K], D <: Idiom, N <: NamingStrategy] extends BaseMirrorRepository[K, T] with MirrorTypes[K, T, D, N] {
  protected val context: MirrorContextDateQuotes[D, N]

  protected def dynamicSchema: context.DynamicEntityQuery[T]

  def create(entity: T): Create

  def createAndRead(entity: T): QueryMirror

  def createOrUpdate(entity: T): Create

  def createOrUpdateAndRead(entity: T): QueryMirror
}

trait MirrorRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], D <: Idiom, N <: NamingStrategy] extends MirrorRepository[K, T, D, N]

trait MirrorRepositoryWithGeneratedId[K, T <: WithId[K], D <: Idiom, N <: NamingStrategy] extends BaseMirrorRepository[K, T] with MirrorTypes[K, T, D, N] {
  def create(entity: T, generatedId: Boolean = true): Create

  def createAndRead(entity: T, generatedId: Boolean = true): ActionMirror

  def createOrUpdate(entity: T, generatedId: Boolean = true): Create

  def createOrUpdateAndRead(entity: T, generatedId: Boolean = true): ActionMirror
}

trait BaseMirrorRepository[K, T <: WithId[K]] {

  type Create = K

  type ActionMirror

  type QueryMirror

  def all: QueryMirror

  def read(id: K): QueryMirror

  def update(t: T): ActionMirror

  def updateAndRead(t: T): QueryMirror

  def delete(id: K): ActionMirror

}
