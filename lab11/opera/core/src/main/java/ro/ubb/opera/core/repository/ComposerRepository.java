package ro.ubb.opera.core.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ro.ubb.opera.core.model.Composer;
import ro.ubb.opera.core.model.exceptions.ValidatorException;


import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class ComposerRepository implements Repository<Integer, Composer> {
    @Autowired
    private JdbcOperations jdbcOperations;

    @Override
    public Optional<Composer> findOne(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("the id of the composer is null");
        }
        String sql = "SELECT * " +
                "FROM public.composer " +
                "WHERE id = ?";
        List<Composer> result = this.jdbcOperations.query(sql, (rs, i) -> {
            String name = rs.getString("name");
            String nationality = rs.getString("nationality");
            String musicalPeriod = rs.getString("musicalPeriod");
            return new Composer(id, name, nationality, musicalPeriod);
        }, id);
        if (result.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    @Override
    public Iterable<Composer> findAll() {
        String sql = "SELECT * " +
                "FROM public.composer";
        return this.jdbcOperations.query(sql, (rs, i) -> {
            Integer id = (int) rs.getLong("id");
            String name = rs.getString("name");
            String nationality = rs.getString("nationality");
            String musicalPeriod = rs.getString("musicalPeriod");
            return new Composer(id, name, nationality, musicalPeriod);
        });
    }

    @Override
    public Optional<Composer> save(Composer composer) {
        if (composer == null) {
            throw new IllegalArgumentException("the composer is null");
        }
        String sql = "INSERT INTO public.composer (name, nationality, musicalPeriod) " +
                "VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcOperations.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, composer.getName());
            ps.setString(2, composer.getNationality());
            ps.setString(3, composer.getMusicalPeriod());
            return ps;
        }, keyHolder);
        Integer id = (Integer) keyHolder.getKeyList().get(0).get("id");
        return Optional.of(new Composer(id, composer.getName(), composer.getNationality(), composer.getMusicalPeriod()));
    }

    @Override
    public Optional<Composer> delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("the id of the composer is null");
        }
        String sql = "DELETE " +
                "FROM public.composer " +
                "WHERE id = ?";
        Optional<Composer> optionalComposer = this.findOne(id);
        this.jdbcOperations.update(sql, id);
        return optionalComposer;
    }

    @Override
    public Optional<Composer> update(Composer composer) throws ValidatorException {
        if (composer == null) {
            throw new IllegalArgumentException("the composer is null");
        }
        String sql = "UPDATE public.composer " +
                "SET name = ?, nationality = ?, musicalPeriod = ? " +
                "WHERE id = ?";
        Optional<Composer> optionalComposer = this.findOne(composer.getId());
        this.jdbcOperations.update(sql, composer.getName(), composer.getNationality(), composer.getMusicalPeriod(), composer.getId());
        return optionalComposer;
    }
}
