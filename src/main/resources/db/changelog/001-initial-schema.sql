CREATE TABLE IF NOT EXISTS application_info (
  id SERIAL PRIMARY KEY,
  key VARCHAR(255) UNIQUE NOT NULL,
  value TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO application_info (key, value) VALUES ('Version', '0.0.1');