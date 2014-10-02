select
   m.*,
   a.id AS album$id,
   a.name AS album$name,
   m.id AS album$musician_id,
   s.song_id AS song$songId,
   s.song_name AS song$songName
from musician m
join album a on m.id = a.musician_id
left join song s on a.id = s.album_id
order by m.id, a.id, s.song_id