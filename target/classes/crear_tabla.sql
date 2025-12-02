-- Este archivo crea la tabla "blocks", donde se van guardando todos los bloques. --

-- Si la tabla no existe, se crea. --
-- Aquí se guardan todos los bloques con su información. --
CREATE TABLE IF NOT EXISTS blocks (

    -- ID único de cada bloque. --
    -- SQLite lo hace autoincremental automáticamente. --
    id INTEGER PRIMARY KEY AUTOINCREMENT,

    -- Momento exacto en que se creó el bloque. --
    -- Se guarda como texto con el formato: "yyyy-MM-dd HH:mm:ss". --
    timestamp TEXT NOT NULL,

    -- Datos que el usuario guarda dentro del bloque. --
    -- Puede ser cualquier texto. --
    datos TEXT NOT NULL,

    -- Hash del bloque anterior. --
    -- En el primer bloque siempre será "GENESIS". --
    hash_anterior TEXT NOT NULL,

    -- Hash generado para este bloque. --
    -- Depende de: id + timestamp + datos + hash_anterior. --
    -- Esto sirve para detectar si alguien modifica algo. --
    hash_actual TEXT NOT NULL
);

