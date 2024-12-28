package stun.league.com.StunLeague.infra.data.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import stun.league.com.StunLeague.infra.entities.User;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User  u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String  email);
    Optional<User> findByName(String  name);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.name = :name")
    boolean checkIfNameExist(@Param("name") String name);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email")
    boolean existByEmail(@Param("email") String email);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.cpf = :cpf")
    boolean existsByCpf(@Param("cpf") String cpf);

    boolean existsByEmail(String email); // Adicione este método
}
