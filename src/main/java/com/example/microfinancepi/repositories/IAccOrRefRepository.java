package com.example.microfinancepi.repositories;

import com.example.microfinancepi.entities.AccOrRef;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAccOrRefRepository extends JpaRepository<AccOrRef, Integer> {
 /*   @Query("SELECT a FROM AccOrRef a JOIN a.request r JOIN r.user u WHERE u.id_user = :id_user")
    List<AccOrRef> find(@Param("id_user") Integer id_user);*/

}
