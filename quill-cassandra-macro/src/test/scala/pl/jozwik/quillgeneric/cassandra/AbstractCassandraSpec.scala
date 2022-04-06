package pl.jozwik.quillgeneric.cassandra

import org.cassandraunit.CQLDataLoader
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet
import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.scalatest.BeforeAndAfterAll
import pl.jozwik.quillgeneric.AbstractSpec
import pl.jozwik.quillgeneric.cassandra.model.{ Address, AddressId }

trait AbstractCassandraSpec extends AbstractSpec with BeforeAndAfterAll {
  sys.props.put("quill.binds.log", true.toString)
  protected def createAddress(id: AddressId) = Address(id, "country", "city")

  protected lazy val session = {
    EmbeddedCassandraServerHelper.startEmbeddedCassandra()
    EmbeddedCassandraServerHelper.getSession()
  }

  protected val keySpace = "demo"

  override def beforeAll(): Unit = {
    val dataLoader = new CQLDataLoader(session)
    dataLoader.load(new ClassPathCQLDataSet("scripts/create.cql", keySpace))
  }

  override protected def afterAll(): Unit = {
    import org.cassandraunit.utils.EmbeddedCassandraServerHelper
    EmbeddedCassandraServerHelper.cleanEmbeddedCassandra()
    super.afterAll()
  }

}
