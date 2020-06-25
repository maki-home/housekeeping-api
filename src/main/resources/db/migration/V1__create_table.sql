CREATE TABLE IF NOT EXISTS mission
(
    mission_id SERIAL PRIMARY KEY,
    place      VARCHAR(64)              NOT NULL,
    cycle      INT                      NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS mission_event
(
    mission_id INT                      NOT NULL,
    date       DATE                     NOT NULL,
    username   VARCHAR(64)              NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    PRIMARY KEY (mission_id, date),
    FOREIGN KEY (mission_id) REFERENCES mission (mission_id) ON DELETE CASCADE
);