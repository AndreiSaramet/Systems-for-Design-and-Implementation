package ro.ubb.opera.core.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ro.ubb.opera.core.model.Opera;
import ro.ubb.opera.core.model.exceptions.ValidatorException;


import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class OperaRepository implements Repository<Integer, Opera> {
    @Autowired
    private JdbcOperations jdbcOperations;

    @Override
    public Optional<Opera> findOne(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("the id of the opera is null");
        }
        String sql = "SELECT * " +
                "FROM public.opera " +
                "WHERE id = ? ";
        List<Opera> result = this.jdbcOperations.query(sql, (rs, i) -> {
            String title = rs.getString("title");
            String language = rs.getString("language");
            Integer composerId = (int) rs.getLong("composerId");
            return new Opera(id, title, language, composerId);
        }, id);
        if (result.size() == 0)
            return Optional.empty();
        return Optional.of(result.get(0));

    }

    @Override
    public Iterable<Opera> findAll() {
        String sql = "SELECT * " +
                "FROM public.opera";
        return this.jdbcOperations.query(sql, (rs, i) -> {
            Integer id = rs.getInt("id");
            String title = rs.getString("title");
            String language = rs.getString("language");
            Integer composerId = rs.getInt("composerId");
            return new Opera(id, title, language, composerId);
        });
    }

    @Override
    public Optional<Opera> save(Opera opera) throws ValidatorException {
        if (opera == null) {
            throw new IllegalArgumentException("the opera is null");
        }
        String sql = "INSERT INTO public.opera (title, language, composerId) " +
                "VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcOperations.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, opera.getTitle());
            ps.setString(2, opera.getLanguage());
            ps.setInt(3, opera.getComposerId());
            return ps;
        }, keyHolder);
        Integer id = (Integer) keyHolder.getKeyList().get(0).get("id");
        return Optional.of(new Opera(id, opera.getTitle(), opera.getLanguage(), opera.getComposerId()));
    }

    @Override
    public Optional<Opera> delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("the id of the opera is null");
        }
        String sql = "DELETE " +
                "FROM public.opera " +
                "WHERE id = ?";
        Optional<Opera> optionalOpera = this.findOne(id);
        this.jdbcOperations.update(sql, id);
        return optionalOpera;
    }

    @Override
    public Optional<Opera> update(Opera opera) throws ValidatorException {
        if (opera == null) {
            throw new IllegalArgumentException("the opera is null");
        }
        String sql = "UPDATE public.opera " +
                "SET title = ?, language = ?, composerId = ? " +
                "WHERE id = ?";
        Optional<Opera> optionalOpera = this.findOne(opera.getId());
        this.jdbcOperations.update(sql, opera.getTitle(), opera.getLanguage(), opera.getComposerId(), opera.getId());
        return optionalOpera;
    }
}
