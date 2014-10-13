SELECT
  m.movie_id,
  m.movie_name AS name ,
  d.director_id AS director$director_id,
  d.director_name AS director$director_name,
  ad.id AS assistant$id,
  ad.name AS assistant$name
FROM
 movie m
JOIN director d using (movie_id)
LEFT JOIN assistant_director ad using (director_id)
