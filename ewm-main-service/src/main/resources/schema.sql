DROP TABLE IF EXISTS
    locations,
    users,
    categories,
    events,
    requests,
    compilations,
    events_compilations
    CASCADE;

CREATE TABLE IF NOT EXISTS locations(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat float NOT NULL,
    lon float NOT NULL);

CREATE TABLE IF NOT EXISTS users(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email varchar(100) NOT NULL UNIQUE,
    name varchar(100));

CREATE TABLE IF NOT EXISTS categories(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(100));

CREATE TABLE IF NOT EXISTS events(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation varchar(1000) NOT NULL,
    category BIGINT REFERENCES categories(id) ON DELETE CASCADE,
    confirmed_request int,
    event_date timestamp NOT NULL,
    initiator BIGINT REFERENCES users(id) ON DELETE CASCADE,
    paid boolean NOT NULL,
    title varchar(200) NOT NULL,
    views BIGINT,
    description text NOT NULL,
    created_on timestamp,
    location BIGINT REFERENCES locations(id) ON DELETE CASCADE,
    participant_limit int,
    published_on timestamp,
    request_moderation boolean,
    state varchar(100)
);

CREATE TABLE IF NOT EXISTS requests(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created timestamp NOT NULL,
    event BIGINT REFERENCES events(id) ON DELETE CASCADE,
    requester BIGINT REFERENCES users(id) ON DELETE CASCADE,
    status varchar (100)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title varchar(100) NOT NULL UNIQUE,
    pinned boolean NOT NULL
    );

CREATE TABLE IF NOT EXISTS events_compilations
(
    event_id BIGINT REFERENCES events(id) ON DELETE CASCADE,
    compilation_id BIGINT REFERENCES compilations(id) ON DELETE CASCADE,
    primary key (event_id, compilation_id),
    constraint key_unique UNIQUE (event_id, compilation_id)
);