select * from messages
where user_id = 181496675 
and date  between '2015-05-02 00:00:00'
and date_add('2015-05-02 00:00:00', interval 1 day);