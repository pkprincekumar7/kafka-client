create table event(
    id VARCHAR(20) NOT NULL,
    source VARCHAR(20),
    --createdAT timestamp default 'now',
    event_type VARCHAR(20),
    format_version VARCHAR(20),
    event_data VARCHAR(2000)
);



