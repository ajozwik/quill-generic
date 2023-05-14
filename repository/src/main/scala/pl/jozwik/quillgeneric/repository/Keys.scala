package pl.jozwik.quillgeneric.repository

object Keys {
  val compositeKey2Name: String = classOf[CompositeKey2[_, _]].getName
  val compositeKey3Name: String = classOf[CompositeKey3[_, _, _]].getName
  val compositeKey4Name: String = classOf[CompositeKey4[_, _, _, _]].getName
  val compositeSet: Set[String] = Set(compositeKey2Name, compositeKey3Name, compositeKey4Name)
}
