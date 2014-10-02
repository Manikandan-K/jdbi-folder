select
    m.*,
    song_id AS song$songId,
    song_name AS song$songName,
    actor_id  AS actor$actorId,
    actor_name AS actor$actorName,
    director_id  AS director$directorId,
    director_name AS director$directorName
from movie m
left join song s   using (movie_id)
left join actor a  using (movie_id)
left join director using (movie_id)
where movie_id = 1