package ro.ubb.opera.core.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ro.ubb.opera.core.model.Rehearsal;
import ro.ubb.opera.core.model.exceptions.ValidatorException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class RehearsalRepository implements Repository<Integer, Rehearsal> {
    @Autowired
    private JdbcOperations jdbcOperations;

    @Override
    public Optional<Rehearsal> findOne(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("the id of the rehearsal is null");
        }
        String sql = "SELECT * " +
                "FROM public.rehearsal " +
                "WHERE id = ?";
        List<Rehearsal> result = this.jdbcOperations.query(sql, (rs, i) -> {
            Integer orchestraId = rs.getInt("orchestraId");
            Integer venueId = rs.getInt("venueId");
            return new Rehearsal(id, orchestraId, venueId);
        }, id);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    @Override
    public Iterable<Rehearsal> findAll() {
        String sql = "SELECT * " +
                "FROM public.rehearsal";
        return this.jdbcOperations.query(sql, (rs, i) -> {
            Integer id = rs.getInt("id");
            Integer orchestraId = rs.getInt("orchestraId");
            Integer venueId = rs.getInt("venueId");
            return new Rehearsal(id, orchestraId, venueId);
        });
    }

    @Override
    public Optional<Rehearsal> save(Rehearsal rehearsal) throws ValidatorException {
        if (rehearsal == null) {
            throw new IllegalArgumentException("the rehearsal is null");
        }
        String sql = "INSERT INTO public.rehearsal (orchestraId, venueId) " +
                "VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcOperations.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, rehearsal.getOrchestraId());
            ps.setInt(2, rehearsal.getVenueId());
            return ps;
        }, keyHolder);
        Integer id = (Integer) keyHolder.getKeyList().get(0).get("id");
        return Optional.of(new Rehearsal(id, rehearsal.getOrchestraId(), rehearsal.getVenueId()));
    }

    @Override
    public Optional<Rehearsal> delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("the id of the rehearsal is null");
        }
        String sql = "DELETE " +
                "FROM public.rehearsal " +
                "WHERE id = ?";
        Optional<Rehearsal> optionalRehearsal = this.findOne(id);
        this.jdbcOperations.update(sql, id);
        return optionalRehearsal;
    }

    @Override
    public Optional<Rehearsal> update(Rehearsal rehearsal) throws ValidatorException {
        if (rehearsal == null) {
            throw new IllegalArgumentException("the rehearsal is null");
        }
        String sql = "UPDATE public.rehearsal " +
                "SET orchestraId = ?, venueId = ? " +
                "WHERE id = ?";
        Optional<Rehearsal> optionalRehearsal = this.findOne(rehearsal.getId());
        this.jdbcOperations.update(sql, rehearsal.getOrchestraId(), rehearsal.getVenueId(), rehearsal.getId());
        return optionalRehearsal;
    }
}
