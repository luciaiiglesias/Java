CREATE TABLE categorias(
    id      SERIAL PRIMARY KEY,
    nombre  VARCHAR(100) NOT NULL
);

CREATE TABLE palabras(
    id              SERIAL PRIMARY KEY,
    palabra         VARCHAR(100) NOT NULL,
    id_categoria    INTEGER NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id) ON DELETE CASCADE
);

INSERT INTO categorias (nombre) VALUES ('Animales'), ('Países'), ('Tecnología');

INSERT INTO palabras(palabra, id_categoria) VALUES ('Perro', 1), ('Ardilla', 1), ('Delfin', 1);
INSERT INTO palabras(palabra, id_categoria) VALUES ('España', 2), ('Italia', 2), ('EEUU', 2);
INSERT INTO palabras(palabra, id_categoria) VALUES ('Software', 3), ('Java', 3), ('Programacion', 3);

SELECT * FROM categorias;
SELECT * FROM palabras;

SELECT palabra FROM palabras WHERE id_categoria = ?
