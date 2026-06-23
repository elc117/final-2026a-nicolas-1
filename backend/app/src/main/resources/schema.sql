-- Tabela de ingredientes
CREATE TABLE IF NOT EXISTS ingredients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    measurement_unit VARCHAR(20) NOT NULL,
    current_amount NUMERIC(10, 2) NOT NULL,
    minimum_stock NUMERIC(10, 2) NOT NULL
);

-- Tabela de usuarios
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(50) NOT NULL,
    password VARCHAR(60) NOT NULL,
    access_profile VARCHAR(20) NOT NULL
);

-- Tabela de funcionarios
CREATE TABLE IF NOT EXISTS employees (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(70) NOT NULL,
    surname VARCHAR(70) NOT NULL,
    cpf VARCHAR(15) NOT NULL,
    role VARCHAR(50) NOT NULL,
    has_access BOOLEAN DEFAULT FALSE,
    user_id BIGINT UNIQUE,

    -- Cria chave estrangeira
    CONSTRAINT fk_employee_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE SET NULL
);
