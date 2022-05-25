package ro.ubb.opera.server.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ro.ubb.opera.common.domain.Venue;
import ro.ubb.opera.common.domain.exceptions.ValidatorException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class VenueRepository implements Repository<Integer, Venue> {
    @Autowired
    private JdbcOperations jdbcOperations;

    @Override
    public Optional<Venue> findOne(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("the id of the venue is null");
        }
        String sql = "SELECT * " +
                "FROM public.venue " +
                "WHERE id = ?";
        List<Venue> result = this.jdbcOperations.query(sql, (rs, i) -> {
            Integer seatsNo = rs.getInt("numberOfSeats");
            Integer floor = rs.getInt("floor");
            return new Venue(id, seatsNo, floor);
        }, id);
        if (result.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    @Override
    public Iterable<Venue> findAll() {
        String sql = "SELECT * " +
                "FROM public.venue";
        return this.jdbcOperations.query(sql, (rs, i) -> {
            Integer id = (int) rs.getLong("id");
            Integer seatsNo = rs.getInt("numberOfSeats");
            Integer floor = rs.getInt("floor");
            return new Venue(id, seatsNo, floor);
        });
    }

    @Override
    public Optional<Venue> save(Venue venue) throws ValidatorException {
        if (venue == null) {
            throw new IllegalArgumentException("the venue is null");
        }
        String sql = "INSERT INTO public.venue (numberOfSeats, floor) " +
                "VALUES (? ,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcOperations.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, venue.getNumberOfSeats());
            ps.setInt(2, venue.getFloor());
            return ps;
        }, keyHolder);
        Integer id = (Integer) keyHolder.getKeyList().get(0).get("id");
        return Optional.of(new Venue(id, venue.getNumberOfSeats(), venue.getFloor()));

    }

    @Override
    public Optional<Venue> delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("the id of the venue is null");
        }
        String sql = "DELETE " +
                "FROM public.venue " +
                "WHERE id = ?";
        Optional<Venue> optionalVenue = this.findOne(id);
        this.jdbcOperations.update(sql, id);
        return optionalVenue;
    }

    @Override
    public Optional<Venue> update(Venue venue) throws ValidatorException {
        if (venue == null) {
            throw new IllegalArgumentException("the venue is null");
        }
        String sql = "UPDATE public.venue " +
                "SET numberOfSeats = ?, floor = ? " +
                "WHERE id = ?";
        Optional<Venue> optionalVenue = this.findOne(venue.getId());
        this.jdbcOperations.update(sql, venue.getNumberOfSeats(), venue.getFloor(), venue.getId());
        return optionalVenue;
    }
}
