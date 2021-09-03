# Unique Id generator

id-generator is a simple Snowflake based unique ID generator. 
Inspired by: [uid-generator](https://github.com/baidu/uid-generator)

## How It Works

A unique ID consists of node ID, timestamp, and sequence within that timestamp. Usually, it is a 64 bits number(long), and the default bits of that three fields are as follows:

- sign(1bit) - The highest bit is always 0.
- delta seconds (29 bits) - The next 29 bits, represents delta seconds since a customer epoch(2020-01-01). The maximum time will be 17 years.
- node ID (24 bits) - The generator uses the last 24 bits value of the IP address as the node ID (CIDR is /24).
- sequence (10 bits) - the lasts 10 bits represent sequence within the one second (maximum is 1024 per second).

**Pros:**

- No database dependency (Implemented as a library and easy to use in all services)
- Work well in distributed systems
- Time-ordered
- Fast

**Cons:**

- Generating large numbers with 18 digits

**Some Ways to generate shorter IDs:**

- Using the last 16 bits of the IP address as the worker ID (if we can set the CIDR of k8s env is /16). 
- Run the ID generator as a separate service (In this case, we can ignore node ID).


## Usage

        long id = uidGenerator.nextId();
        ParsedId parsedId = uidGenerator.parseId(id);
        
        {"UID":"383693120938476544", "timestamp":"2021-09-03 01:23:59", "nodeId":"1077534", "sequence":"0"}