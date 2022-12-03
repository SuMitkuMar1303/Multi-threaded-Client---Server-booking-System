CREATE TABLE trains(
    train_num INTEGER NOT NULL,
    doj DATE NOT NULL,
    AC_couch INTEGER,
    SL_couch INTEGER,
    AC_seat_booked INTEGER,
    SL_seat_booked INTEGER,

    PRIMARY KEY(train_num,doj)
);


CREATE TABLE tickets(
    pnr TEXT,
    train_num INTEGER NOT NULL,
    doj DATE NOT NULL,
    pname text,
    berth_type text,
    berth_num INTEGER NOT NULL,
    couch_num TEXT,
    PRIMARY KEY(pnr),
    FOREIGN KEY(train_num,doj) REFERENCES trains(train_num,doj)
);

-- --------------------------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION check_train(
    train INTEGER,
    dat DATE,
    seat INTEGER,
    seat_type TEXT
)

RETURNS text as $$
DECLARE
count integer default 0;
total INTEGER default 0;
tseat text;
max integer default 0;
rur record ;
cur CURSOR for (select * from trains as t where t.train_num = train and t.doj = dat FOR UPDATE );
rur2 record;
result text;
BEGIN
open cur;
loop 
fetch cur into rur;
exit when not found;
rur2:=rur;
count := count + 1;

end loop;


close cur;
if count = 0 THEN
    result := 'Train not avaiable for the given combinations';
    RETURN result;
end if;

if seat_type='SL' THEN
    max:=rur2.SL_couch*24;
    IF max - rur2.SL_seat_booked >=seat THEN
        total:= rur2.SL_seat_booked + rur2.AC_seat_booked;
        -- tseat  := cast(total as char(6));
        result := concat('YES ', cast(total as char(6)),' ' ,cast(rur2.SL_seat_booked as char(6)));
        UPDATE trains
        SET SL_seat_booked = rur2.SL_seat_booked + seat
        WHERE train_num = train and doj =  dat;
        RETURN result;
    end if;
    RETURN 'NO';
end if;

if seat_type='AC' THEN
    max:=rur2.AC_couch*18;
    IF max - rur2.AC_seat_booked >=seat THEN
        total:= rur2.SL_seat_booked + rur2.AC_seat_booked;
        -- tseat  := cast(total as char(6));
        result := concat('YES ', cast(total as char(6)),' ',cast(rur2.AC_seat_booked as char(6)));
        UPDATE trains
        SET AC_seat_booked = rur2.AC_seat_booked + seat
        WHERE train_num = train and doj =  dat;
        RETURN result;
    end if;
    RETURN 'NO';
end if;
RETURN 'Type dekh bhai';
end;$$ language 'plpgsql';



-- --------------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION add_train(
    train INTEGER,
    dat DATE,
    AC INTEGER,
    SL INTEGER
)
RETURNS text as $$
DECLARE
count integer default 0;
rur record ;
cur2 CURSOR for (select * from trains as t where t.train_num = train and t.doj = dat );
result TEXT;
BEGIN
open cur2;
loop
fetch cur2 into rur;
exit when not found;
count:= count + 1;
end loop;

if count <> 0 then 
    result := 'Train in alredy there';
    RETURN result;
END IF;
INSERT INTO trains(train_num,doj,AC_couch,SL_couch,AC_seat_booked,SL_seat_booked)
VALUES(train,dat,AC,SL,0,0);
CLOSE cur2;
RETURN 'DONE';
end; $$ language 'plpgsql';

-- ----------------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION book_ticket(
    tpnr TEXT ,
    ttrain_num INTEGER ,
    tdoj DATE ,
    tpname text,
    tberth_type text,
    tberth_num INTEGER,
    tcouch_num TEXT
)
RETURNS TEXT AS $$
BEGIN
INSERT INTO tickets(pnr,train_num,doj,pname,berth_type,berth_num,couch_num)
VALUES(tpnr,ttrain_num,tdoj,tpname,tberth_type,tberth_num,tcouch_num);
return 'DONE';
END; $$ LANGUAGE 'plpgsql';


-- ----------------------------------------------------------------------------------------------------------------------------------







select check_train('400','2022-11-17',10,'SL');
select add_train(6773,'2023-05-09',5,5);