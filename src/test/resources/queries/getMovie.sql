select
    m.movie_id,
    m.movie_name AS name,
    m.ratings,
    song_id AS song$songId,
    song_name AS song$name,
    actor_id  AS actor$actorId,
    actor_name AS actor$actorName,
    director_id  AS director$directorId,
    director_name AS director$directorName
from movie m
left join song s   using (movie_id)
left join actor a  using (movie_id)
left join director using (movie_id)
where movie_id = :movieId