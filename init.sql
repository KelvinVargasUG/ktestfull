CREATE TABLE "user" (
    id_user SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(50),
    created_by VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    updated_at TIMESTAMP
);

CREATE TABLE rol (
    id_rol SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    status VARCHAR(50),
    created_by VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    updated_at TIMESTAMP
);

CREATE TABLE user_rol (
    id_user_rol SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    rol_id INTEGER NOT NULL,
    status VARCHAR(50),
    created_by VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    updated_at TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "user"(id_user) ON DELETE CASCADE,
    CONSTRAINT fk_rol FOREIGN KEY (rol_id) REFERENCES rol(id_rol) ON DELETE CASCADE
);

CREATE TABLE project (
    id_project SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50),
    expires_at TIMESTAMP,
    created_by VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    updated_at TIMESTAMP,
    CONSTRAINT fk_project_user FOREIGN KEY (user_id) REFERENCES "user"(id_user) ON DELETE CASCADE
);

CREATE TABLE task (
    id_task SERIAL PRIMARY KEY,
    project_id INTEGER NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50),
    created_by VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    updated_at TIMESTAMP,
    CONSTRAINT fk_task_project FOREIGN KEY (project_id) REFERENCES project(id_project) ON DELETE CASCADE
);

CREATE TABLE user_task (
    id_user_task SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    task_id INTEGER NOT NULL,
    status VARCHAR(50),
    created_by VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    updated_at TIMESTAMP,
    CONSTRAINT fk_ut_user FOREIGN KEY (user_id) REFERENCES "user"(id_user) ON DELETE CASCADE,
    CONSTRAINT fk_ut_task FOREIGN KEY (task_id) REFERENCES task(id_task) ON DELETE CASCADE
);

CREATE INDEX idx_user_username_status ON "user" (username, status);

CREATE INDEX idx_user_rol_userid_status ON user_rol (user_id, status);
CREATE INDEX idx_user_rol_rolid_status ON user_rol (rol_id, status);

CREATE INDEX idx_project_userid_status ON project (user_id, status);

CREATE INDEX idx_task_projectid_status ON task (project_id, status);

CREATE INDEX idx_usertask_userid_status ON user_task (user_id, status);
CREATE INDEX idx_usertask_taskid_status ON user_task (task_id, status);

INSERT INTO rol (name, description, status, created_by, created_at)
SELECT 'ADMIN', 'Rol de administrador del sistema', 'ACTIVE', 'system', CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM rol WHERE name = 'ADMIN'
);

INSERT INTO rol (name, description, status, created_by, created_at)
SELECT 'USER', 'Rol de usuario del sistema', 'ACTIVE', 'system', CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM rol WHERE name = 'USER'
);

INSERT INTO "user" (username, email, password, status, created_by, created_at)
SELECT 'admin', 'admin@ktestfull.local', 'admin123', 'ACTIVE', 'system', CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM "user" WHERE email = 'admin@ktestfull.local'
);

DO $$
DECLARE
    v_user_id INTEGER;
    v_rol_id INTEGER;
BEGIN
    SELECT id_user INTO v_user_id FROM "user" WHERE email = 'admin@ktestfull.local';
    SELECT id_rol INTO v_rol_id FROM rol WHERE name = 'ADMIN';

    IF NOT EXISTS (
        SELECT 1 FROM user_rol WHERE user_id = v_user_id AND rol_id = v_rol_id
    ) THEN
        INSERT INTO user_rol (user_id, rol_id, status, created_by, created_at)
        VALUES (v_user_id, v_rol_id, 'ACTIVE', 'system', CURRENT_TIMESTAMP);
    END IF;
END
$$;