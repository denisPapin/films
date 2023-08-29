package com.dp.homework.films.repo;

import com.dp.homework.films.model.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmRepository extends GenericRepository<Film> {

    @Query(nativeQuery = true,
            value = """
          select distinct b.*
          from films b
          left join directors_films ba on b.id = ba.film_id
          join directors a on a.id = ba.director_id
          where b.film_title ilike '%' || coalesce(:film_title, '%') || '%'
          and cast(b.genre as char(2)) like coalesce(:genre,'%')
          and a.fio ilike '%' || :fio || '%'
          and b.is_deleted = false
               """)
    Page<Film> searchFilms(
            @Param(value = "genre") String genre,
            @Param(value = "film_title") String film_title,
            @Param(value = "fio") String fio,
            Pageable pageable);

    Page<Film> searchAllByTitleContainsIgnoreCaseAndIsDeletedFalse(String title, Pageable pageable);

}
