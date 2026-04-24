CREATE TABLE insights (
      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
      user_id UUID REFERENCES users(id) ON DELETE CASCADE,
      message TEXT NOT NULL,
      type VARCHAR(50),
      generated_at TIMESTAMP DEFAULT NOW()
);