package ro.ubb.opera.core.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ro.ubb.opera.core.model.Orchestra;
import ro.ubb.opera.core.model.exceptions.ValidatorException;


import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class OrchestraRepository implements Repository<Integer, Orchestra> {
    @Autowired
    private JdbcOperations jdbcOperations;

    @Override
    public Optional<Orchestra> findOne(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("the id of the orchestra is null");
        }
        String sql = "SELECT * " +
                "FROM public.orchestra " +
                "WHERE id = ?";
        List<Orchestra> result = this.jdbcOperations.query(sql, (rs, i) -> {
            String name = rs.getString("name");
            String conductor = rs.getString("conductor");
            return new Orchestra(id, name, conductor);
        }, id);
        if (result.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    @Override
    public Iterable<Orchestra> findAll() {
        String sql = "SELECT * " +
                "FROM public.orchestra";
        return this.jdbcOperations.query(sql, (rs, i) -> {
            Integer id = (int) rs.getLong("id");
            String name = rs.getString("name");
            String conductor = rs.getString("conductor");
            return new Orchestra(id, name, conductor);
        });
    }

    @Override
    public Optional<Orchestra> save(Orchestra orchestra) throws ValidatorException {
        if (orchestra == null) {
            throw new IllegalArgumentException("the orchestra is null");
        }
        String sql = "INSERT INTO public.orchestra (name, conductor) " +
                "VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcOperations.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, orchestra.getName());
            ps.setString(2, orchestra.getConductor());
            return ps;
        }, keyHolder);
        Integer id = (Integer) keyHolder.getKeyList().get(0).get("id");
        return Optional.of(new Orchestra(id, orchestra.getName(), orchestra.getConductor()));
    }

    @Override
    public Optional<Orchestra> delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("the id of the orchestra is null");
        }
        String sql = "DELETE " +
                "FROM public.orchestra " +
                "WHERE id = ?";
        Optional<Orchestra> optionalOrchestra = this.findOne(id);
        this.jdbcOperations.update(sql, id);
        return optionalOrchestra;
    }

    @Override
    public Optional<Orchestra> update(Orchestra orchestra) throws ValidatorException {
        if (orchestra == null) {
            throw new IllegalArgumentException("the orchestra is null");
        }
        String sql = "UPDATE public.orchestra " +
                "SET name = ?, conductor = ? " +
                "WHERE id = ?";
        Optional<Orchestra> optionalOrchestra = this.findOne(orchestra.getId());
        this.jdbcOperations.update(sql, orchestra.getName(), orchestra.getConductor(), orchestra.getId());
        return optionalOrchestra;
    }
}
