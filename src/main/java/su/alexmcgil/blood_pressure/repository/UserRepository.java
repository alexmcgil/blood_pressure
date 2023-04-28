package su.alexmcgil.blood_pressure.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import su.alexmcgil.blood_pressure.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    @Query("select u from User u inner join Pressure p where u.telegramID = ?1")
    Optional<User> getRecords(Long telegramID);

}
