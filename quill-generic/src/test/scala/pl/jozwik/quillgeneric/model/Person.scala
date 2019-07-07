package pl.jozwik.quillgeneric.model

import java.time.LocalDate

final case class Person(id: Int, firstName: String, lastName: String, birthDate: LocalDate)