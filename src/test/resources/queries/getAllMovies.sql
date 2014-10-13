select
    movie_id AS movieId,
    movie_name AS name,
    song_id AS song$songId,
    song_name AS song$name
from movie
left join song
using ( movie_id )
order by movie_id