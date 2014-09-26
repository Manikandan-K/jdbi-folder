select
    m.*,
    song_id AS song$songId,
    song_name AS song$songName
from movie m
left join song s
using ( movie_id )
where movie_id = :movieId