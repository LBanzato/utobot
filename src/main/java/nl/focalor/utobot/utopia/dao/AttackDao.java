package nl.focalor.utobot.utopia.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.focalor.utobot.utopia.model.Attack;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AttackDao implements IAttackDao {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final AttackRowMapper mapper = new AttackRowMapper();

	@Autowired
	public AttackDao(NamedParameterJdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	@Transactional(readOnly = true)
	public Attack get(long id) {
		try {
			Map<String, Long> params = Collections.singletonMap("id", id);
			return jdbcTemplate.queryForObject("SELECT * FROM attacks WHERE id=:id", params, mapper);
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	@Override
	@Transactional
	public void create(Attack attack) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("person", attack.getPerson());
		params.addValue("personId", attack.getPersonId());
		params.addValue("returnDate", attack.getReturnDate());

		jdbcTemplate.update(
				"INSERT INTO attacks(person, personId, returnDate) VALUES (:person, :personId, :returnDate)", params,
				keyHolder);
		attack.setId(keyHolder.getKey().longValue());
	}

	@Override
	@Transactional
	public void delete(long id) {
		Map<String, Long> params = Collections.singletonMap("id", id);
		jdbcTemplate.update("DELETE FROM attacks WHERE id = :id", params);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Attack> find(Long personId, String person) {
		List<String> whereClause = new ArrayList<>();
		Map<String, Object> params = new HashMap<>();

		if (personId != null) {
			whereClause.add("personId = :personId");
			params.put("personId", personId);
		}
		if (!StringUtils.isEmpty(person)) {
			whereClause.add("person = :person");
			params.put("person", person);
		}

		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM attacks ");
		if (!whereClause.isEmpty()) {
			query.append(" WHERE ");
			query.append(StringUtils.join(whereClause, " AND "));
		}
		return jdbcTemplate.query(query.toString(), params, mapper);
	}

	private static class AttackRowMapper implements RowMapper<Attack> {

		@Override
		public Attack mapRow(ResultSet rs, int rowNum) throws SQLException {
			Attack result = new Attack();
			result.setId(rs.getLong("id"));
			result.setPerson(rs.getString("person"));
			result.setPersonId(rs.getLong("personId"));
			if (rs.wasNull()) {
				result.setPersonId(null);
			}
			result.setReturnDate(rs.getTimestamp("returnDate"));
			return result;
		}
	}

}
