PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS mood_notes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    content TEXT NOT NULL,
    mood TEXT NOT NULL CHECK (mood IN ('calm', 'happy', 'sad', 'angry', 'anxious', 'tired')),
    owner_username TEXT NOT NULL,
    created_at TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (owner_username) REFERENCES users(username) ON DELETE CASCADE
);

-- 默认管理员密码是 123456（已使用 PBKDF2 加盐哈希存储）
INSERT OR IGNORE INTO users(username, password) VALUES
('admin', '120000$jyxNgKGyPE1eb3CBkqO0xQ==$myTuCZGvDAFpSSipH7vGFSjoT/30MxUQNvVybN3A7I4=');

INSERT INTO mood_notes(content, mood, owner_username) VALUES
('今天完成了一个棘手任务，松了一口气。', 'calm', 'admin'),
('刚喝完咖啡，突然觉得一切都可以再试一次。', 'happy', 'admin'),
('有点焦虑，但我在慢慢整理思绪。', 'anxious', 'admin');
