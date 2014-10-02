select
   m.id AS musician$id,
   m.name AS musician$name,
   a.id AS album$id,
   a.name AS album$name,
   m.id AS album$musician_id
from musician m
join album a
on m.id = a.musician_id