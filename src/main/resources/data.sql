INSERT INTO roles (id, name)
VALUES (gen_random_uuid(), 'ROLE_USER')
ON CONFLICT (name) DO NOTHING;

INSERT INTO roles (id, name)
VALUES (gen_random_uuid(), 'ROLE_ADMIN')
ON CONFLICT (name) DO NOTHING;

INSERT INTO roles (id, name)
VALUES (gen_random_uuid(), 'ROLE_VENDEDOR')
ON CONFLICT (name) DO NOTHING;

INSERT INTO roles (id, name)
VALUES (gen_random_uuid(), 'ROLE_PUBLICISTA')
ON CONFLICT (name) DO NOTHING;