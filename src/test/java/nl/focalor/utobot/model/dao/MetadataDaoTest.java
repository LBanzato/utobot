package nl.focalor.utobot.model.dao;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import nl.focalor.utobot.base.model.dao.MetadataDao;
import nl.focalor.utobot.config.TestConfig;
import nl.focalor.utobot.util.Version;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class MetadataDaoTest {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private MetadataDao dao;

	@Before
	public void init() {
		dao = new MetadataDao(jdbcTemplate);
	}

	@Test
	public void getUnknownVersion() {
		// Test
		Version version = dao.getSchemaVersion();

		// Verify
		assertEquals(version, new Version(0, 0, 0));
	}

	@Test
	public void getKnownVersion() {
		// Setup
		jdbcTemplate.update(
				"INSERT INTO schema_information(version_major, version_minor, version_patch) VALUES(0, 3, 1)",
				new HashMap<>());

		// Test
		Version version = dao.getSchemaVersion();

		// Verify
		assertEquals(version, new Version(0, 3, 1));
	}
}
