package pl.jozwik.quillgeneric.monad

class QuillCrudJdbcSpec
  extends AddressSuite
  with PersonRepositoryNotGeneratedIdSuite
  with PersonCustomRepositorySuite
  with PersonRepositorySuite
  with ConfigurationRepositorySuite
  with SaleRepositorySuite
  with Cell4dSuite
