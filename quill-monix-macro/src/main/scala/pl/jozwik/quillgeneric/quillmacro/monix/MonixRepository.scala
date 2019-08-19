package pl.jozwik.quillgeneric.quillmacro.monix

import pl.jozwik.quillgeneric.quillmacro.{ Repository, RepositoryWithGeneratedId, WithId }

trait MonixRepository[K, T <: WithId[K]] extends Repository[K, T] with WithMonix

trait MonixRepositoryWithGeneratedId[K, T <: WithId[K]] extends RepositoryWithGeneratedId[K, T] with WithMonix

trait MonixRepositoryLong[K, T <: WithId[K]] extends Repository[K, T] with WithMonix {
  type A = Long
}

trait MonixRepositoryWithGeneratedIdLong[K, T <: WithId[K]] extends RepositoryWithGeneratedId[K, T] with WithMonix {
  type A = Long
}

trait MonixRepositoryUnit[K, T <: WithId[K]] extends Repository[K, T] with WithMonix {
  type A = Unit
}

trait MonixRepositoryWithGeneratedIdUnit[K, T <: WithId[K]] extends RepositoryWithGeneratedId[K, T] with WithMonix {
  type A = Unit
}
