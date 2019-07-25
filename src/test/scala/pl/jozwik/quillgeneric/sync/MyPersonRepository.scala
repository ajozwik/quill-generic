package pl.jozwik.quillgeneric.sync

import pl.jozwik.quillgeneric.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.quillmacro.sync.Repository

trait MyPersonRepository extends Repository[PersonId, Person]