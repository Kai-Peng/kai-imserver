-- keys: user_id
-- argv: device_type, device_data

local data = redis.call("HGET", KEYS[1], ARGV[1])
redis.call("HSET", KEYS[1], ARGV[1], ARGV[2])

return data