select
    movie_id AS movieId,
    movie_name AS movieName,
    song_id AS song$songId,
    song_name AS song$songName
from movie
left join song
using ( movie_id )
where movie_id = :movieId