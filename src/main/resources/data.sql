BEGIN;
INSERT INTO users (id, email, password) VALUES (1, 'user@domain.com', '{bcrypt}$2a$10$PEaCGX7JrKlJ3rzxGYP1Eu.xF0d/V0PfR8WqMirGSbqCdoMwrv4zK');
INSERT INTO users_roles (users_id, roles) VALUES (1, 'ROLE_USER');
SELECT nextval('hibernate_sequence');
COMMIT;